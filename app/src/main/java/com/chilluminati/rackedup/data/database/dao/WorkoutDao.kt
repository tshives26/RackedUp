package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.Workout
import java.util.Date

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<Workout>>
    
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    suspend fun getAllWorkoutsList(): List<Workout>
    
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: Long): Workout?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<Workout>)
    
    @Update
    suspend fun updateWorkout(workout: Workout)
    
    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT * FROM workouts WHERE name = :name AND date = :date")
    suspend fun getWorkoutsByNameAndDate(name: String, date: Date): List<Workout>
    
    @Query("UPDATE workouts SET total_volume = :totalVolume, total_sets = :totalSets WHERE id = :workoutId")
    suspend fun updateWorkoutTotals(workoutId: Long, totalVolume: Double, totalSets: Int)
    
    @Query("UPDATE workouts SET total_volume = (SELECT COALESCE(SUM(es.weight * es.reps), 0) FROM exercise_sets es INNER JOIN workout_exercises we ON es.workout_exercise_id = we.id WHERE we.workout_id = :workoutId AND es.weight IS NOT NULL AND es.reps IS NOT NULL), total_sets = (SELECT COUNT(*) FROM exercise_sets es INNER JOIN workout_exercises we ON es.workout_exercise_id = we.id WHERE we.workout_id = :workoutId) WHERE id = :workoutId")
    suspend fun recalculateWorkoutTotals(workoutId: Long)
}
