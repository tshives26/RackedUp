package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.*
import com.chilluminati.rackedup.data.database.entity.*
import com.chilluminati.rackedup.presentation.programs.ProgramTemplatesSystem
import com.chilluminati.rackedup.presentation.programs.ProgramTemplate
import com.chilluminati.rackedup.presentation.programs.ProgramDayTemplate
import com.chilluminati.rackedup.presentation.programs.ProgramExerciseTemplate
import com.chilluminati.rackedup.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Utility class for seeding test data using existing workout templates
 * Generates realistic workout history for testing progress graphs and analytics
 */
@Singleton
class TestDataSeeder @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val exerciseDao: ExerciseDao,
    private val programDao: ProgramDao,
    private val programDayDao: ProgramDayDao,
    private val programExerciseDao: ProgramExerciseDao,
    private val personalRecordDao: PersonalRecordDao,
    private val bodyMeasurementDao: BodyMeasurementDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    
    companion object {
        private const val TEST_USER_NAME = "Test User"
        private const val TEST_USER_EMAIL = "test@rackedup.dev"
        private const val WEEKS_OF_DATA = 6
        private const val DAYS_PER_WEEK = 7
        private const val BASE_WEIGHT_SQUAT = 135.0
        private const val BASE_WEIGHT_BENCH = 115.0
        private const val BASE_WEIGHT_DEADLIFT = 185.0
        private const val BASE_WEIGHT_OVERHEAD = 85.0
        private const val BASE_WEIGHT_ROW = 95.0
    }
    
    /**
     * Generate comprehensive test data including user profile, workouts, and body measurements
     */
    suspend fun generateTestData(): Result<String> = withContext(ioDispatcher) {
        try {
            android.util.Log.i("TestDataSeeder", "Starting test data generation...")
            
            // Step 1: Create test user profile
            android.util.Log.i("TestDataSeeder", "Creating test user profile...")
            val testProfile = createTestUserProfile()
            val profileId = userProfileDao.insertProfile(testProfile)
            android.util.Log.i("TestDataSeeder", "Test user profile created with ID: $profileId")
            
            // Step 2: Generate workout history using existing templates
            android.util.Log.i("TestDataSeeder", "Generating workout history...")
            val workoutCount = generateWorkoutHistory(profileId)
            android.util.Log.i("TestDataSeeder", "Generated $workoutCount workouts")
            
            // Step 3: Generate body measurements
            android.util.Log.i("TestDataSeeder", "Generating body measurements...")
            val measurementCount = generateBodyMeasurements(profileId)
            android.util.Log.i("TestDataSeeder", "Generated $measurementCount body measurements")
            
            // Step 4: Generate personal records
            android.util.Log.i("TestDataSeeder", "Generating personal records...")
            val prCount = generatePersonalRecords()
            android.util.Log.i("TestDataSeeder", "Generated $prCount personal records")
            
            android.util.Log.i("TestDataSeeder", "Test data generation completed successfully")
            Result.success("Test data generated successfully: $workoutCount workouts, $measurementCount measurements, $prCount personal records")
        } catch (e: Exception) {
            android.util.Log.e("TestDataSeeder", "Error generating test data", e)
            Result.failure(e)
        }
    }
    
    /**
     * Remove all test data from the database
     */
    suspend fun clearTestData(): Result<String> = withContext(ioDispatcher) {
        try {
            // Find and remove test user profile
            val testProfile = userProfileDao.getAllProfiles().find { it.name == TEST_USER_NAME }
            testProfile?.let { profile ->
                // Remove all related data
                workoutDao.getAllWorkoutsList().filter { it.programId != null }.forEach { workout ->
                    workoutDao.deleteWorkout(workout)
                }
                userProfileDao.deleteProfile(profile)
            }
            
            Result.success("Test data cleared successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Create a realistic test user profile
     */
    private suspend fun createTestUserProfile(): UserProfile {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -25) // 25 years old
        
        return UserProfile(
            name = TEST_USER_NAME,
            email = TEST_USER_EMAIL,
            birthday = calendar.time,
            sex = "Male",
            age = 25,
            gender = "Male",
            heightCm = 175.0,
            weightKg = 75.0,
            activityLevel = "Moderately Active",
            fitnessGoal = "Gain Muscle",
            experienceLevel = "Intermediate",
            preferredWeightUnit = "lbs",
            preferredDistanceUnit = "miles",
            defaultRestTime = 120,
            timezone = "America/New_York",
            isActive = true,
            createdAt = Date(),
            updatedAt = Date()
        )
    }
    
    /**
     * Generate 6 weeks of realistic workout history using existing templates
     * Starting from the current week and going backwards
     */
    private suspend fun generateWorkoutHistory(profileId: Long): Int {
        val templates = ProgramTemplatesSystem.getAllTemplates()
        val calendar = Calendar.getInstance()
        // Start from current week (don't subtract weeks initially)
        
        android.util.Log.i("TestDataSeeder", "Starting workout generation for $WEEKS_OF_DATA weeks (current week backwards)")
        android.util.Log.i("TestDataSeeder", "Current date: ${calendar.time}")
        android.util.Log.i("TestDataSeeder", "Available templates: ${templates.map { it.name }}")
        
        var workoutCount = 0
        var currentWeek = 0
        
        while (currentWeek < WEEKS_OF_DATA) {
            // Rotate through different templates for variety
            val template = templates[currentWeek % templates.size]
            android.util.Log.i("TestDataSeeder", "Week $currentWeek: Using template '${template.name}'")
            
            // Generate workouts for this week based on template
            val weekWorkouts = generateWeekOfWorkouts(template, calendar.time, profileId)
            android.util.Log.i("TestDataSeeder", "Week $currentWeek: Generated ${weekWorkouts.size} workouts")
            
            // Insert workouts and related data
            weekWorkouts.forEach { workoutData ->
                val workoutId = workoutDao.insertWorkout(workoutData.workout)
                android.util.Log.d("TestDataSeeder", "Inserted workout: ${workoutData.workout.name} with ID: $workoutId")
                
                // Insert workout exercises
                workoutData.exercises.forEach { exerciseData ->
                    val workoutExerciseId = workoutExerciseDao.insertWorkoutExercise(
                        exerciseData.workoutExercise.copy(workoutId = workoutId)
                    )
                    
                    // Insert exercise sets
                    exerciseData.sets.forEach { setData ->
                        exerciseSetDao.insertExerciseSet(
                            setData.copy(workoutExerciseId = workoutExerciseId)
                        )
                    }
                }
                
                // Recalculate workout totals after all data is inserted
                workoutDao.recalculateWorkoutTotals(workoutId)
                
                workoutCount++
            }
            
            // Move to previous week (going backwards in time)
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
            currentWeek++
        }
        
        android.util.Log.i("TestDataSeeder", "Workout generation completed. Total workouts: $workoutCount")
        return workoutCount
    }
    
    /**
     * Generate a week of workouts based on a template
     */
    private suspend fun generateWeekOfWorkouts(
        template: ProgramTemplate,
        weekStart: Date,
        profileId: Long
    ): List<WorkoutData> {
        val workouts = mutableListOf<WorkoutData>()
        val calendar = Calendar.getInstance().apply { time = weekStart }
        
        // Generate workouts for each day in the template
        template.days.forEachIndexed { dayIndex, dayTemplate ->
            if (dayTemplate.exercises.isNotEmpty()) {
                // This is a workout day
                val workoutDate = calendar.time
                val workout = createWorkoutFromTemplate(template, dayTemplate, workoutDate, profileId)
                
                // Generate exercise data for this workout
                val exercises = generateExercisesForWorkout(dayTemplate, workout.id)
                
                workouts.add(WorkoutData(workout, exercises))
                
                // Move to next day
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            } else {
                // This is a rest day, just move to next day
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        
        return workouts
    }
    
    /**
     * Create a workout entity from a template day
     */
    private suspend fun createWorkoutFromTemplate(
        template: ProgramTemplate,
        dayTemplate: ProgramDayTemplate,
        date: Date,
        profileId: Long
    ): Workout {
        val startTime = generateRealisticStartTime(date)
        val duration = template.estimatedDuration + Random.nextInt(-15, 15) // Add some variation
        
        return Workout(
            name = "${template.name} - ${dayTemplate.name}",
            date = date,
            startTime = startTime,
            endTime = Date(startTime.time + (duration * 60 * 1000)),
            durationMinutes = duration,
            notes = generateWorkoutNotes(template, dayTemplate),
            totalVolume = 0.0, // Will be calculated after sets are added
            totalSets = dayTemplate.exercises.sumOf { parseSets(it.sets) },
            totalReps = 0, // Will be calculated after sets are added
            isCompleted = true,
            rating = Random.nextInt(3, 6), // 3-5 rating
            isFavorite = Random.nextBoolean(),
            createdAt = date,
            updatedAt = date
        )
    }
    
    /**
     * Generate realistic exercise data for a workout
     */
    private suspend fun generateExercisesForWorkout(
        dayTemplate: ProgramDayTemplate,
        workoutId: Long
    ): List<ExerciseData> {
        val exercises = mutableListOf<ExerciseData>()
        
        dayTemplate.exercises.forEachIndexed { index, exerciseTemplate ->
            // Find or create the exercise
            val exercise = findOrCreateExercise(exerciseTemplate.exerciseName)
            
            val workoutExercise = WorkoutExercise(
                workoutId = workoutId,
                exerciseId = exercise.id,
                orderIndex = index
            )
            
            // Generate sets for this exercise
            val sets = generateSetsForExercise(exerciseTemplate, exercise)
            
            exercises.add(ExerciseData(workoutExercise, sets))
        }
        
        return exercises
    }
    
    /**
     * Find existing exercise or create a basic one if not found
     */
    private suspend fun findOrCreateExercise(exerciseName: String): Exercise {
        var exercise = exerciseDao.getExerciseByName(exerciseName)
        
        if (exercise == null) {
            // Create a basic exercise if not found
            exercise = Exercise(
                name = exerciseName,
                category = determineExerciseCategory(exerciseName),
                equipment = determineExerciseEquipment(exerciseName),
                exerciseType = "Strength",
                difficultyLevel = "Intermediate",
                muscleGroups = listOf("General"),
                isCustom = true,
                createdAt = Date(),
                updatedAt = Date()
            )
            val exerciseId = exerciseDao.insertExercise(exercise)
            exercise = exercise.copy(id = exerciseId)
        }
        
        return exercise
    }
    
    /**
     * Generate realistic sets for an exercise based on template
     */
    private fun generateSetsForExercise(
        exerciseTemplate: ProgramExerciseTemplate,
        exercise: Exercise
    ): List<ExerciseSet> {
        val sets = mutableListOf<ExerciseSet>()
        val setCount = parseSets(exerciseTemplate.sets)
        val targetReps = parseReps(exerciseTemplate.reps)
        
        // Determine base weight for this exercise
        val baseWeight = getBaseWeightForExercise(exercise.name)
        var currentWeight = baseWeight
        
        // Generate progressive overload over the weeks
        val weekMultiplier = 1.0 + (Random.nextDouble() * 0.1) // 0-10% increase per week
        
        for (setIndex in 0 until setCount) {
            val reps = if (targetReps is IntRange) {
                Random.nextInt(targetReps.first, targetReps.last + 1)
            } else {
                targetReps.first
            }
            
            // Add some weight variation and progression
            val weightVariation = Random.nextDouble(-2.5, 2.5)
            val finalWeight = currentWeight + weightVariation
            
            val set = ExerciseSet(
                workoutExerciseId = 0, // Will be set when workout exercise is created
                setNumber = setIndex + 1,
                weight = finalWeight,
                reps = reps,
                restTimeSeconds = Random.nextInt(60, 180), // 1-3 minutes rest
                notes = generateSetNotes(exercise.name, finalWeight, reps),
                isCompleted = true,
                createdAt = Date()
            )
            
            sets.add(set)
            
            // Slight weight increase for next set (drop sets)
            currentWeight += Random.nextDouble(-5.0, 2.0)
        }
        
        return sets
    }
    
    /**
     * Generate body measurements over time
     * Starting from current week and going backwards
     */
    private suspend fun generateBodyMeasurements(profileId: Long): Int {
        val calendar = Calendar.getInstance()
        // Start from current week (don't subtract weeks initially)
        
        android.util.Log.i("TestDataSeeder", "Starting body measurements generation for $WEEKS_OF_DATA weeks (current week backwards)")
        android.util.Log.i("TestDataSeeder", "Current date: ${calendar.time}")
        
        val measurements = mutableListOf<BodyMeasurement>()
        var measurementCount = 0
        
        repeat(WEEKS_OF_DATA) { week ->
            val date = calendar.time
            
            // Weight measurement (slight weekly variation)
            val baseWeight = 75.0
            val weightVariation = Random.nextDouble(-0.5, 0.3) // Slight weight loss trend
            val currentWeight = baseWeight + (week * weightVariation)
            
            measurements.add(BodyMeasurement(
                measurementType = "Weight",
                value = currentWeight,
                unit = "kg",
                bodyPart = "Total",
                measurementMethod = "Scale",
                measuredAt = date,
                createdAt = date
            ))
            
            // Body measurements (chest, arms, waist)
            if (week % 2 == 0) { // Every other week
                measurements.add(BodyMeasurement(
                    measurementType = "Chest",
                    value = 95.0 + Random.nextDouble(-1.0, 1.0),
                    unit = "cm",
                    bodyPart = "Chest",
                    measurementMethod = "Tape",
                    measuredAt = date,
                    createdAt = date
                ))
                
                measurements.add(BodyMeasurement(
                    measurementType = "Arms",
                    value = 32.0 + Random.nextDouble(-0.5, 0.5),
                    unit = "cm",
                    bodyPart = "Arms",
                    measurementMethod = "Tape",
                    measuredAt = date,
                    createdAt = date
                ))
                
                measurements.add(BodyMeasurement(
                    measurementType = "Waist",
                    value = 80.0 + Random.nextDouble(-1.0, 1.0),
                    unit = "cm",
                    bodyPart = "Waist",
                    measurementMethod = "Tape",
                    measuredAt = date,
                    createdAt = date
                ))
                
                measurementCount += 4
            } else {
                measurementCount += 1
            }
            
            // Move to previous week (going backwards in time)
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
        }
        
        bodyMeasurementDao.insertBodyMeasurements(measurements)
        return measurementCount
    }
    
    /**
     * Generate personal records based on workout data
     */
    private suspend fun generatePersonalRecords(): Int {
        // This will be populated automatically by the existing PR system
        // based on the workout data we generated
        return 0 // Will be calculated by the system
    }
    
    // Helper functions
    
    private fun generateRealisticStartTime(date: Date): Date {
        val calendar = Calendar.getInstance().apply { time = date }
        
        // Generate realistic workout times (morning, afternoon, evening)
        val timeSlots = listOf(
            Triple(6, 0, 0),   // 6:00 AM
            Triple(12, 0, 0),  // 12:00 PM
            Triple(18, 0, 0)   // 6:00 PM
        )
        
        val (hour, minute, second) = timeSlots.random()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        
        return calendar.time
    }
    
    private fun generateWorkoutNotes(template: ProgramTemplate, dayTemplate: ProgramDayTemplate): String {
        val notes = mutableListOf<String>()
        
        if (Random.nextBoolean()) notes.add("Felt strong today")
        if (Random.nextBoolean()) notes.add("Good form on all sets")
        if (Random.nextBoolean()) notes.add("Progressive overload achieved")
        if (Random.nextBoolean()) notes.add("Following ${template.name} template")
        
        return notes.joinToString(". ")
    }
    
    private fun generateSetNotes(exerciseName: String, weight: Double, reps: Int): String? {
        return if (Random.nextBoolean()) {
            when {
                reps >= 12 -> "Light weight, high reps"
                reps >= 8 -> "Moderate weight, good form"
                reps >= 5 -> "Heavy weight, focus on form"
                else -> "Max effort set"
            }
        } else null
    }
    
    private fun parseSets(setsString: String): Int {
        return setsString.split("x").first().toIntOrNull() ?: 3
    }
    
    private fun parseReps(repsString: String?): IntRange {
        if (repsString == null) return 8..12
        
        return when {
            repsString.contains("-") -> {
                val parts = repsString.split("-")
                val first = parts.first().toIntOrNull() ?: 8
                val last = parts.last().toIntOrNull() ?: 12
                first..last
            }
            repsString.contains("per leg") -> {
                val reps = repsString.split(" ").first().toIntOrNull() ?: 10
                reps..reps
            }
            else -> {
                val reps = repsString.toIntOrNull() ?: 10
                reps..reps
            }
        }
    }
    
    private fun getBaseWeightForExercise(exerciseName: String): Double {
        return when {
            exerciseName.contains("Squat", ignoreCase = true) -> BASE_WEIGHT_SQUAT
            exerciseName.contains("Bench", ignoreCase = true) -> BASE_WEIGHT_BENCH
            exerciseName.contains("Deadlift", ignoreCase = true) -> BASE_WEIGHT_DEADLIFT
            exerciseName.contains("Overhead", ignoreCase = true) -> BASE_WEIGHT_OVERHEAD
            exerciseName.contains("Row", ignoreCase = true) -> BASE_WEIGHT_ROW
            exerciseName.contains("Push", ignoreCase = true) -> 45.0
            exerciseName.contains("Pull", ignoreCase = true) -> 45.0
            exerciseName.contains("Lunge", ignoreCase = true) -> 25.0
            exerciseName.contains("Curl", ignoreCase = true) -> 25.0
            else -> 45.0
        }
    }
    
    private fun determineExerciseCategory(exerciseName: String): String {
        return when {
            exerciseName.contains("Squat", ignoreCase = true) -> "Legs"
            exerciseName.contains("Deadlift", ignoreCase = true) -> "Legs"
            exerciseName.contains("Bench", ignoreCase = true) -> "Chest"
            exerciseName.contains("Press", ignoreCase = true) -> "Shoulders"
            exerciseName.contains("Row", ignoreCase = true) -> "Back"
            exerciseName.contains("Curl", ignoreCase = true) -> "Arms"
            exerciseName.contains("Push", ignoreCase = true) -> "Chest"
            exerciseName.contains("Pull", ignoreCase = true) -> "Back"
            exerciseName.contains("Lunge", ignoreCase = true) -> "Legs"
            else -> "General"
        }
    }
    
    private fun determineExerciseEquipment(exerciseName: String): String {
        return when {
            exerciseName.contains("Barbell", ignoreCase = true) -> "Barbell"
            exerciseName.contains("Dumbbell", ignoreCase = true) -> "Dumbbell"
            exerciseName.contains("Machine", ignoreCase = true) -> "Machine"
            exerciseName.contains("Push", ignoreCase = true) -> "Bodyweight"
            exerciseName.contains("Pull", ignoreCase = true) -> "Bodyweight"
            exerciseName.contains("Squat", ignoreCase = true) -> "Bodyweight"
            exerciseName.contains("Lunge", ignoreCase = true) -> "Bodyweight"
            else -> "Bodyweight"
        }
    }
    
    // Data classes for organizing workout generation
    
    private data class WorkoutData(
        val workout: Workout,
        val exercises: List<ExerciseData>
    )
    
    private data class ExerciseData(
        val workoutExercise: WorkoutExercise,
        val sets: List<ExerciseSet>
    )
}
