package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.WorkoutExercise

@Dao
interface WorkoutExerciseDao {
    @Query("SELECT * FROM workout_exercises WHERE workout_id = :workoutId ORDER BY order_index ASC")
    fun getWorkoutExercises(workoutId: Long): Flow<List<WorkoutExercise>>
    
    @Query("SELECT * FROM workout_exercises")
    suspend fun getAllWorkoutExercises(): List<WorkoutExercise>
    
    @Query("SELECT * FROM workout_exercises")
    fun getAllWorkoutExercisesFlow(): Flow<List<WorkoutExercise>>
    
    @Query("SELECT * FROM workout_exercises WHERE id = :workoutExerciseId")
    suspend fun getWorkoutExerciseById(workoutExerciseId: Long): WorkoutExercise?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExercise): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercises(workoutExercises: List<WorkoutExercise>)
    
    @Update
    suspend fun updateWorkoutExercise(workoutExercise: WorkoutExercise)
    
    @Delete
    suspend fun deleteWorkoutExercise(workoutExercise: WorkoutExercise)
}
