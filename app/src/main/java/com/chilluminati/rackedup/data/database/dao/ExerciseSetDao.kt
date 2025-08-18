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
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSets(exerciseSets: List<ExerciseSet>)
    
    @Update
    suspend fun updateExerciseSet(exerciseSet: ExerciseSet)
    
    @Delete
    suspend fun deleteExerciseSet(exerciseSet: ExerciseSet)
}
