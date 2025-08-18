package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.PersonalRecordDao
import com.chilluminati.rackedup.data.database.dao.ProgramDao
import com.chilluminati.rackedup.data.database.dao.WorkoutDao
import com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao
import com.chilluminati.rackedup.data.database.dao.ExerciseDao
import com.chilluminati.rackedup.data.database.dao.BodyMeasurementDao
import com.chilluminati.rackedup.data.database.dao.UserProfileDao
import com.chilluminati.rackedup.data.database.entity.Workout
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
    private val exerciseDao: ExerciseDao,
    private val bodyMeasurementDao: BodyMeasurementDao,
    private val userProfileDao: UserProfileDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    enum class Category { Milestones, Consistency, Volume, Sets, Duration, Strength, TimeOfDay, Programs }

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
        // Recompute when any of workouts, PRs, or programs change
        return combine(
            workoutDao.getAllWorkouts(),
            personalRecordDao.getAllPersonalRecordsFlow(),
            programDao.getAllPrograms()
        ) { workouts, prs, programs ->
            computeStates(workouts, prs.map { it.achievedAt }, programs)
        }
    }

    private fun computeStates(
        workouts: List<Workout>,
        prDates: List<Date>,
        programs: List<Program>
    ): List<State> {
        val workoutsSorted = workouts.sortedBy { it.date }
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

        // Volume
        Definition("volume_10k", "Volume Up", "Accumulate 10k lbs of volume", Category.Volume, 10),
        Definition("volume_100k", "Volume Monster", "Accumulate 100k lbs of volume", Category.Volume, 20),
        Definition("volume_1m", "Million Mover", "Accumulate 1,000,000 lbs of volume", Category.Volume, 40),

        // Sets
        Definition("sets_100", "Set Centurion", "Log 100 total sets", Category.Sets, 10),
        Definition("sets_500", "Set General", "Log 500 total sets", Category.Sets, 20),
        Definition("sets_1000", "Set Legend", "Log 1,000 total sets", Category.Sets, 40),

        // Duration
        Definition("duration_500", "Time Under Tension", "Train 500 total minutes", Category.Duration, 15),
        Definition("duration_2000", "Gym Resident", "Train 2,000 total minutes", Category.Duration, 40),

        // Strength / PRs
        Definition("first_pr", "PR Hunter", "Set any personal record", Category.Strength, 20),

        // Time of day
        Definition("early_bird", "Early Bird", "Workout before 6 AM", Category.TimeOfDay, 15),
        Definition("night_owl", "Night Owl", "Workout after 10 PM", Category.TimeOfDay, 15),

        // Programs (remove finisher achievements per spec)
        Definition("program_starter", "Program Starter", "Create or start a program", Category.Programs, 10),

        // Extended Milestones & Totals
        Definition("workout_750", "Seven-Fifty", "Complete 750 workouts", Category.Milestones, 80),
        Definition("workout_1000", "Millennium", "Complete 1000 workouts", Category.Milestones, 100),
        Definition("single_volume_10k", "Ten-K Blast", "10k volume in one workout", Category.Volume, 15),
        Definition("single_volume_20k", "Twenty-K Blast", "20k volume in one workout", Category.Volume, 25),
        Definition("single_volume_40k", "Forty-K Blast", "40k volume in one workout", Category.Volume, 35),
        Definition("single_sets_30", "Set Marathon", "30 sets in one workout", Category.Sets, 15),
        Definition("single_sets_50", "Set Ultra", "50 sets in one workout", Category.Sets, 30),
        Definition("single_duration_90", "Long Haul", "90 minute workout", Category.Duration, 15),
        Definition("single_duration_120", "Marathon Session", "120 minute workout", Category.Duration, 30),

        // Consistency extended
        Definition("week_all7", "All Week", "Train all 7 days in a week", Category.Consistency, 25),
        Definition("year_weeks", "No-Miss Year", "Workout every week for a year", Category.Consistency, 60),

        // Logging/quality
        Definition("first_rated", "First Rating", "Rate a workout", Category.Milestones, 5),
        Definition("ten_rated", "Rater", "Rate 10 workouts", Category.Milestones, 15),
        Definition("first_favorite", "Favorite", "Mark a workout favorite", Category.Milestones, 5),
        Definition("ten_favorite", "Collector", "Mark 10 workouts as favorites", Category.Milestones, 15)
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
}


