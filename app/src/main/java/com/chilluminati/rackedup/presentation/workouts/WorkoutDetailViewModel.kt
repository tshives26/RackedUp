package com.chilluminati.rackedup.presentation.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.repository.WorkoutRepository
import com.chilluminati.rackedup.data.repository.ExerciseRepository
import com.chilluminati.rackedup.data.database.entity.Workout
import com.chilluminati.rackedup.data.database.entity.WorkoutExercise
import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.data.database.entity.Exercise
import com.chilluminati.rackedup.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for workout detail screen
 */
@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WorkoutDetailUiState())
    val uiState: StateFlow<WorkoutDetailUiState> = _uiState.asStateFlow()
    
    fun loadWorkoutDetail(workoutId: Long) {
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Load workout
                val workout = workoutRepository.getWorkoutById(workoutId)
                if (workout == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Workout not found"
                    )
                    return@launch
                }
                
                // Load workout exercises with sets
                workoutRepository.getWorkoutExercises(workoutId).collect { workoutExercises ->
                    // Load exercise details and sets for each workout exercise
                    val exerciseDetails = mutableMapOf<Long, Exercise>()
                    val exerciseSets = mutableMapOf<Long, List<ExerciseSet>>()
                    
                    workoutExercises.forEach { workoutExercise ->
                        // Load exercise details
                        exerciseRepository.getExerciseById(workoutExercise.exerciseId)?.let { exercise ->
                            exerciseDetails[workoutExercise.id] = exercise
                        }
                        
                        // Load sets for this exercise
                        workoutRepository.getExerciseSets(workoutExercise.id).take(1).collect { sets ->
                            exerciseSets[workoutExercise.id] = sets
                        }
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        workout = workout,
                        workoutExercises = workoutExercises,
                        exerciseDetails = exerciseDetails,
                        exerciseSets = exerciseSets,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load workout details"
                )
            }
        }
    }
    
    fun updateWorkout(workout: Workout) {
        viewModelScope.launch(ioDispatcher) {
            try {
                workoutRepository.updateWorkout(workout)
                _uiState.value = _uiState.value.copy(workout = workout)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to update workout"
                )
            }
        }
    }
    
    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch(ioDispatcher) {
            try {
                workoutRepository.deleteWorkout(workout)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to delete workout"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * UI state for workout detail screen
 */
data class WorkoutDetailUiState(
    val workout: Workout? = null,
    val workoutExercises: List<WorkoutExercise> = emptyList(),
    val exerciseDetails: Map<Long, Exercise> = emptyMap(),
    val exerciseSets: Map<Long, List<ExerciseSet>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val totalVolume: Double get() = exerciseSets.values.flatten().sumOf { set ->
        val weight = set.weight ?: 0.0
        val reps = set.reps ?: 0
        weight * reps
    }
    
    val totalSets: Int get() = exerciseSets.values.sumOf { it.size }
    
    val totalExercises: Int get() = workoutExercises.size
    
    val personalRecords: Int get() = 0 // TODO: Implement PR detection
}
