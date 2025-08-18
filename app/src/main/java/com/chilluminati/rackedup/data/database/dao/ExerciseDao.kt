package com.chilluminati.rackedup.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.chilluminati.rackedup.data.database.entity.Exercise

/**
 * DAO for Exercise operations
 */
@Dao
interface ExerciseDao {
    
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getAllExercises(): Flow<List<Exercise>>
    
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    suspend fun getAllExercisesList(): List<Exercise>
    
    @Query("SELECT * FROM exercises WHERE name = :name LIMIT 1")
    suspend fun getExerciseByName(name: String): Exercise?
    
    @Query("SELECT * FROM exercises WHERE category = :category ORDER BY name ASC")
    fun getExercisesByCategory(category: String): Flow<List<Exercise>>
    
    @Query("SELECT * FROM exercises WHERE is_favorite = 1 ORDER BY name ASC")
    fun getFavoriteExercises(): Flow<List<Exercise>>
    
    @Query("SELECT * FROM exercises WHERE is_custom = 1 ORDER BY name ASC")
    fun getCustomExercises(): Flow<List<Exercise>>
    
    @Query("SELECT * FROM exercises WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchExercises(searchQuery: String): Flow<List<Exercise>>
    
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    suspend fun getExerciseById(exerciseId: Long): Exercise?
    
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    fun getExerciseByIdFlow(exerciseId: Long): Flow<Exercise?>
    
    @Query("SELECT DISTINCT category FROM exercises ORDER BY category ASC")
    suspend fun getAllCategories(): List<String>
    
    @Query("SELECT DISTINCT equipment FROM exercises ORDER BY equipment ASC")
    suspend fun getAllEquipment(): List<String>
    
    @Query("DELETE FROM exercises")
    suspend fun clearAll()
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)
    
    @Update
    suspend fun updateExercise(exercise: Exercise)
    
    @Delete
    suspend fun deleteExercise(exercise: Exercise)
    
    @Query("DELETE FROM exercises WHERE id = :exerciseId")
    suspend fun deleteExerciseById(exerciseId: Long)
    
    @Query("UPDATE exercises SET is_favorite = :isFavorite WHERE id = :exerciseId")
    suspend fun updateFavoriteStatus(exerciseId: Long, isFavorite: Boolean)
}
