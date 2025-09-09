package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.WorkoutDao
import com.chilluminati.rackedup.data.database.dao.ExerciseSetDao
import com.chilluminati.rackedup.data.database.dao.ExerciseDao
import com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao
import com.chilluminati.rackedup.data.database.dao.PersonalRecordDao
import com.chilluminati.rackedup.data.database.entity.PersonalRecord
import com.chilluminati.rackedup.data.database.entity.BodyMeasurement
import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.data.database.entity.Exercise
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
     * Get personal records achieved in a specific workout
     */
    suspend fun getPersonalRecordsForWorkout(workoutId: Long): List<PersonalRecord> {
        return withContext(ioDispatcher) {
            personalRecordDao.getPersonalRecordsForWorkout(workoutId)
        }
    }
    
    /**
     * Get the best personal records achieved in a specific workout (one per exercise per record type)
     * This ensures we only count unique PRs per exercise, not multiple PRs for the same exercise
     */
    suspend fun getBestPersonalRecordsForWorkout(workoutId: Long): List<PersonalRecord> {
        return withContext(ioDispatcher) {
            personalRecordDao.getBestPersonalRecordsForWorkout(workoutId)
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
     * Initialize all types of personal records from historical workout data
     * This is a comprehensive one-time operation to populate all PRs for existing workouts
     */
    suspend fun initializeAllPRsFromHistory() {
        withContext(ioDispatcher) {
            // Get all completed workouts ordered by date
            val workouts = workoutDao.getAllWorkoutsList()
                .filter { it.isCompleted }
                .sortedBy { it.date }
            
            // Process each workout chronologically to build PR history
            for (workout in workouts) {
                checkAndUpdatePersonalRecords(workout.id)
            }
        }
    }
    
    /**
     * Initialize volume-based personal records from existing workout data
     * This should be called once to populate PRs from historical data
     * @deprecated Use initializeAllPRsFromHistory() instead for comprehensive PR tracking
     */
    @Deprecated("Use initializeAllPRsFromHistory() instead for comprehensive PR tracking")
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
     * Check and update all types of personal records for a completed workout
     * This should be called when a workout is completed to check for new PRs
     */
    suspend fun checkAndUpdatePersonalRecords(workoutId: Long) {
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
            
            // Group sets by exercise to avoid duplicate PRs for the same exercise
            val setsByExercise = exerciseSets
                .filter { it.isCompleted }
                .groupBy { set ->
                    val workoutExercise = workoutExercises.find { it.id == set.workoutExerciseId }
                    workoutExercise?.exerciseId
                }
                .filterKeys { it != null }
                .mapKeys { it.key!! }
            
            // Check each exercise for potential new PRs (only the best performance per exercise)
            for ((exerciseId, sets) in setsByExercise) {
                val exercise = exerciseMap[exerciseId]
                if (exercise != null) {
                    val existingPRs = personalRecordDao.getPersonalRecordsSync(exerciseId)
                    
                    when (exercise.exerciseType) {
                        "Strength" -> {
                            checkStrengthPRsForExercise(sets, exercise, existingPRs, workoutId)
                        }
                        "Cardio" -> {
                            checkCardioPRsForExercise(sets, exercise, existingPRs, workoutId)
                        }
                        "Isometric" -> {
                            checkIsometricPRsForExercise(sets, exercise, existingPRs, workoutId)
                        }
                        else -> {
                            // Default to strength-based tracking for unknown types
                            checkStrengthPRsForExercise(sets, exercise, existingPRs, workoutId)
                        }
                    }
                }
            }
            
            // Clean up any duplicate PRs that might have been created to ensure only 1 PR per exercise
            cleanupDuplicatePRsForWorkout(workoutId)
        }
    }
    
    /**
     * Check for strength-based personal records for an exercise
     * Only creates ONE PR per exercise per workout (the most significant one)
     */
    private suspend fun checkStrengthPRsForExercise(
        sets: List<ExerciseSet>, 
        exercise: Exercise, 
        existingPRs: List<PersonalRecord>, 
        workoutId: Long
    ) {
        val validSets = sets.filter { it.weight != null && it.reps != null && it.reps > 0 }
        if (validSets.isEmpty()) return
        
        // Find the best performance for each PR type
        val bestVolumeSet = validSets.maxByOrNull { it.weight!! * it.reps!! }
        val bestMaxWeightSet = validSets.maxByOrNull { it.weight!! }
        val best1RMSet = validSets.maxByOrNull { calculateOneRepMax(it.weight!!, it.reps!!, it.rpe) }
        
        // Check each PR type and determine which ones are actually new PRs
        val potentialPRs = mutableListOf<PersonalRecord>()
        
        // 1. Volume PR check
        bestVolumeSet?.let { set ->
            val volume = set.weight!! * set.reps!!
            val existingVolumePR = existingPRs.find { it.recordType == "Volume" }
            
            if (existingVolumePR == null || volume > (existingVolumePR.volume ?: 0.0)) {
                potentialPRs.add(PersonalRecord(
                    exerciseId = exercise.id,
                    recordType = "Volume",
                    weight = set.weight,
                    reps = set.reps,
                    volume = volume,
                    workoutId = workoutId,
                    achievedAt = set.createdAt,
                    previousValue = existingVolumePR?.volume,
                    improvement = if (existingVolumePR?.volume != null) {
                        ((volume - existingVolumePR.volume) / existingVolumePR.volume) * 100
                    } else null
                ))
            }
        }
        
        // 2. Max Weight PR check
        bestMaxWeightSet?.let { set ->
            val existingMaxWeightPR = existingPRs.find { it.recordType == "Max Weight" }
            
            if (existingMaxWeightPR == null || set.weight!! > (existingMaxWeightPR.weight ?: 0.0)) {
                potentialPRs.add(PersonalRecord(
                    exerciseId = exercise.id,
                    recordType = "Max Weight",
                    weight = set.weight,
                    reps = set.reps,
                    workoutId = workoutId,
                    achievedAt = set.createdAt,
                    previousValue = existingMaxWeightPR?.weight,
                    improvement = if (existingMaxWeightPR?.weight != null) {
                        ((set.weight!! - existingMaxWeightPR.weight) / existingMaxWeightPR.weight) * 100
                    } else null
                ))
            }
        }
        
        // 3. Estimated 1RM PR check
        best1RMSet?.let { set ->
            val estimated1RM = calculateOneRepMax(set.weight!!, set.reps!!, set.rpe)
            val existing1RMPR = existingPRs.find { it.recordType == "1RM" }
            
            if (existing1RMPR == null || estimated1RM > (existing1RMPR.estimated1RM ?: 0.0)) {
                potentialPRs.add(PersonalRecord(
                    exerciseId = exercise.id,
                    recordType = "1RM",
                    weight = set.weight,
                    reps = set.reps,
                    estimated1RM = estimated1RM,
                    workoutId = workoutId,
                    achievedAt = set.createdAt,
                    previousValue = existing1RMPR?.estimated1RM,
                    improvement = if (existing1RMPR?.estimated1RM != null) {
                        ((estimated1RM - existing1RMPR.estimated1RM) / existing1RMPR.estimated1RM) * 100
                    } else null
                ))
            }
        }
        
        // Only create ONE PR per exercise per workout - choose the most significant one
        if (potentialPRs.isNotEmpty()) {
            // Always prioritize Volume PR if it exists, otherwise Max Weight, otherwise 1RM
            val bestPR = potentialPRs.find { it.recordType == "Volume" }
                ?: potentialPRs.find { it.recordType == "Max Weight" }
                ?: potentialPRs.firstOrNull()
            
            bestPR?.let { personalRecordDao.insertPersonalRecord(it) }
        }
    }
    
    /**
     * Check for cardio-based personal records (Distance, Duration, Speed) for an exercise
     * Only creates ONE PR per exercise per workout (the most significant one)
     */
    private suspend fun checkCardioPRsForExercise(
        sets: List<ExerciseSet>, 
        exercise: Exercise, 
        existingPRs: List<PersonalRecord>, 
        workoutId: Long
    ) {
        val potentialPRs = mutableListOf<PersonalRecord>()
        
        // Find the best performance for each PR type
        val bestDistanceSet = sets.filter { it.distance != null && it.distance > 0 }
            .maxByOrNull { it.distance!! }
        val bestDurationSet = sets.filter { it.durationSeconds != null && it.durationSeconds > 0 }
            .maxByOrNull { it.durationSeconds!! }
        val bestSpeedSet = sets.filter { 
            it.distance != null && it.durationSeconds != null && 
            it.distance > 0 && it.durationSeconds > 0 
        }.maxByOrNull { it.distance!! / (it.durationSeconds!! / 3600.0) }
        
        // 1. Distance PR - only if this is the best distance set
        bestDistanceSet?.let { set ->
            val existingDistancePR = existingPRs.find { it.recordType == "Distance" }
            
            if (existingDistancePR == null || set.distance!! > (existingDistancePR.distance ?: 0.0)) {
                potentialPRs.add(PersonalRecord(
                    exerciseId = exercise.id,
                    recordType = "Distance",
                    distance = set.distance,
                    durationSeconds = set.durationSeconds,
                    workoutId = workoutId,
                    achievedAt = set.createdAt,
                    previousValue = existingDistancePR?.distance,
                    improvement = if (existingDistancePR?.distance != null) {
                        ((set.distance!! - existingDistancePR.distance) / existingDistancePR.distance) * 100
                    } else null
                ))
            }
        }
        
        // 2. Duration PR - only if this is the best duration set
        bestDurationSet?.let { set ->
            val existingDurationPR = existingPRs.find { it.recordType == "Duration" }
            
            if (existingDurationPR == null || set.durationSeconds!! > (existingDurationPR.durationSeconds ?: 0)) {
                potentialPRs.add(PersonalRecord(
                    exerciseId = exercise.id,
                    recordType = "Duration",
                    durationSeconds = set.durationSeconds,
                    distance = set.distance,
                    workoutId = workoutId,
                    achievedAt = set.createdAt,
                    previousValue = existingDurationPR?.durationSeconds?.toDouble(),
                    improvement = if (existingDurationPR?.durationSeconds != null) {
                        ((set.durationSeconds!! - existingDurationPR.durationSeconds).toDouble() / existingDurationPR.durationSeconds) * 100
                    } else null
                ))
            }
        }
        
        // 3. Speed PR - only if this is the best speed set
        bestSpeedSet?.let { set ->
            val speed = set.distance!! / (set.durationSeconds!! / 3600.0) // km/h or mph
            val existingSpeedPR = existingPRs.find { it.recordType == "Speed" }
            val existingSpeed = if (existingSpeedPR?.distance != null && existingSpeedPR.durationSeconds != null) {
                existingSpeedPR.distance / (existingSpeedPR.durationSeconds / 3600.0)
            } else null
            
            if (existingSpeed == null || speed > existingSpeed) {
                potentialPRs.add(PersonalRecord(
                    exerciseId = exercise.id,
                    recordType = "Speed",
                    distance = set.distance,
                    durationSeconds = set.durationSeconds,
                    workoutId = workoutId,
                    achievedAt = set.createdAt,
                    previousValue = existingSpeed,
                    improvement = if (existingSpeed != null) {
                        ((speed - existingSpeed) / existingSpeed) * 100
                    } else null
                ))
            }
        }
        
        // Only create ONE PR per exercise per workout - choose the most significant one
        if (potentialPRs.isNotEmpty()) {
            // Priority: Distance > Duration > Speed (Distance is most meaningful for cardio)
            val bestPR = potentialPRs.find { it.recordType == "Distance" }
                ?: potentialPRs.find { it.recordType == "Duration" }
                ?: potentialPRs.first()
            
            personalRecordDao.insertPersonalRecord(bestPR)
        }
    }
    
    /**
     * Check for isometric-based personal records (Duration) for an exercise
     * Only creates ONE PR per exercise per workout
     */
    private suspend fun checkIsometricPRsForExercise(
        sets: List<ExerciseSet>, 
        exercise: Exercise, 
        existingPRs: List<PersonalRecord>, 
        workoutId: Long
    ) {
        // Find the best duration set for isometric exercises
        val bestDurationSet = sets.filter { it.durationSeconds != null && it.durationSeconds > 0 }
            .maxByOrNull { it.durationSeconds!! }
        
        bestDurationSet?.let { set ->
            val existingDurationPR = existingPRs.find { it.recordType == "Duration" }
            
            if (existingDurationPR == null || set.durationSeconds!! > (existingDurationPR.durationSeconds ?: 0)) {
                val newPR = PersonalRecord(
                    exerciseId = exercise.id,
                    recordType = "Duration",
                    durationSeconds = set.durationSeconds,
                    workoutId = workoutId,
                    achievedAt = set.createdAt,
                    previousValue = existingDurationPR?.durationSeconds?.toDouble(),
                    improvement = if (existingDurationPR?.durationSeconds != null) {
                        ((set.durationSeconds!! - existingDurationPR.durationSeconds).toDouble() / existingDurationPR.durationSeconds) * 100
                    } else null
                )
                
                personalRecordDao.insertPersonalRecord(newPR)
            }
        }
    }
    
    /**
     * Calculate estimated 1RM using appropriate formula
     */
    private fun calculateOneRepMax(weight: Double, reps: Int, rpe: Int? = null): Double {
        return when {
            reps == 1 -> weight
            rpe != null -> calculateOneRepMaxWithRPE(weight, reps, rpe)
            else -> calculateOneRepMaxEpley(weight, reps)
        }
    }
    
    /**
     * Epley formula for 1RM estimation
     */
    private fun calculateOneRepMaxEpley(weight: Double, reps: Int): Double {
        if (reps <= 0) return weight
        return weight * (1 + reps / 30.0)
    }
    
    /**
     * RPE-based 1RM estimation
     */
    private fun calculateOneRepMaxWithRPE(weight: Double, reps: Int, rpe: Int): Double {
        if (reps <= 0 || rpe <= 0) return weight
        
        // RPE to percentage of 1RM conversion table
        val rpeToPercentage = mapOf(
            10 to 100, 9 to 97, 8 to 93, 7 to 88, 6 to 83,
            5 to 77, 4 to 70, 3 to 62, 2 to 52, 1 to 40
        )
        
        val percentage = rpeToPercentage[rpe] ?: return calculateOneRepMaxEpley(weight, reps)
        return (weight * 100) / percentage
    }
    
    /**
     * Legacy method for backward compatibility - now calls the comprehensive method
     */
    suspend fun checkAndUpdateVolumePRs(workoutId: Long) {
        checkAndUpdatePersonalRecords(workoutId)
    }
    
    /**
     * Clean up duplicate PRs for a specific workout to ensure only 1 PR per exercise
     * This removes multiple PRs for the same exercise in the same workout and keeps only the best one
     */
    suspend fun cleanupDuplicatePRsForWorkout(workoutId: Long) {
        withContext(ioDispatcher) {
            val workoutPRs = personalRecordDao.getPersonalRecordsForWorkout(workoutId)
            
            // Group PRs by exercise (not by type) - we want only 1 PR per exercise total
            val prsByExercise = workoutPRs.groupBy { it.exerciseId }
            
            for ((exerciseId, prs) in prsByExercise) {
                if (prs.size > 1) {
                    // Keep only the best PR for this exercise - prioritize Volume > Max Weight > 1RM > Distance > Duration > Speed
                    val bestPR = prs.find { it.recordType == "Volume" }
                        ?: prs.find { it.recordType == "Max Weight" }
                        ?: prs.find { it.recordType == "1RM" }
                        ?: prs.find { it.recordType == "Distance" }
                        ?: prs.find { it.recordType == "Duration" }
                        ?: prs.find { it.recordType == "Speed" }
                        ?: prs.first()
                    
                    // Delete all PRs except the best one
                    prs.filter { it.id != bestPR.id }.forEach { pr ->
                        personalRecordDao.deletePersonalRecord(pr)
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
