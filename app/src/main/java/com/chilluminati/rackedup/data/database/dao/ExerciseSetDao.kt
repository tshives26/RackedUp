package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.ExerciseSet

@Dao
interface ExerciseSetDao {
    @Query("SELECT * FROM exercise_sets WHERE workout_exercise_id = :workoutExerciseId ORDER BY set_number ASC")
    fun getExerciseSets(workoutExerciseId: Long): Flow<List<ExerciseSet>>
    
    @Query("SELECT * FROM exercise_sets WHERE workout_exercise_id = :workoutExerciseId ORDER BY set_number ASC")
    suspend fun getSetsByWorkoutExerciseId(workoutExerciseId: Long): List<ExerciseSet>
    
    @Query("SELECT * FROM exercise_sets")
    suspend fun getAllExerciseSets(): List<ExerciseSet>
    
    @Query("SELECT * FROM exercise_sets")
    fun getAllExerciseSetsFlow(): Flow<List<ExerciseSet>>
    
    @Query("""
        SELECT es.* FROM exercise_sets es
        INNER JOIN workout_exercises we ON es.workout_exercise_id = we.id
        INNER JOIN workouts w ON we.workout_id = w.id
        WHERE we.exercise_id = :exerciseId 
        AND es.weight IS NOT NULL 
        AND es.weight > 0
        AND es.is_completed = 1
        ORDER BY w.date DESC, es.created_at DESC
        LIMIT :limit
    """)
    suspend fun getLastCompletedSetsForExercise(exerciseId: Long, limit: Int = 10): List<ExerciseSet>
    
    @Query("""
        SELECT es.weight FROM exercise_sets es
        INNER JOIN workout_exercises we ON es.workout_exercise_id = we.id
        INNER JOIN workouts w ON we.workout_id = w.id
        WHERE we.exercise_id = :exerciseId 
        AND es.weight IS NOT NULL 
        AND es.weight > 0
        AND es.is_completed = 1
        ORDER BY w.date DESC, es.created_at DESC
        LIMIT 1
    """)
    suspend fun getLastWeightForExercise(exerciseId: Long): Double?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSets(exerciseSets: List<ExerciseSet>)
    
    @Update
    suspend fun updateExerciseSet(exerciseSet: ExerciseSet)
    
    @Delete
    suspend fun deleteExerciseSet(exerciseSet: ExerciseSet)
}
