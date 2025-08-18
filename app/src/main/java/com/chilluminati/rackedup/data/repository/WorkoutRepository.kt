package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.WorkoutDao
import com.chilluminati.rackedup.data.database.dao.WorkoutExerciseDao
import com.chilluminati.rackedup.data.database.dao.ExerciseSetDao
import com.chilluminati.rackedup.data.database.entity.Workout
import com.chilluminati.rackedup.data.database.entity.WorkoutExercise
import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Date
import com.chilluminati.rackedup.data.database.entity.ProgramExercise
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for workout-related data operations
 */
@Singleton
class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val progressRepository: ProgressRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    
    /**
     * Get all workouts ordered by date (newest first)
     */
    fun getAllWorkouts(): Flow<List<Workout>> = workoutDao.getAllWorkouts()

    /**
     * Get all workouts as a concrete list (one-shot), ordered by date (newest first)
     */
    suspend fun getAllWorkoutsList(): List<Workout> {
        return withContext(ioDispatcher) {
            workoutDao.getAllWorkoutsList()
        }
    }
    
    /**
     * Get workout by ID
     */
    suspend fun getWorkoutById(workoutId: Long): Workout? {
        return withContext(ioDispatcher) {
            workoutDao.getWorkoutById(workoutId)
        }
    }
    
    /**
     * Get the last completed workout for a program day
     */
    suspend fun getLastWorkoutForProgramDay(programId: Long, programDayId: Long): Workout? {
        return withContext(ioDispatcher) {
            workoutDao.getAllWorkoutsList()
                .filter { it.programId == programId && it.programDayId == programDayId && it.isCompleted }
                .maxByOrNull { it.date.time }
        }
    }
    
    /**
     * Get exercise sets from a workout
     */
    suspend fun getExerciseSetsFromWorkout(workoutId: Long, exerciseId: Long): List<ExerciseSet> {
        return withContext(ioDispatcher) {
            // First find the workout exercise
            val workoutExercises = workoutExerciseDao.getWorkoutExercises(workoutId).first()
            val workoutExercise = workoutExercises.find { it.exerciseId == exerciseId }
            
            // Then get its sets
            workoutExercise?.let { exercise ->
                exerciseSetDao.getExerciseSets(exercise.id).first()
            } ?: emptyList()
        }
    }
    
    /**
     * Get workout exercises for a specific workout
     */
    fun getWorkoutExercises(workoutId: Long): Flow<List<WorkoutExercise>> {
        return workoutExerciseDao.getWorkoutExercises(workoutId)
    }
    
    /**
     * Get exercise sets for a specific workout exercise
     */
    fun getExerciseSets(workoutExerciseId: Long): Flow<List<ExerciseSet>> {
        return exerciseSetDao.getExerciseSets(workoutExerciseId)
    }
    
    /**
     * Create a new workout
     */
    suspend fun createWorkout(
        name: String,
        date: Date = Date(),
        notes: String? = null,
        programId: Long? = null,
        programDayId: Long? = null
    ): Long {
        return withContext(ioDispatcher) {
            val workout = Workout(
                name = name,
                date = date,
                notes = notes,
                programId = programId,
                programDayId = programDayId
            )
            workoutDao.insertWorkout(workout)
        }
    }
    
    /**
     * Start a workout (set start time)
     */
    suspend fun startWorkout(workoutId: Long) {
        withContext(ioDispatcher) {
            val workout = workoutDao.getWorkoutById(workoutId)
            workout?.let {
                val updatedWorkout = it.copy(startTime = Date())
                workoutDao.updateWorkout(updatedWorkout)
            }
        }
    }
    
    /**
     * Finish a workout (set end time, calculate duration, totals, and mark as completed)
     */
    suspend fun finishWorkout(workoutId: Long) {
        withContext(ioDispatcher) {
            val workout = workoutDao.getWorkoutById(workoutId)
            workout?.let {
                val endTime = Date()
                // Round up to the next minute so super-short workouts don't show 0 min
                val durationMinutes = run {
                    val startMs = (it.startTime ?: it.date).time
                    val diffMs = (endTime.time - startMs).coerceAtLeast(0)
                    val minutes = kotlin.math.ceil(diffMs / 60000.0).toInt()
                    // If there was any elapsed time, show at least 1 minute
                    if (diffMs > 0 && minutes == 0) 1 else minutes
                }
                
                // Calculate workout totals
                val allWorkoutExercises = workoutExerciseDao.getWorkoutExercises(workoutId).first()
                var totalVolume = 0.0
                var totalSets = 0
                var totalReps = 0
                
                allWorkoutExercises.forEach { we ->
                    val sets = exerciseSetDao.getExerciseSets(we.id).first()
                    sets.forEach { s ->
                        if (s.isCompleted) {
                            val weight = s.weight ?: 0.0
                            val reps = s.reps ?: 0
                            totalVolume += weight * reps
                            totalSets++
                            totalReps += reps
                        }
                    }
                }
                
                val updatedWorkout = it.copy(
                    endTime = endTime,
                    durationMinutes = durationMinutes,
                    totalVolume = totalVolume,
                    totalSets = totalSets,
                    totalReps = totalReps,
                    isCompleted = true
                )
                workoutDao.updateWorkout(updatedWorkout)
                
                // Check for new personal records after workout completion
                progressRepository.checkAndUpdateVolumePRs(workoutId)
            }
        }
    }
    
    /**
     * Update workout
     */
    suspend fun updateWorkout(workout: Workout) {
        withContext(ioDispatcher) {
            workoutDao.updateWorkout(workout)
        }
    }
    
    /**
     * Delete workout
     */
    suspend fun deleteWorkout(workout: Workout) {
        withContext(ioDispatcher) {
            workoutDao.deleteWorkout(workout)
        }
    }
    
    /**
     * Add exercise to workout
     */
    suspend fun addExerciseToWorkout(
        workoutId: Long,
        exerciseId: Long,
        orderIndex: Int,
        programExercise: ProgramExercise? = null,
        lastSets: List<ExerciseSet> = emptyList()
    ): Long {
        return withContext(ioDispatcher) {
            val workoutExercise = WorkoutExercise(
                workoutId = workoutId,
                exerciseId = exerciseId,
                orderIndex = orderIndex
            )
            val workoutExerciseId = workoutExerciseDao.insertWorkoutExercise(workoutExercise)
            
            // If this is from a program exercise, create the initial sets
            if (programExercise != null) {
                // Parse sets format (e.g., "3x8-12" or "5x5")
                val setsCount = programExercise.sets.split("x").firstOrNull()?.toIntOrNull() ?: 0
                
                // Create sets with target reps and previous weights if available
                repeat(setsCount) { setNumber ->
                    val lastSet = lastSets.getOrNull(setNumber)
                    val exerciseSet = ExerciseSet(
                        workoutExerciseId = workoutExerciseId,
                        setNumber = setNumber + 1,
                        reps = programExercise.reps?.split("-")?.firstOrNull()?.toIntOrNull(),
                        weight = lastSet?.weight,
                        restTimeSeconds = programExercise.restTimeSeconds
                    )
                    exerciseSetDao.insertExerciseSet(exerciseSet)
                }
            }
            
            workoutExerciseId
        }
    }
    
    /**
     * Add set to workout exercise
     */
    suspend fun addSetToWorkoutExercise(
        workoutExerciseId: Long,
        setNumber: Int,
        weight: Double? = null,
        reps: Int? = null,
        durationSeconds: Int? = null,
        distance: Double? = null,
        restTimeSeconds: Int? = null
    ): Long {
        return withContext(ioDispatcher) {
            val exerciseSet = ExerciseSet(
                workoutExerciseId = workoutExerciseId,
                setNumber = setNumber,
                weight = weight,
                reps = reps,
                durationSeconds = durationSeconds,
                distance = distance,
                restTimeSeconds = restTimeSeconds
            )
            exerciseSetDao.insertExerciseSet(exerciseSet)
        }
    }
    
    /**
     * Update set
     */
    suspend fun updateSet(set: ExerciseSet) {
        withContext(ioDispatcher) {
            exerciseSetDao.updateExerciseSet(set)
            
            // Get the workout exercise to find the workout
            val workoutExercise = workoutExerciseDao.getWorkoutExerciseById(set.workoutExerciseId)
            workoutExercise?.let { exercise ->
                // Get all sets for this workout to recalculate total volume
                val allWorkoutExercises = workoutExerciseDao.getWorkoutExercises(exercise.workoutId).first()
                var totalVolume = 0.0
                var totalSets = 0
                var totalReps = 0
                
                allWorkoutExercises.forEach { we ->
                    val sets = exerciseSetDao.getExerciseSets(we.id).first()
                    sets.forEach { s ->
                        if (s.isCompleted) {
                            val weight = s.weight ?: 0.0
                            val reps = s.reps ?: 0
                            totalVolume += weight * reps
                            totalSets++
                            totalReps += reps
                        }
                    }
                }
                
                // Update the workout with new totals
                val workout = workoutDao.getWorkoutById(exercise.workoutId)
                workout?.let { w ->
                    workoutDao.updateWorkout(w.copy(
                        totalVolume = totalVolume,
                        totalSets = totalSets,
                        totalReps = totalReps
                    ))
                }
            }
        }
    }
    
    /**
     * Delete set
     */
    suspend fun deleteSet(set: ExerciseSet) {
        withContext(ioDispatcher) {
            exerciseSetDao.deleteExerciseSet(set)
        }
    }
    
    /**
     * Recalculate totals for a workout (useful for fixing existing data)
     */
    suspend fun recalculateWorkoutTotals(workoutId: Long) {
        withContext(ioDispatcher) {
            val workout = workoutDao.getWorkoutById(workoutId)
            workout?.let {
                val allWorkoutExercises = workoutExerciseDao.getWorkoutExercises(workoutId).first()
                var totalVolume = 0.0
                var totalSets = 0
                var totalReps = 0
                
                allWorkoutExercises.forEach { we ->
                    val sets = exerciseSetDao.getExerciseSets(we.id).first()
                    sets.forEach { s ->
                        if (s.isCompleted) {
                            val weight = s.weight ?: 0.0
                            val reps = s.reps ?: 0
                            totalVolume += weight * reps
                            totalSets++
                            totalReps += reps
                        }
                    }
                }
                
                val updatedWorkout = it.copy(
                    totalVolume = totalVolume,
                    totalSets = totalSets,
                    totalReps = totalReps
                )
                workoutDao.updateWorkout(updatedWorkout)
            }
        }
    }
    
    /**
     * Get recent workouts (last 10)
     */
    suspend fun getRecentWorkouts(limit: Int = 10): List<Workout> {
        return withContext(ioDispatcher) {
            workoutDao.getAllWorkouts().first().sortedByDescending { it.date }.take(limit)
        }
    }
    
    /**
     * Calculate workout statistics
     */
    suspend fun calculateWorkoutStats(workoutId: Long): WorkoutStats {
        return withContext(ioDispatcher) {
            if (workoutDao.getWorkoutById(workoutId) == null) {
                return@withContext WorkoutStats()
            }
            val allWorkoutExercises = workoutExerciseDao.getWorkoutExercises(workoutId).first()
            var totalVolume = 0.0
            var totalSets = 0
            var totalReps = 0
            allWorkoutExercises.forEach { we ->
                val sets = exerciseSetDao.getExerciseSets(we.id).first()
                sets.forEach { s ->
                    if (s.isCompleted) {
                        val weight = s.weight ?: 0.0
                        val reps = s.reps ?: 0
                        totalVolume += weight * reps
                        totalSets++
                        totalReps += reps
                    }
                }
            }
            WorkoutStats(
                totalVolume = totalVolume,
                totalSets = totalSets,
                totalReps = totalReps,
                exerciseCount = allWorkoutExercises.size,
                averageRestTime = 0
            )
        }
    }
}

/**
 * Data class for workout statistics
 */
data class WorkoutStats(
    val totalVolume: Double = 0.0,
    val totalSets: Int = 0,
    val totalReps: Int = 0,
    val exerciseCount: Int = 0,
    val averageRestTime: Int = 0
)
