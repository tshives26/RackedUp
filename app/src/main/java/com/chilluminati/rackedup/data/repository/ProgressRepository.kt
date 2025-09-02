package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.WorkoutDao
import com.chilluminati.rackedup.data.database.dao.ExerciseSetDao
import com.chilluminati.rackedup.data.database.dao.ExerciseDao
import com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao
import com.chilluminati.rackedup.data.database.dao.PersonalRecordDao
import com.chilluminati.rackedup.data.database.entity.PersonalRecord
import com.chilluminati.rackedup.data.database.entity.BodyMeasurement
import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for progress tracking and analytics
 */
@Singleton
class ProgressRepository @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val exerciseDao: ExerciseDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val personalRecordDao: PersonalRecordDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    
    /**
     * Get workout volume over time
     */
    fun getWorkoutVolumeOverTime(days: Int = 30): Flow<List<VolumeDataPoint>> {
        return workoutDao.getAllWorkouts().map { workouts ->
            val cutoffDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -days)
            }.time
            
            val filteredWorkouts = workouts.filter { it.date.after(cutoffDate) }
            
            if (filteredWorkouts.isEmpty()) {
                emptyList()
            } else {
                filteredWorkouts.map { workout ->
                    VolumeDataPoint(
                        date = workout.date,
                        volume = workout.totalVolume
                    )
                }.sortedBy { it.date }
            }
        }
    }
    
    /**
     * Get exercise frequency over time
     */
    @Suppress("UNUSED_PARAMETER")
    fun getExerciseFrequency(exerciseId: Long, days: Int = 90): Flow<List<FrequencyDataPoint>> {
        // TODO: Implement exercise frequency tracking
        // This would require joining workout, workout_exercise, and exercise tables
        return workoutDao.getAllWorkouts().map { emptyList<FrequencyDataPoint>() }
    }
    
    /**
     * Get 1RM progression for an exercise
     */
    @Suppress("UNUSED_PARAMETER")
    suspend fun getOneRepMaxProgression(exerciseId: Long): List<OneRepMaxDataPoint> {
        return withContext(ioDispatcher) {
            // TODO: Calculate 1RM from exercise sets using Epley formula
            // 1RM = weight * (1 + reps/30)
            emptyList()
        }
    }
    
    /**
     * Calculate current 1RM for exercise
     */
    @Suppress("UNUSED_PARAMETER")
    suspend fun calculateCurrentOneRepMax(exerciseId: Long): Double? {
        return withContext(ioDispatcher) {
            // TODO: Get the highest weight lifted for this exercise and calculate 1RM
            null
        }
    }
    
    /**
     * Get personal records
     */
    suspend fun getPersonalRecords(): List<PersonalRecord> {
        return withContext(ioDispatcher) {
            personalRecordDao.getAllPersonalRecords()
        }
    }
    
    /**
     * Get volume-based personal records for all exercises
     * Returns the highest volume (weight × reps) for each exercise
     */
    suspend fun getVolumeBasedPersonalRecords(): List<VolumeBasedPersonalRecord> {
        return withContext(ioDispatcher) {
            // Get only the best volume personal record for each exercise
            val personalRecords = personalRecordDao.getBestVolumePersonalRecords()
            
            val exercises = exerciseDao.getAllExercisesList()
            val exerciseMap = exercises.associateBy { it.id }
            
            personalRecords.mapNotNull { pr ->
                val exercise = exerciseMap[pr.exerciseId]
                if (exercise != null && pr.volume != null && pr.weight != null && pr.reps != null) {
                    VolumeBasedPersonalRecord(
                        exerciseId = pr.exerciseId,
                        exerciseName = exercise.name,
                        exerciseCategory = exercise.category,
                        equipment = exercise.equipment,
                        weight = pr.weight,
                        reps = pr.reps,
                        volume = pr.volume,
                        workoutId = pr.workoutId ?: 0,
                        achievedAt = pr.achievedAt
                    )
                } else null
            }
        }
    }
    
    /**
     * Get volume-based personal records grouped by category
     */
    suspend fun getVolumeBasedPersonalRecordsByCategory(): Map<String, List<VolumeBasedPersonalRecord>> {
        val records = getVolumeBasedPersonalRecords()
        return records.groupBy { record -> record.exerciseCategory }
    }

    /**
     * Clean up duplicate volume personal records in the database
     * This should be called once to fix existing duplicate data
     */
    suspend fun cleanupDuplicateVolumeRecords() {
        withContext(ioDispatcher) {
            personalRecordDao.cleanupDuplicateVolumeRecords()
        }
    }
    
    /**
     * Initialize volume-based personal records from existing workout data
     * This should be called once to populate PRs from historical data
     */
    suspend fun initializeVolumePRsFromHistory() {
        withContext(ioDispatcher) {
            // Get exercise sets, workout exercises, and exercises for volume PR calculation
            val exerciseSets = exerciseSetDao.getAllExerciseSets()
            val workoutExercises = workoutExerciseDao.getAllWorkoutExercises()
            val exercises = exerciseDao.getAllExercisesList()
            
            // Create lookup maps
            val exerciseMap = exercises.associateBy { it.id }
            val workoutExerciseMap = workoutExercises.associateBy { it.id }
            
            // Group sets by exercise and find the highest volume set for each
            val exerciseVolumeMap = mutableMapOf<Long, PersonalRecord>()
            
            for (set in exerciseSets) {
                val workoutExercise = workoutExerciseMap[set.workoutExerciseId]
                if (workoutExercise != null && set.weight != null && set.reps != null && set.reps > 0) {
                    val exerciseId = workoutExercise.exerciseId
                    val volume = set.weight * set.reps
                    
                    val currentRecord = exerciseVolumeMap[exerciseId]
                    if (currentRecord == null || volume > (currentRecord.volume ?: 0.0)) {
                        val exercise = exerciseMap[exerciseId]
                        if (exercise != null) {
                            exerciseVolumeMap[exerciseId] = PersonalRecord(
                                exerciseId = exerciseId,
                                recordType = "Volume",
                                weight = set.weight,
                                reps = set.reps,
                                volume = volume,
                                workoutId = workoutExercise.workoutId,
                                achievedAt = set.createdAt
                            )
                        }
                    }
                }
            }
            
            // Insert all PRs
            if (exerciseVolumeMap.isNotEmpty()) {
                personalRecordDao.insertPersonalRecords(exerciseVolumeMap.values.toList())
            }
        }
    }
    
    /**
     * Check and update volume-based personal records for a completed workout
     * This should be called when a workout is completed to check for new PRs
     */
    suspend fun checkAndUpdateVolumePRs(workoutId: Long) {
        withContext(ioDispatcher) {
            // Get all exercise sets for this workout
            val workoutExercises = workoutExerciseDao.getWorkoutExercises(workoutId).first()
            val exerciseSets = mutableListOf<ExerciseSet>()
            
            workoutExercises.forEach { workoutExercise ->
                val sets = exerciseSetDao.getSetsByWorkoutExerciseId(workoutExercise.id)
                exerciseSets.addAll(sets)
            }
            
            val exercises = exerciseDao.getAllExercisesList()
            val exerciseMap = exercises.associateBy { it.id }
            
            // Check each set for potential new PRs
            for (set in exerciseSets) {
                if (set.weight != null && set.reps != null && set.reps > 0) {
                    val workoutExercise = workoutExercises.find { it.id == set.workoutExerciseId }
                    if (workoutExercise != null) {
                        val exerciseId = workoutExercise.exerciseId
                        val volume = set.weight * set.reps
                        
                        // Check if this is a new PR
                        val existingPRs = personalRecordDao.getPersonalRecordsSync(exerciseId)
                        val existingPR = existingPRs.find { it.recordType == "Volume" }
                        
                        if (existingPR == null || volume > (existingPR.volume ?: 0.0)) {
                            val exercise = exerciseMap[exerciseId]
                            if (exercise != null) {
                                val newPR = PersonalRecord(
                                    exerciseId = exerciseId,
                                    recordType = "Volume",
                                    weight = set.weight,
                                    reps = set.reps,
                                    volume = volume,
                                    workoutId = workoutId,
                                    achievedAt = set.createdAt,
                                    previousValue = existingPR?.volume,
                                    improvement = if (existingPR != null && existingPR.volume != null) {
                                        ((volume - existingPR.volume) / existingPR.volume) * 100
                                    } else null
                                )
                                
                                personalRecordDao.insertPersonalRecord(newPR)
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Get body measurements over time
     */
    suspend fun getBodyMeasurements(): List<BodyMeasurement> {
        return withContext(ioDispatcher) {
            // No mock data; return empty until measurements are added
            emptyList()
        }
    }
    
    /**
     * Add body measurement
     */
    @Suppress("UNUSED_PARAMETER")
    suspend fun addBodyMeasurement(
        measurementType: String,
        value: Double,
        date: Date = Date(),
        unit: String
    ) {
        withContext(ioDispatcher) {
            // TODO: Implement body measurement insertion
        }
    }
    
    /**
     * Get workout consistency (workouts per week over time)
     */
    fun getWorkoutConsistency(weeks: Int = 12): Flow<List<ConsistencyDataPoint>> {
        return workoutDao.getAllWorkouts().map { workouts ->
            val calendar = Calendar.getInstance()
            val dataPoints = mutableListOf<ConsistencyDataPoint>()
            
            repeat(weeks) { weekOffset ->
                calendar.time = Date()
                calendar.add(Calendar.WEEK_OF_YEAR, -weekOffset)
                
            val weekStart = calendar.apply {
                    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                }.time
                
            val weekEnd = calendar.apply {
                    add(Calendar.DAY_OF_WEEK, 6)
                }.time
                
                val workoutCount = workouts.count { workout ->
                    workout.date.after(weekStart) && workout.date.before(weekEnd)
                }
                
                dataPoints.add(
                    ConsistencyDataPoint(
                        weekStart = weekStart,
                        workoutCount = workoutCount
                    )
                )
            }
            
            dataPoints.reversed() // Show oldest to newest
        }
    }
    
    /**
     * Get weekly workout summary
     */
    suspend fun getWeeklyWorkoutSummary(): WeeklyWorkoutSummary {
        return withContext(ioDispatcher) {
            // Return sample data for now
            WeeklyWorkoutSummary(
                workoutCount = 3,
                totalVolume = 15000.0,
                totalSets = 24,
                totalDuration = 180,
                avgDuration = 60
            )
        }
    }
    
    /**
     * Get monthly workout summary
     */
    suspend fun getMonthlyWorkoutSummary(): MonthlyWorkoutSummary {
        return withContext(ioDispatcher) {
            // Return sample data for now
            MonthlyWorkoutSummary(
                workoutCount = 12,
                totalVolume = 60000.0,
                totalSets = 96,
                totalDuration = 720,
                avgDuration = 60,
                uniqueExercises = 8
            )
        }
    }
    
    /**
     * Get universal strength metrics (relative strength and volume load)
     */
    fun getUniversalStrengthMetrics(days: Int = 30): Flow<List<UniversalStrengthDataPoint>> {
        return workoutDao.getAllWorkouts().map { workouts ->
            val cutoffDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -days)
            }.time
            
            val filteredWorkouts = workouts.filter { it.date.after(cutoffDate) }
            
            if (filteredWorkouts.isEmpty()) {
                emptyList()
            } else {
                filteredWorkouts.map { workout ->
                    // Calculate relative strength (volume as % of body weight)
                    // For now, using a placeholder body weight of 70kg
                    val bodyWeight = 70.0 // TODO: Get from user profile
                    val relativeStrength = if (bodyWeight > 0) (workout.totalVolume / bodyWeight) * 100 else 0.0
                    
                    UniversalStrengthDataPoint(
                        date = workout.date,
                        relativeStrength = relativeStrength,
                        volumeLoad = workout.totalVolume
                    )
                }.sortedBy { it.date }
            }
        }
    }
    
    /**
     * Get workout density metrics (volume per minute, sets per minute)
     */
    fun getWorkoutDensityMetrics(days: Int = 30): Flow<List<WorkoutDensityDataPoint>> {
        return workoutDao.getAllWorkouts().map { workouts ->
            val cutoffDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -days)
            }.time
            
            val filteredWorkouts = workouts.filter { 
                it.date.after(cutoffDate) && it.durationMinutes != null && it.durationMinutes > 0 
            }
            
            if (filteredWorkouts.isEmpty()) {
                emptyList()
            } else {
                filteredWorkouts.map { workout ->
                    val durationMinutes = workout.durationMinutes?.toDouble() ?: 1.0
                    val volumePerMinute = workout.totalVolume / durationMinutes
                    val setsPerMinute = workout.totalSets.toDouble() / durationMinutes
                    
                    WorkoutDensityDataPoint(
                        date = workout.date,
                        volumePerMinute = volumePerMinute,
                        setsPerMinute = setsPerMinute
                    )
                }.sortedBy { it.date }
            }
        }
    }
    
    /**
     * Get progression metrics (improvement percentages)
     */
    fun getProgressionMetrics(days: Int = 30): Flow<List<ProgressionDataPoint>> {
        return workoutDao.getAllWorkouts().map { workouts ->
            val cutoffDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -days)
            }.time
            
            val filteredWorkouts = workouts.filter { it.date.after(cutoffDate) }
                .sortedBy { it.date }
            
            if (filteredWorkouts.size < 2) {
                emptyList()
            } else {
                val progressionPoints = mutableListOf<ProgressionDataPoint>()
                
                for (i in 1 until filteredWorkouts.size) {
                    val current = filteredWorkouts[i]
                    val previous = filteredWorkouts[i - 1]
                    
                    val volumeImprovement = if (previous.totalVolume > 0) {
                        ((current.totalVolume - previous.totalVolume) / previous.totalVolume) * 100
                    } else 0.0
                    
                    val weeklyProgress = if (i >= 7) {
                        val weekAgo = filteredWorkouts.getOrNull(i - 7)
                        if (weekAgo != null && weekAgo.totalVolume > 0) {
                            ((current.totalVolume - weekAgo.totalVolume) / weekAgo.totalVolume) * 100
                        } else 0.0
                    } else 0.0
                    
                    progressionPoints.add(
                        ProgressionDataPoint(
                            date = current.date,
                            improvementPercentage = volumeImprovement,
                            weeklyProgressRate = weeklyProgress
                        )
                    )
                }
                
                progressionPoints
            }
        }
    }
    
    /**
     * Get exercise variety metrics
     */
    fun getExerciseVarietyMetrics(days: Int = 30): Flow<List<ExerciseVarietyDataPoint>> {
        return workoutDao.getAllWorkouts().map { workouts ->
            val cutoffDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -days)
            }.time
            
            val filteredWorkouts = workouts.filter { it.date.after(cutoffDate) }
            
            if (filteredWorkouts.isEmpty()) {
                emptyList()
            } else {
                // TODO: This would need to join with workout_exercise and exercise tables
                // For now, using placeholder data based on workout complexity
                filteredWorkouts.map { workout ->
                    val estimatedExercises = (workout.totalSets / 3).coerceAtLeast(1).coerceAtMost(8)
                    val estimatedMuscleGroups = (estimatedExercises / 2).coerceAtLeast(1).coerceAtMost(6)
                    
                    ExerciseVarietyDataPoint(
                        date = workout.date,
                        uniqueExercises = estimatedExercises,
                        muscleGroups = estimatedMuscleGroups
                    )
                }.sortedBy { it.date }
            }
        }
    }
}

/**
 * Data classes for progress tracking
 */
data class VolumeDataPoint(
    val date: Date,
    val volume: Double
)

data class FrequencyDataPoint(
    val date: Date,
    val frequency: Int
)

data class OneRepMaxDataPoint(
    val date: Date,
    val oneRepMax: Double,
    val actualWeight: Double,
    val reps: Int
)

data class ConsistencyDataPoint(
    val weekStart: Date,
    val workoutCount: Int
)

data class WeeklyWorkoutSummary(
    val workoutCount: Int,
    val totalVolume: Double,
    val totalSets: Int,
    val totalDuration: Int, // in minutes
    val avgDuration: Int // in minutes
)

data class MonthlyWorkoutSummary(
    val workoutCount: Int,
    val totalVolume: Double,
    val totalSets: Int,
    val totalDuration: Int, // in minutes
    val avgDuration: Int, // in minutes
    val uniqueExercises: Int
)

/**
 * Universal strength metrics data point
 */
data class UniversalStrengthDataPoint(
    val date: Date,
    val relativeStrength: Double, // Volume as % of body weight
    val volumeLoad: Double // Total volume in kg
)

/**
 * Workout density metrics data point
 */
data class WorkoutDensityDataPoint(
    val date: Date,
    val volumePerMinute: Double, // Volume per minute of workout
    val setsPerMinute: Double // Sets per minute of workout
)

/**
 * Progression metrics data point
 */
data class ProgressionDataPoint(
    val date: Date,
    val improvementPercentage: Double, // Improvement from previous workout
    val weeklyProgressRate: Double // Weekly progress rate
)

/**
 * Exercise variety metrics data point
 */
data class ExerciseVarietyDataPoint(
    val date: Date,
    val uniqueExercises: Int, // Number of unique exercises
    val muscleGroups: Int // Number of muscle groups targeted
)

/**
 * Volume-based personal record for any exercise
 */
data class VolumeBasedPersonalRecord(
    val exerciseId: Long,
    val exerciseName: String,
    val exerciseCategory: String,
    val equipment: String,
    val weight: Double,
    val reps: Int,
    val volume: Double, // weight × reps
    val workoutId: Long,
    val achievedAt: Date
)
