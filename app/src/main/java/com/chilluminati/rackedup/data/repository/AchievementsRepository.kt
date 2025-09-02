package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.PersonalRecordDao
import com.chilluminati.rackedup.data.database.dao.ProgramDao
import com.chilluminati.rackedup.data.database.dao.WorkoutDao
import com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao
import com.chilluminati.rackedup.data.database.dao.ExerciseSetDao
import com.chilluminati.rackedup.data.database.dao.ExerciseDao
import com.chilluminati.rackedup.data.database.dao.BodyMeasurementDao
import com.chilluminati.rackedup.data.database.dao.UserProfileDao
import com.chilluminati.rackedup.data.database.entity.Workout
import com.chilluminati.rackedup.data.database.entity.Exercise
import com.chilluminati.rackedup.data.database.entity.BodyMeasurement
import com.chilluminati.rackedup.data.database.entity.WorkoutExercise
import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.data.database.entity.PersonalRecord
import com.chilluminati.rackedup.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import com.chilluminati.rackedup.data.database.entity.Program

/**
 * Production-ready achievements engine that evaluates unlocks from existing app data.
 * No schema changes required; achievements are computed on the fly from workouts, PRs, and programs.
 */
@Singleton
class AchievementsRepository @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val personalRecordDao: PersonalRecordDao,
    private val programDao: ProgramDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val exerciseDao: ExerciseDao,
    private val bodyMeasurementDao: BodyMeasurementDao,
    private val userProfileDao: UserProfileDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    enum class Category { Milestones, Consistency, Volume, Sets, Duration, Strength, TimeOfDay, Programs, AdvancedTracking }

    data class Definition(
        val id: String,
        val title: String,
        val description: String,
        val category: Category,
        val points: Int = 10
    )

    data class State(
        val definition: Definition,
        val isUnlocked: Boolean,
        val unlockedAt: Date? = null
    )

    /** Public observable of achievement states. */
    fun observeAchievements(): Flow<List<State>> {
        // Recompute when any of workouts, PRs, programs, exercises, or body measurements change
        return combine(
            listOf(
                workoutDao.getAllWorkouts(),
                personalRecordDao.getAllPersonalRecordsFlow(),
                programDao.getAllPrograms(),
                exerciseDao.getAllExercises(),
                bodyMeasurementDao.getBodyMeasurements("weight"), // Use weight as default type
                workoutExerciseDao.getAllWorkoutExercisesFlow(),
                exerciseSetDao.getAllExerciseSetsFlow()
            )
        ) { values ->
            val workouts = values[0] as List<Workout>
            val prs = values[1] as List<PersonalRecord>
            val programs = values[2] as List<Program>
            val exercises = values[3] as List<Exercise>
            val bodyMeasurements = values[4] as List<BodyMeasurement>
            val workoutExercises = values[5] as List<WorkoutExercise>
            val exerciseSets = values[6] as List<ExerciseSet>
            computeStates(workouts, prs.map { it.achievedAt }, programs, exercises, bodyMeasurements, workoutExercises, exerciseSets)
        }
    }

    private fun computeStates(
        workouts: List<Workout>,
        prDates: List<Date>,
        programs: List<Program>,
        exercises: List<Exercise>,
        bodyMeasurements: List<BodyMeasurement>,
        workoutExercises: List<com.chilluminati.rackedup.data.database.entity.WorkoutExercise>,
        exerciseSets: List<ExerciseSet>
    ): List<State> {
        val workoutsSorted = workouts.filter { it.isCompleted }.sortedBy { it.date }
        val totalWorkouts = workoutsSorted.size

        val daysWithWorkouts = workoutsSorted.map { truncateToDay(it.date) }.toSet()
        val longestStreak = calculateLongestStreak(daysWithWorkouts)

        val weekStats = countWorkoutsThisWeek(workoutsSorted)
        val monthStats = countWorkoutsThisMonth(workoutsSorted)

        val earlyBirdDate = workoutsSorted.firstOrNull { it.startTime?.let { d -> hourOfDay(d) < 6 } == true }?.date
        val nightOwlDate = workoutsSorted.firstOrNull { (it.endTime ?: it.startTime)?.let { d -> hourOfDay(d) >= 22 } == true }?.date

        val defs = definitions()
        val programStartDates = programs.mapNotNull { it.startDate ?: it.createdAt }
        val programFinishAny = programs.firstOrNull { it.endDate != null }
        val programFinish8 = programs.firstOrNull { it.endDate != null && (it.durationWeeks ?: 0) >= 8 }
        val programFinish12 = programs.firstOrNull { it.endDate != null && (it.durationWeeks ?: 0) >= 12 }
        
        // Calculate exercise variety metrics based on exercises actually performed with completed sets
        val completedSetExerciseIds = exerciseSets
            .filter { it.isCompleted }
            .map { it.workoutExerciseId }
            .distinct()
            .flatMap { workoutExerciseId ->
                workoutExercises.filter { it.id == workoutExerciseId }.map { it.exerciseId }
            }
            .distinct()
        
        val performedExercises = exercises.filter { it.id in completedSetExerciseIds }
        val uniqueExercises = performedExercises.size
        val uniqueMuscleGroups = performedExercises.flatMap { it.muscleGroups }.distinct().size
        val uniqueEquipment = performedExercises.map { it.equipment }.distinct().size
        
        // Calculate body measurement metrics - only count meaningful measurements
        val meaningfulMeasurements = bodyMeasurements.filter { it.value > 0 && it.measurementType.isNotBlank() }
        val bodyMeasurementCount = meaningfulMeasurements.size
        val firstMeasurementDate = meaningfulMeasurements.minByOrNull { it.measuredAt }?.measuredAt
        val nthMeasurementDate = if (meaningfulMeasurements.size >= 10) meaningfulMeasurements.sortedBy { it.measuredAt }[9].measuredAt else null

        return defs.map { def ->
            val (unlocked, date) = when (def.id) {
                // Milestones - workout counts
                "workout_1" -> reachedCount(totalWorkouts, 1) to nthWorkoutDate(workoutsSorted, 1)
                "workout_5" -> reachedCount(totalWorkouts, 5) to nthWorkoutDate(workoutsSorted, 5)
                "workout_10" -> reachedCount(totalWorkouts, 10) to nthWorkoutDate(workoutsSorted, 10)
                "workout_25" -> reachedCount(totalWorkouts, 25) to nthWorkoutDate(workoutsSorted, 25)
                "workout_50" -> reachedCount(totalWorkouts, 50) to nthWorkoutDate(workoutsSorted, 50)
                "workout_100" -> reachedCount(totalWorkouts, 100) to nthWorkoutDate(workoutsSorted, 100)
                "workout_250" -> reachedCount(totalWorkouts, 250) to nthWorkoutDate(workoutsSorted, 250)
                "workout_500" -> reachedCount(totalWorkouts, 500) to nthWorkoutDate(workoutsSorted, 500)
                "workout_750" -> reachedCount(totalWorkouts, 750) to nthWorkoutDate(workoutsSorted, 750)
                "workout_1000" -> reachedCount(totalWorkouts, 1000) to nthWorkoutDate(workoutsSorted, 1000)

                // Consistency - streaks and current week/month totals
                "streak_3" -> (longestStreak >= 3) to streakUnlockedDate(daysWithWorkouts, 3)
                "streak_7" -> (longestStreak >= 7) to streakUnlockedDate(daysWithWorkouts, 7)
                "streak_14" -> (longestStreak >= 14) to streakUnlockedDate(daysWithWorkouts, 14)
                "streak_30" -> (longestStreak >= 30) to streakUnlockedDate(daysWithWorkouts, 30)
                "week_3" -> (weekStats >= 3) to lastWorkoutThisWeek(workoutsSorted)
                "week_5" -> (weekStats >= 5) to lastWorkoutThisWeek(workoutsSorted)
                "month_12" -> (monthStats >= 12) to lastWorkoutThisMonth(workoutsSorted)
                "month_16" -> (monthStats >= 16) to lastWorkoutThisMonth(workoutsSorted)
                "month_20" -> (monthStats >= 20) to lastWorkoutThisMonth(workoutsSorted)

                // Volume / Sets / Duration totals
                "volume_10k" -> thresholdDateByCumulative(workoutsSorted) { it.totalVolume }(10_000.0)
                "volume_100k" -> thresholdDateByCumulative(workoutsSorted) { it.totalVolume }(100_000.0)
                "volume_1m" -> thresholdDateByCumulative(workoutsSorted) { it.totalVolume }(1_000_000.0)
                "sets_100" -> thresholdDateByCumulative(workoutsSorted) { it.totalSets.toDouble() }(100.0)
                "sets_500" -> thresholdDateByCumulative(workoutsSorted) { it.totalSets.toDouble() }(500.0)
                "sets_1000" -> thresholdDateByCumulative(workoutsSorted) { it.totalSets.toDouble() }(1000.0)
                "duration_500" -> thresholdDateByCumulative(workoutsSorted) { (it.durationMinutes ?: 0).toDouble() }(500.0)
                "duration_2000" -> thresholdDateByCumulative(workoutsSorted) { (it.durationMinutes ?: 0).toDouble() }(2000.0)

                // Strength / PRs
                "first_pr" -> (prDates.isNotEmpty()) to prDates.minOrNull()

                // Time of day
                "early_bird" -> (earlyBirdDate != null) to earlyBirdDate
                "night_owl" -> (nightOwlDate != null) to nightOwlDate

                // Programs
                "program_starter" -> (programStartDates.isNotEmpty()) to programStartDates.minOrNull()
                "program_finisher" -> ((programFinishAny != null) to programFinishAny?.endDate)
                "program_finisher_8" -> ((programFinish8 != null) to programFinish8?.endDate)
                "program_finisher_12" -> ((programFinish12 != null) to programFinish12?.endDate)

                // Single-session thresholds
                "single_volume_10k" -> singleWorkoutThreshold(workoutsSorted) { it.totalVolume }(10_000.0)
                "single_volume_20k" -> singleWorkoutThreshold(workoutsSorted) { it.totalVolume }(20_000.0)
                "single_volume_40k" -> singleWorkoutThreshold(workoutsSorted) { it.totalVolume }(40_000.0)
                "single_sets_30" -> singleWorkoutThreshold(workoutsSorted) { it.totalSets.toDouble() }(30.0)
                "single_sets_50" -> singleWorkoutThreshold(workoutsSorted) { it.totalSets.toDouble() }(50.0)
                "single_duration_90" -> singleWorkoutThreshold(workoutsSorted) { (it.durationMinutes ?: 0).toDouble() }(90.0)
                "single_duration_120" -> singleWorkoutThreshold(workoutsSorted) { (it.durationMinutes ?: 0).toDouble() }(120.0)

                // Consistency extended
                "week_all7" -> weekAllSeven(workoutsSorted)
                "year_weeks" -> yearEveryWeek(workoutsSorted)

                // Logging/quality
                "first_rated" -> firstWorkoutWith(workoutsSorted) { it.rating != null }
                "ten_rated" -> nthWorkoutWith(workoutsSorted, 10) { it.rating != null }
                "first_favorite" -> firstWorkoutWith(workoutsSorted) { it.isFavorite }
                "ten_favorite" -> nthWorkoutWith(workoutsSorted, 10) { it.isFavorite }

                // NEW: Exercise Variety Achievements
                "exercise_10" -> (uniqueExercises >= 10) to null
                "exercise_25" -> (uniqueExercises >= 25) to null
                "exercise_50" -> (uniqueExercises >= 50) to null
                "muscle_groups_5" -> (uniqueMuscleGroups >= 5) to null
                "muscle_groups_all" -> (uniqueMuscleGroups >= 8) to null // Assuming 8 major muscle groups
                "equipment_3" -> (uniqueEquipment >= 3) to null
                "equipment_all" -> (uniqueEquipment >= 5) to null // Assuming 5 equipment types

                // NEW: Body Measurement Achievements
                "measurement_first" -> (bodyMeasurementCount >= 1) to firstMeasurementDate
                "measurement_10" -> (bodyMeasurementCount >= 10) to nthMeasurementDate
                "measurement_month" -> (bodyMeasurementCount >= 30) to nthMeasurementDate // Assuming 30 days for a month

                // NEW: Advanced Consistency Achievements
                "streak_60" -> (longestStreak >= 60) to streakUnlockedDate(daysWithWorkouts, 60)
                "streak_100" -> (longestStreak >= 100) to streakUnlockedDate(daysWithWorkouts, 100)
                "year_consistency" -> (daysWithWorkouts.size >= 365) to null // Assuming 365 days for a year

                // NEW: Template Achievements
                "template_creator" -> workouts.any { it.isTemplate && it.name.isNotBlank() } to workouts.firstOrNull { it.isTemplate && it.name.isNotBlank() }?.date
                "template_user" -> workouts.any { it.isTemplate && it.name.isNotBlank() } to workouts.firstOrNull { it.isTemplate && it.name.isNotBlank() }?.date

                // NEW: Advanced Volume Achievements
                "volume_5m" -> thresholdDateByCumulative(workoutsSorted) { it.totalVolume }(5_000_000.0)
                "volume_10m" -> thresholdDateByCumulative(workoutsSorted) { it.totalVolume }(10_000_000.0)

                // NEW: Time-Based Achievements
                "workout_365" -> reachedCount(totalWorkouts, 365) to nthWorkoutDate(workoutsSorted, 365)
                "workout_500_days" -> reachedCount(totalWorkouts, 500) to nthWorkoutDate(workoutsSorted, 500)

                // NEW: Special Session Achievements
                "single_duration_180" -> singleWorkoutThreshold(workoutsSorted) { (it.durationMinutes ?: 0).toDouble() }(180.0)
                "single_sets_100" -> singleWorkoutThreshold(workoutsSorted) { it.totalSets.toDouble() }(100.0)

                // NEW: Advanced Tracking Achievements
                "weight_progression_5" -> (meaningfulMeasurements.filter { it.measurementType == "Weight" }.size >= 5) to meaningfulMeasurements.filter { it.measurementType == "Weight" }.sortedBy { it.measuredAt }.getOrNull(4)?.measuredAt
                "weight_progression_10" -> (meaningfulMeasurements.filter { it.measurementType == "Weight" }.size >= 10) to meaningfulMeasurements.filter { it.measurementType == "Weight" }.sortedBy { it.measuredAt }.getOrNull(9)?.measuredAt
                "body_fat_tracking" -> (meaningfulMeasurements.any { it.measurementType == "Body Fat" }) to meaningfulMeasurements.filter { it.measurementType == "Body Fat" }.minByOrNull { it.measuredAt }?.measuredAt
                "measurement_streak_7" -> (hasMeasurementStreak(meaningfulMeasurements, 7)) to getMeasurementStreakDate(meaningfulMeasurements, 7)
                "measurement_streak_30" -> (hasMeasurementStreak(meaningfulMeasurements, 30)) to getMeasurementStreakDate(meaningfulMeasurements, 30)
                "weekly_volume_consistency" -> (hasWeeklyVolumeConsistency(workoutsSorted, 4)) to getWeeklyVolumeConsistencyDate(workoutsSorted, 4)
                "monthly_volume_consistency" -> (hasMonthlyVolumeConsistency(workoutsSorted, 3)) to getMonthlyVolumeConsistencyDate(workoutsSorted, 3)
                "progressive_overload" -> (hasProgressiveOverload(workoutsSorted)) to getProgressiveOverloadDate(workoutsSorted)
                "deload_master" -> (hasDeloadCycles(workoutsSorted)) to getDeloadCycleDate(workoutsSorted)
                "time_under_tension_master" -> (hasTimeUnderTensionFocus(workoutsSorted)) to getTimeUnderTensionDate(workoutsSorted)
                "frequency_master" -> (hasHighFrequencyTraining(workoutsSorted)) to getHighFrequencyDate(workoutsSorted)
                "intensity_master" -> (hasHighIntensityTraining(workoutsSorted)) to getHighIntensityDate(workoutsSorted)
                "recovery_tracker" -> (hasRecoveryTracking(workoutsSorted)) to getRecoveryTrackingDate(workoutsSorted)
                "periodization_master" -> (hasPeriodization(workoutsSorted)) to getPeriodizationDate(workoutsSorted)
                "data_analyst" -> (hasComprehensiveTracking(workoutsSorted, meaningfulMeasurements)) to getComprehensiveTrackingDate(workoutsSorted, meaningfulMeasurements)

                else -> false to null
            }
            State(definition = def, isUnlocked = unlocked, unlockedAt = date)
        }
    }

    private fun definitions(): List<Definition> = listOf(
        // Milestones
        Definition("workout_1", "First Workout", "Complete your first workout", Category.Milestones, 10),
        Definition("workout_5", "Five Workouts", "Complete 5 workouts", Category.Milestones, 10),
        Definition("workout_10", "Ten Workouts", "Complete 10 workouts", Category.Milestones, 10),
        Definition("workout_25", "Twenty-Five", "Complete 25 workouts", Category.Milestones, 15),
        Definition("workout_50", "Fifty Strong", "Complete 50 workouts", Category.Milestones, 20),
        Definition("workout_100", "Century", "Complete 100 workouts", Category.Milestones, 30),
        Definition("workout_250", "Quarter-K", "Complete 250 workouts", Category.Milestones, 40),
        Definition("workout_500", "Half-K", "Complete 500 workouts", Category.Milestones, 60),
        Definition("workout_750", "Seven-Fifty", "Complete 750 workouts", Category.Milestones, 80),
        Definition("workout_1000", "Millennium", "Complete 1000 workouts", Category.Milestones, 100),

        // Consistency
        Definition("streak_3", "Consistency I", "Work out 3 days in a row", Category.Consistency, 10),
        Definition("streak_7", "Consistency II", "Work out 7 days in a row", Category.Consistency, 15),
        Definition("streak_14", "Consistency III", "Work out 14 days in a row", Category.Consistency, 20),
        Definition("streak_30", "Consistency IV", "Work out 30 days in a row", Category.Consistency, 30),
        Definition("week_3", "Weekly Warrior", "Train 3+ days this week", Category.Consistency, 10),
        Definition("week_5", "Weekly Elite", "Train 5+ days this week", Category.Consistency, 20),
        Definition("month_12", "Monthly Momentum", "Train 12+ days this month", Category.Consistency, 15),
        Definition("month_16", "Monthly Grinder", "Train 16+ days this month", Category.Consistency, 20),
        Definition("month_20", "Monthly Machine", "Train 20+ days this month", Category.Consistency, 30),
        Definition("week_all7", "All Week", "Train all 7 days in a week", Category.Consistency, 25),
        Definition("year_weeks", "No-Miss Year", "Workout every week for a year", Category.Consistency, 60),

        // Volume
        Definition("volume_10k", "Volume Up", "Accumulate 10k lbs of volume", Category.Volume, 10),
        Definition("volume_100k", "Volume Monster", "Accumulate 100k lbs of volume", Category.Volume, 20),
        Definition("volume_1m", "Million Mover", "Accumulate 1,000,000 lbs of volume", Category.Volume, 40),
        Definition("single_volume_10k", "Ten-K Blast", "10k volume in one workout", Category.Volume, 15),
        Definition("single_volume_20k", "Twenty-K Blast", "20k volume in one workout", Category.Volume, 25),
        Definition("single_volume_40k", "Forty-K Blast", "40k volume in one workout", Category.Volume, 35),

        // Sets
        Definition("sets_100", "Set Centurion", "Log 100 total sets", Category.Sets, 10),
        Definition("sets_500", "Set General", "Log 500 total sets", Category.Sets, 20),
        Definition("sets_1000", "Set Legend", "Log 1,000 total sets", Category.Sets, 40),
        Definition("single_sets_30", "Set Marathon", "30 sets in one workout", Category.Sets, 15),
        Definition("single_sets_50", "Set Ultra", "50 sets in one workout", Category.Sets, 30),

        // Duration
        Definition("duration_500", "Time Under Tension", "Train 500 total minutes", Category.Duration, 15),
        Definition("duration_2000", "Gym Resident", "Train 2,000 total minutes", Category.Duration, 40),
        Definition("single_duration_90", "Long Haul", "90 minute workout", Category.Duration, 15),
        Definition("single_duration_120", "Marathon Session", "120 minute workout", Category.Duration, 30),

        // Strength / PRs
        Definition("first_pr", "PR Hunter", "Set any personal record", Category.Strength, 20),

        // Time of day
        Definition("early_bird", "Early Bird", "Workout before 6 AM", Category.TimeOfDay, 15),
        Definition("night_owl", "Night Owl", "Workout after 10 PM", Category.TimeOfDay, 15),

        // Programs
        Definition("program_starter", "Program Starter", "Create or start a program", Category.Programs, 10),
        Definition("program_finisher", "Program Finisher", "Complete any program", Category.Programs, 20),
        Definition("program_finisher_8", "8-Week Warrior", "Complete an 8+ week program", Category.Programs, 30),
        Definition("program_finisher_12", "12-Week Champion", "Complete a 12+ week program", Category.Programs, 50),

        // Quality/Logging
        Definition("first_rated", "First Rating", "Rate a workout", Category.Milestones, 5),
        Definition("ten_rated", "Rater", "Rate 10 workouts", Category.Milestones, 15),
        Definition("first_favorite", "Favorite", "Mark a workout favorite", Category.Milestones, 5),
        Definition("ten_favorite", "Collector", "Mark 10 workouts as favorites", Category.Milestones, 15),

        // NEW: Exercise Variety Achievements
        Definition("exercise_10", "Exercise Explorer", "Try 10 different exercises", Category.Milestones, 15),
        Definition("exercise_25", "Exercise Enthusiast", "Try 25 different exercises", Category.Milestones, 25),
        Definition("exercise_50", "Exercise Master", "Try 50 different exercises", Category.Milestones, 40),
        Definition("muscle_groups_5", "Balanced Builder", "Train 5 different muscle groups", Category.Milestones, 20),
        Definition("muscle_groups_all", "Full Body Warrior", "Train all major muscle groups", Category.Milestones, 35),
        Definition("equipment_3", "Equipment Explorer", "Use 3 different types of equipment", Category.Milestones, 15),
        Definition("equipment_all", "Equipment Master", "Use all equipment types", Category.Milestones, 30),

        // NEW: Body Measurement Achievements
        Definition("measurement_first", "Progress Tracker", "Log your first body measurement", Category.Milestones, 10),
        Definition("measurement_10", "Dedicated Tracker", "Log 10 body measurements", Category.Milestones, 20),
        Definition("measurement_month", "Monthly Tracker", "Track measurements for a full month", Category.Milestones, 25),

        // NEW: Advanced Consistency Achievements
        Definition("streak_60", "Two Month Warrior", "Work out 60 days in a row", Category.Consistency, 50),
        Definition("streak_100", "Century Streak", "Work out 100 days in a row", Category.Consistency, 75),
        Definition("year_consistency", "Year-Round Warrior", "Work out every month for a year", Category.Consistency, 60),

        // NEW: Template Achievements
        Definition("template_creator", "Template Creator", "Create your first workout template", Category.Milestones, 15),
        Definition("template_user", "Template User", "Use a workout template", Category.Milestones, 10),

        // NEW: Advanced Volume Achievements
        Definition("volume_5m", "Five Million Mover", "Accumulate 5,000,000 lbs of volume", Category.Volume, 80),
        Definition("volume_10m", "Ten Million Mover", "Accumulate 10,000,000 lbs of volume", Category.Volume, 100),

        // NEW: Time-Based Achievements
        Definition("workout_365", "Year of Fitness", "Complete 365 workouts", Category.Milestones, 80),
        Definition("workout_500_days", "500 Days Strong", "Complete 500 workouts", Category.Milestones, 90),

        // NEW: Special Session Achievements
        Definition("single_duration_180", "Triple Marathon", "Complete a 3-hour workout", Category.Duration, 50),
        Definition("single_sets_100", "Century Sets", "Complete 100 sets in one workout", Category.Sets, 60),

        // NEW: Advanced Tracking Achievements
        Definition("weight_progression_5", "Weight Tracker", "Log 5 weight measurements", Category.AdvancedTracking, 20),
        Definition("weight_progression_10", "Weight Analyst", "Log 10 weight measurements", Category.AdvancedTracking, 35),
        Definition("body_fat_tracking", "Body Composition Tracker", "Track body fat percentage", Category.AdvancedTracking, 25),
        Definition("measurement_streak_7", "Weekly Tracker", "Track measurements for 7 days straight", Category.AdvancedTracking, 30),
        Definition("measurement_streak_30", "Monthly Tracker", "Track measurements for 30 days straight", Category.AdvancedTracking, 50),
        Definition("weekly_volume_consistency", "Volume Consistency", "Maintain consistent weekly volume for 4 weeks", Category.AdvancedTracking, 40),
        Definition("monthly_volume_consistency", "Volume Master", "Maintain consistent monthly volume for 3 months", Category.AdvancedTracking, 60),
        Definition("progressive_overload", "Progressive Overload", "Demonstrate progressive overload in your training", Category.AdvancedTracking, 45),
        Definition("deload_master", "Deload Master", "Implement proper deload cycles", Category.AdvancedTracking, 35),
        Definition("time_under_tension_master", "TUT Master", "Focus on time under tension training", Category.AdvancedTracking, 30),
        Definition("frequency_master", "Frequency Master", "Train with high frequency (5+ days/week)", Category.AdvancedTracking, 40),
        Definition("intensity_master", "Intensity Master", "Train with high intensity consistently", Category.AdvancedTracking, 45),
        Definition("recovery_tracker", "Recovery Tracker", "Track recovery metrics and patterns", Category.AdvancedTracking, 25),
        Definition("periodization_master", "Periodization Master", "Follow structured periodization", Category.AdvancedTracking, 70),
        Definition("data_analyst", "Data Analyst", "Comprehensive tracking of all metrics", Category.AdvancedTracking, 80)
    )

    private fun truncateToDay(date: Date): Date {
        val cal = Calendar.getInstance().apply { time = date }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    private fun nthWorkoutDate(workouts: List<Workout>, n: Int): Date? =
        if (workouts.size >= n) workouts.sortedBy { it.date }[n - 1].date else null

    private fun reachedCount(total: Int, threshold: Int): Boolean = total >= threshold

    private fun streakUnlockedDate(days: Set<Date>, threshold: Int): Date? {
        if (days.isEmpty()) return null
        val sorted = days.toList().sorted()
        val cal = Calendar.getInstance()
        var current = 1
        for (i in 1 until sorted.size) {
            cal.time = sorted[i - 1]
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val prevPlusOne = cal.time
            if (!sorted[i].before(prevPlusOne) && !sorted[i].after(prevPlusOne)) {
                current += 1
                if (current == threshold) return sorted[i]
            } else {
                current = 1
            }
        }
        return null
    }

    private fun lastWorkoutThisWeek(workouts: List<Workout>): Date? {
        val cal = Calendar.getInstance()
        val end = cal.time
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.time
        return workouts.filter { it.date.after(start) && it.date.before(end) }.maxByOrNull { it.date }?.date
    }

    private fun lastWorkoutThisMonth(workouts: List<Workout>): Date? {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.time
        val end = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.time
        return workouts.filter { it.date.after(start) && it.date.before(end) }.maxByOrNull { it.date }?.date
    }

    private fun thresholdDateByCumulative(workouts: List<Workout>, value: (Workout) -> Double): (Double) -> Pair<Boolean, Date?> = { threshold ->
        var sum = 0.0
        var date: Date? = null
        for (w in workouts.sortedBy { it.date }) {
            sum += value(w)
            if (sum >= threshold) { date = w.date; break }
        }
        (sum >= threshold) to date
    }

    private fun singleWorkoutThreshold(workouts: List<Workout>, value: (Workout) -> Double): (Double) -> Pair<Boolean, Date?> = { threshold ->
        val w = workouts.firstOrNull { value(it) >= threshold }
        (w != null) to w?.date
    }

    private fun weekAllSeven(workouts: List<Workout>): Pair<Boolean, Date?> {
        if (workouts.isEmpty()) return false to null
        val grouped = workouts.groupBy { weekOfYear(it.date) to yearOf(it.date) }
        val match = grouped.entries.firstOrNull { (_, list) ->
            val days = list.map { dayOfWeek(it.date) }.toSet()
            days.size == 7
        }
        return (match != null) to (match?.value?.maxByOrNull { it.date }?.date)
    }

    private fun yearEveryWeek(workouts: List<Workout>): Pair<Boolean, Date?> {
        if (workouts.isEmpty()) return false to null
        val byYear = workouts.groupBy { yearOf(it.date) }
        val okYear = byYear.entries.firstOrNull { (_, list) ->
            val weeks = list.map { weekOfYear(it.date) }.toSet()
            weeks.size >= 52
        }
        return (okYear != null) to (okYear?.value?.maxByOrNull { it.date }?.date)
    }

    private fun firstWorkoutWith(workouts: List<Workout>, predicate: (Workout) -> Boolean): Pair<Boolean, Date?> {
        val w = workouts.firstOrNull { predicate(it) }
        return (w != null) to w?.date
    }

    private fun nthWorkoutWith(workouts: List<Workout>, n: Int, predicate: (Workout) -> Boolean): Pair<Boolean, Date?> {
        val list = workouts.filter(predicate)
        return (list.size >= n) to list.getOrNull(n - 1)?.date
    }

    private fun weekOfYear(date: Date): Int = Calendar.getInstance().apply { time = date }.get(Calendar.WEEK_OF_YEAR)
    private fun yearOf(date: Date): Int = Calendar.getInstance().apply { time = date }.get(Calendar.YEAR)
    private fun dayOfWeek(date: Date): Int = Calendar.getInstance().apply { time = date }.get(Calendar.DAY_OF_WEEK)

    private fun hourOfDay(date: Date): Int {
        val cal = Calendar.getInstance().apply { time = date }
        return cal.get(Calendar.HOUR_OF_DAY)
    }

    private fun calculateLongestStreak(days: Set<Date>): Int {
        if (days.isEmpty()) return 0
        val sorted = days.toList().sorted()
        var longest = 1
        var current = 1
        val cal = Calendar.getInstance()
        for (i in 1 until sorted.size) {
            cal.time = sorted[i - 1]
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val prevPlusOne = cal.time
            if (!sorted[i].before(prevPlusOne) && !sorted[i].after(prevPlusOne)) {
                current += 1
                if (current > longest) longest = current
            } else {
                current = 1
            }
        }
        return longest
    }

    private fun countWorkoutsThisWeek(workouts: List<Workout>): Int {
        val cal = Calendar.getInstance()
        val end = cal.time
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.time
        return workouts.count { it.date.after(start) && it.date.before(end) }
    }

    private fun countWorkoutsThisMonth(workouts: List<Workout>): Int {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.time
        val end = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.time
        return workouts.count { it.date.after(start) && it.date.before(end) }
    }

    // Advanced Tracking Helper Functions
    private fun hasMeasurementStreak(measurements: List<BodyMeasurement>, days: Int): Boolean {
        if (measurements.size < days) return false
        val sortedMeasurements = measurements.sortedBy { it.measuredAt }
        val cal = Calendar.getInstance()
        var currentStreak = 1
        
        for (i in 1 until sortedMeasurements.size) {
            cal.time = sortedMeasurements[i - 1].measuredAt
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val expectedDate = cal.time
            
            cal.time = sortedMeasurements[i].measuredAt
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val actualDate = cal.time
            
            if (actualDate == expectedDate) {
                currentStreak++
                if (currentStreak >= days) return true
            } else {
                currentStreak = 1
            }
        }
        return false
    }

    private fun getMeasurementStreakDate(measurements: List<BodyMeasurement>, days: Int): Date? {
        if (!hasMeasurementStreak(measurements, days)) return null
        val sortedMeasurements = measurements.sortedBy { it.measuredAt }
        return sortedMeasurements.getOrNull(days - 1)?.measuredAt
    }

    private fun hasWeeklyVolumeConsistency(workouts: List<Workout>, weeks: Int): Boolean {
        if (workouts.size < weeks * 3) return false // Need at least 3 workouts per week
        val weeklyVolumes = workouts.groupBy { weekOfYear(it.date) to yearOf(it.date) }
            .mapValues { (_, list) -> list.sumOf { it.totalVolume } }
            .values.toList()
        
        if (weeklyVolumes.size < weeks) return false
        
        // Check if volumes are within 20% of each other for consecutive weeks
        for (i in 0 until weeklyVolumes.size - weeks + 1) {
            val weekRange = weeklyVolumes.subList(i, i + weeks)
            val avgVolume = weekRange.average()
            val isConsistent = weekRange.all { volume ->
                volume >= avgVolume * 0.8 && volume <= avgVolume * 1.2
            }
            if (isConsistent) return true
        }
        return false
    }

    private fun getWeeklyVolumeConsistencyDate(workouts: List<Workout>, weeks: Int): Date? {
        if (!hasWeeklyVolumeConsistency(workouts, weeks)) return null
        val weeklyGroups = workouts.groupBy { weekOfYear(it.date) to yearOf(it.date) }
        return weeklyGroups.values.elementAtOrNull(weeks - 1)?.maxByOrNull { it.date }?.date
    }

    private fun hasMonthlyVolumeConsistency(workouts: List<Workout>, months: Int): Boolean {
        if (workouts.size < months * 12) return false // Need at least 12 workouts per month
        val monthlyVolumes = workouts.groupBy { monthOfYear(it.date) to yearOf(it.date) }
            .mapValues { (_, list) -> list.sumOf { it.totalVolume } }
            .values.toList()
        
        if (monthlyVolumes.size < months) return false
        
        // Check if volumes are within 25% of each other for consecutive months
        for (i in 0 until monthlyVolumes.size - months + 1) {
            val monthRange = monthlyVolumes.subList(i, i + months)
            val avgVolume = monthRange.average()
            val isConsistent = monthRange.all { volume ->
                volume >= avgVolume * 0.75 && volume <= avgVolume * 1.25
            }
            if (isConsistent) return true
        }
        return false
    }

    private fun getMonthlyVolumeConsistencyDate(workouts: List<Workout>, months: Int): Date? {
        if (!hasMonthlyVolumeConsistency(workouts, months)) return null
        val monthlyGroups = workouts.groupBy { monthOfYear(it.date) to yearOf(it.date) }
        return monthlyGroups.values.elementAtOrNull(months - 1)?.maxByOrNull { it.date }?.date
    }

    private fun hasProgressiveOverload(workouts: List<Workout>): Boolean {
        if (workouts.size < 10) return false
        val sortedWorkouts = workouts.sortedBy { it.date }
        
        // Look for patterns of increasing volume over time
        var progressiveCount = 0
        for (i in 0 until sortedWorkouts.size - 3) {
            val volumes = sortedWorkouts.subList(i, i + 4).map { it.totalVolume }
            if (volumes[0] < volumes[1] && volumes[1] < volumes[2] && volumes[2] < volumes[3]) {
                progressiveCount++
            }
        }
        return progressiveCount >= 2 // At least 2 instances of progressive overload
    }

    private fun getProgressiveOverloadDate(workouts: List<Workout>): Date? {
        if (!hasProgressiveOverload(workouts)) return null
        val sortedWorkouts = workouts.sortedBy { it.date }
        return sortedWorkouts.getOrNull(9)?.date // Return 10th workout date
    }

    private fun hasDeloadCycles(workouts: List<Workout>): Boolean {
        if (workouts.size < 15) return false
        val sortedWorkouts = workouts.sortedBy { it.date }
        
        // Look for patterns of volume reduction followed by increase
        var deloadCount = 0
        for (i in 0 until sortedWorkouts.size - 6) {
            val volumes = sortedWorkouts.subList(i, i + 7).map { it.totalVolume }
            val avgBefore = volumes.take(3).average()
            val avgDuring = volumes.subList(3, 5).average()
            val avgAfter = volumes.takeLast(2).average()
            
            if (avgDuring < avgBefore * 0.7 && avgAfter > avgDuring * 1.3) {
                deloadCount++
            }
        }
        return deloadCount >= 1
    }

    private fun getDeloadCycleDate(workouts: List<Workout>): Date? {
        if (!hasDeloadCycles(workouts)) return null
        val sortedWorkouts = workouts.sortedBy { it.date }
        return sortedWorkouts.getOrNull(14)?.date
    }

    private fun hasTimeUnderTensionFocus(workouts: List<Workout>): Boolean {
        if (workouts.size < 5) return false
        // Look for workouts with longer duration relative to volume
        val tutWorkouts = workouts.filter { workout ->
            val volumePerMinute = workout.totalVolume / (workout.durationMinutes ?: 1)
            volumePerMinute < 1000 // Lower volume per minute suggests TUT focus
        }
        return tutWorkouts.size >= 3
    }

    private fun getTimeUnderTensionDate(workouts: List<Workout>): Date? {
        if (!hasTimeUnderTensionFocus(workouts)) return null
        val tutWorkouts = workouts.filter { workout ->
            val volumePerMinute = workout.totalVolume / (workout.durationMinutes ?: 1)
            volumePerMinute < 1000
        }
        return tutWorkouts.sortedBy { it.date }.getOrNull(2)?.date
    }

    private fun hasHighFrequencyTraining(workouts: List<Workout>): Boolean {
        if (workouts.size < 10) return false
        val weeklyCounts = workouts.groupBy { weekOfYear(it.date) to yearOf(it.date) }
            .mapValues { (_, list) -> list.size }
            .values
        
        return weeklyCounts.any { it >= 5 } // At least one week with 5+ workouts
    }

    private fun getHighFrequencyDate(workouts: List<Workout>): Date? {
        if (!hasHighFrequencyTraining(workouts)) return null
        val weeklyGroups = workouts.groupBy { weekOfYear(it.date) to yearOf(it.date) }
        val highFreqWeek = weeklyGroups.entries.firstOrNull { (_, list) -> list.size >= 5 }
        return highFreqWeek?.value?.maxByOrNull { it.date }?.date
    }

    private fun hasHighIntensityTraining(workouts: List<Workout>): Boolean {
        if (workouts.size < 5) return false
        // Look for workouts with high volume in shorter duration
        val highIntensityWorkouts = workouts.filter { workout ->
            val volumePerMinute = workout.totalVolume / (workout.durationMinutes ?: 1)
            volumePerMinute > 2000 // High volume per minute suggests high intensity
        }
        return highIntensityWorkouts.size >= 3
    }

    private fun getHighIntensityDate(workouts: List<Workout>): Date? {
        if (!hasHighIntensityTraining(workouts)) return null
        val highIntensityWorkouts = workouts.filter { workout ->
            val volumePerMinute = workout.totalVolume / (workout.durationMinutes ?: 1)
            volumePerMinute > 2000
        }
        return highIntensityWorkouts.sortedBy { it.date }.getOrNull(2)?.date
    }

    private fun hasRecoveryTracking(workouts: List<Workout>): Boolean {
        if (workouts.size < 8) return false
        // Look for patterns of lighter workouts after intense ones
        val sortedWorkouts = workouts.sortedBy { it.date }
        var recoveryCount = 0
        
        for (i in 0 until sortedWorkouts.size - 1) {
            val current = sortedWorkouts[i]
            val next = sortedWorkouts[i + 1]
            
            val currentIntensity = current.totalVolume / (current.durationMinutes ?: 1)
            val nextIntensity = next.totalVolume / (next.durationMinutes ?: 1)
            
            if (currentIntensity > 1500 && nextIntensity < currentIntensity * 0.6) {
                recoveryCount++
            }
        }
        return recoveryCount >= 2
    }

    private fun getRecoveryTrackingDate(workouts: List<Workout>): Date? {
        if (!hasRecoveryTracking(workouts)) return null
        val sortedWorkouts = workouts.sortedBy { it.date }
        return sortedWorkouts.getOrNull(7)?.date
    }

    private fun hasPeriodization(workouts: List<Workout>): Boolean {
        if (workouts.size < 20) return false
        // Look for structured patterns in volume over time
        val sortedWorkouts = workouts.sortedBy { it.date }
        val volumes = sortedWorkouts.map { it.totalVolume }
        
        // Check for wave-like patterns (increasing then decreasing volume)
        var periodizationCount = 0
        for (i in 0 until volumes.size - 5) {
            val wave = volumes.subList(i, i + 6)
            if (wave[0] < wave[1] && wave[1] < wave[2] && wave[2] > wave[3] && wave[3] > wave[4]) {
                periodizationCount++
            }
        }
        return periodizationCount >= 1
    }

    private fun getPeriodizationDate(workouts: List<Workout>): Date? {
        if (!hasPeriodization(workouts)) return null
        val sortedWorkouts = workouts.sortedBy { it.date }
        return sortedWorkouts.getOrNull(19)?.date
    }

    private fun hasComprehensiveTracking(workouts: List<Workout>, measurements: List<BodyMeasurement>): Boolean {
        // Check if user has comprehensive tracking across multiple metrics
        val hasWorkoutTracking = workouts.size >= 50
        val hasMeasurementTracking = measurements.size >= 10
        val hasRatedWorkouts = workouts.any { it.rating != null }
        val hasFavoriteWorkouts = workouts.any { it.isFavorite }
        val hasTimeTracking = workouts.any { it.startTime != null && it.endTime != null }
        
        return hasWorkoutTracking && hasMeasurementTracking && hasRatedWorkouts && 
               hasFavoriteWorkouts && hasTimeTracking
    }

    private fun getComprehensiveTrackingDate(workouts: List<Workout>, measurements: List<BodyMeasurement>): Date? {
        if (!hasComprehensiveTracking(workouts, measurements)) return null
        val sortedWorkouts = workouts.sortedBy { it.date }
        return sortedWorkouts.getOrNull(49)?.date // 50th workout
    }

    private fun monthOfYear(date: Date): Int = Calendar.getInstance().apply { time = date }.get(Calendar.MONTH)
}


