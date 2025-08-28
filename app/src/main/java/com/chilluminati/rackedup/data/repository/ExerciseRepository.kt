package com.chilluminati.rackedup.data.repository

import com.chilluminati.rackedup.data.database.dao.ExerciseDao
import com.chilluminati.rackedup.data.database.entity.Exercise
import com.chilluminati.rackedup.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for exercise-related data operations
 */
@Singleton
class ExerciseRepository @Inject constructor(
    private val exerciseDao: ExerciseDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    
    /**
     * Get all exercises ordered by name
     */
    fun getAllExercises(): Flow<List<Exercise>> = exerciseDao.getAllExercises()
    
    /**
     * Get all exercises as a list (synchronous)
     */
    suspend fun getAllExercisesList(): List<Exercise> {
        return withContext(ioDispatcher) {
            exerciseDao.getAllExercisesList()
        }
    }
    
    /**
     * Get exercises by category
     */
    fun getExercisesByCategory(category: String): Flow<List<Exercise>> =
        exerciseDao.getExercisesByCategory(category)
    
    /**
     * Get favorite exercises
     */
    fun getFavoriteExercises(): Flow<List<Exercise>> = exerciseDao.getFavoriteExercises()
    
    /**
     * Get custom exercises (user-created)
     */
    fun getCustomExercises(): Flow<List<Exercise>> = exerciseDao.getCustomExercises()
    
    /**
     * Search exercises by name
     */
    fun searchExercises(query: String): Flow<List<Exercise>> = exerciseDao.searchExercises(query)
    
    /**
     * Get exercise by ID
     */
    suspend fun getExerciseById(exerciseId: Long): Exercise? {
        return withContext(ioDispatcher) {
            exerciseDao.getExerciseById(exerciseId)
        }
    }
    
    /**
     * Get exercise by ID as Flow
     */
    fun getExerciseByIdFlow(exerciseId: Long): Flow<Exercise?> =
        exerciseDao.getExerciseByIdFlow(exerciseId)
    
    /**
     * Get all categories
     */
    suspend fun getAllCategories(): List<String> {
        return withContext(ioDispatcher) {
            exerciseDao.getAllCategories()
        }
    }
    
    /**
     * Get all equipment types
     */
    suspend fun getAllEquipment(): List<String> {
        return withContext(ioDispatcher) {
            exerciseDao.getAllEquipment()
        }
    }
    
    /**
     * Create a new exercise
     */
    suspend fun createExercise(
        name: String,
        category: String,
        subcategory: String? = null,
        equipment: String,
        exerciseType: String,
        difficultyLevel: String,
        muscleGroups: List<String> = emptyList(),
        secondaryMuscles: List<String> = emptyList(),
        instructions: String? = null,
        instructionSteps: List<String> = emptyList(),
        tips: String? = null,
        imageUrl: String? = null,
        videoUrl: String? = null,
        force: String? = null,
        mechanic: String? = null,
        isCompound: Boolean = false,
        isUnilateral: Boolean = false,
        isCustom: Boolean = true
    ): Long {
        return withContext(ioDispatcher) {
            val exercise = Exercise(
                name = name,
                category = category,
                subcategory = subcategory,
                equipment = equipment,
                exerciseType = exerciseType,
                difficultyLevel = difficultyLevel,
                muscleGroups = muscleGroups,
                secondaryMuscles = secondaryMuscles,
                instructions = instructions,
                instructionSteps = instructionSteps,
                tips = tips,
                force = force,
                mechanic = mechanic,
                isCompound = isCompound,
                isUnilateral = isUnilateral,
                imageUrl = imageUrl,
                videoUrl = videoUrl,
                isCustom = isCustom
            )
            exerciseDao.insertExercise(exercise)
        }
    }
    
    /**
     * Update exercise
     */
    suspend fun updateExercise(exercise: Exercise) {
        withContext(ioDispatcher) {
            exerciseDao.updateExercise(exercise)
        }
    }
    
    /**
     * Delete exercise
     */
    suspend fun deleteExercise(exercise: Exercise) {
        withContext(ioDispatcher) {
            exerciseDao.deleteExercise(exercise)
        }
    }
    
    /**
     * Toggle favorite status
     */
    suspend fun toggleFavorite(exerciseId: Long, isFavorite: Boolean) {
        withContext(ioDispatcher) {
            exerciseDao.updateFavoriteStatus(exerciseId, isFavorite)
        }
    }
    
    // Removed default seed list; importing from external dataset instead
}
