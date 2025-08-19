package com.chilluminati.rackedup.presentation.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.database.entity.Exercise
import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.data.database.entity.WorkoutExercise
import com.chilluminati.rackedup.data.repository.ExerciseRepository
import com.chilluminati.rackedup.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseEditUiState(
    val isLoading: Boolean = false,
    val exercise: Exercise? = null,
    val workoutExercise: WorkoutExercise? = null,
    val sets: List<ExerciseSet> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ExerciseEditViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExerciseEditUiState())
    val uiState: StateFlow<ExerciseEditUiState> = _uiState.asStateFlow()

    fun loadExerciseDetails(workoutExerciseId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Load workout exercise
                val workoutExercise = workoutRepository.getWorkoutExerciseById(workoutExerciseId)
                
                // Load exercise details
                val exercise = exerciseRepository.getExerciseById(workoutExercise.exerciseId)
                
                // Load sets
                val sets = workoutRepository.getExerciseSetsList(workoutExerciseId)
                
                _uiState.value = ExerciseEditUiState(
                    isLoading = false,
                    exercise = exercise,
                    workoutExercise = workoutExercise,
                    sets = sets
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun updateSetWeight(setIndex: Int, weight: Double?) {
        val currentSets = _uiState.value.sets.toMutableList()
        if (setIndex < currentSets.size) {
            currentSets[setIndex] = currentSets[setIndex].copy(weight = weight)
            _uiState.value = _uiState.value.copy(sets = currentSets)
        }
    }

    fun updateSetReps(setIndex: Int, reps: Int?) {
        val currentSets = _uiState.value.sets.toMutableList()
        if (setIndex < currentSets.size) {
            currentSets[setIndex] = currentSets[setIndex].copy(reps = reps)
            _uiState.value = _uiState.value.copy(sets = currentSets)
        }
    }



    fun addSet() {
        val currentSets = _uiState.value.sets.toMutableList()
        val workoutExercise = _uiState.value.workoutExercise
        if (workoutExercise != null) {
            val newSet = ExerciseSet(
                id = 0, // Will be set by database
                workoutExerciseId = workoutExercise.id,
                setNumber = currentSets.size + 1,
                weight = null,
                reps = null,
                durationSeconds = null,
                notes = null
            )
            currentSets.add(newSet)
            _uiState.value = _uiState.value.copy(sets = currentSets)
        }
    }

    fun deleteSet(setIndex: Int) {
        val currentSets = _uiState.value.sets.toMutableList()
        if (setIndex < currentSets.size) {
            currentSets.removeAt(setIndex)
            // Update set numbers
            currentSets.forEachIndexed { index, set ->
                currentSets[index] = set.copy(setNumber = index + 1)
            }
            _uiState.value = _uiState.value.copy(sets = currentSets)
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            try {
                val sets = _uiState.value.sets
                val workoutExercise = _uiState.value.workoutExercise
                
                if (workoutExercise != null) {
                    // Delete existing sets
                    workoutRepository.deleteExerciseSets(workoutExercise.id)
                    
                    // Insert updated sets
                    sets.forEach { set ->
                        workoutRepository.insertExerciseSet(set.copy(workoutExerciseId = workoutExercise.id))
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
