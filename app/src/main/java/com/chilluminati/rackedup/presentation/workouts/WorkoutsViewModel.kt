package com.chilluminati.rackedup.presentation.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.repository.WorkoutRepository
import com.chilluminati.rackedup.data.repository.ExerciseRepository
import com.chilluminati.rackedup.data.repository.SettingsRepository
import com.chilluminati.rackedup.data.repository.ActiveWorkoutSessionRepository
import com.chilluminati.rackedup.data.repository.ProgramRepository
import com.chilluminati.rackedup.data.database.entity.Workout
import com.chilluminati.rackedup.data.database.entity.Exercise
import com.chilluminati.rackedup.data.database.entity.WorkoutExercise
import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel for workouts screen and workout management
 */
@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    private val settingsRepository: SettingsRepository,
    private val activeSessionRepository: ActiveWorkoutSessionRepository,
    private val programRepository: ProgramRepository,
    private val soundManager: com.chilluminati.rackedup.core.sound.SoundManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WorkoutsUiState())
    val uiState: StateFlow<WorkoutsUiState> = _uiState.asStateFlow()
    
    private val _activeWorkoutState = MutableStateFlow(ActiveWorkoutState())
    val activeWorkoutState: StateFlow<ActiveWorkoutState> = _activeWorkoutState.asStateFlow()

    val activeSessionFlow = activeSessionRepository.activeSession
    
    init {
        loadWorkouts()
        loadSettings()
        observePersistedActiveSession()
    }
    
    private fun loadWorkouts() {
        viewModelScope.launch(ioDispatcher) {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                workoutRepository.getAllWorkouts().collect { workouts ->
                    _uiState.value = _uiState.value.copy(
                        workouts = workouts,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load workouts"
                )
            }
        }
    }
    
    private fun loadSettings() {
        viewModelScope.launch(ioDispatcher) {
            settingsRepository.weightUnit.collect { unit ->
                _uiState.value = _uiState.value.copy(weightUnit = unit)
            }
        }
        // Collect default rest timer seconds so UI can use it when starting rest
        viewModelScope.launch(ioDispatcher) {
            settingsRepository.defaultRestSeconds.collect { seconds ->
                _uiState.value = _uiState.value.copy(defaultRestSeconds = seconds)
            }
        }
    }

    private fun observePersistedActiveSession() {
        viewModelScope.launch(ioDispatcher) {
            activeSessionRepository.activeSession.collect { session ->
                if (session == null) return@collect
                if (!_activeWorkoutState.value.isActive || _activeWorkoutState.value.currentWorkout?.id != session.workoutId) {
                    // Rehydrate minimal state so UI can show resume banner elsewhere.
                    val workout = workoutRepository.getWorkoutById(session.workoutId)
                    if (workout != null && !workout.isCompleted) {
                        _activeWorkoutState.value = _activeWorkoutState.value.copy(
                            currentWorkout = workout,
                            isActive = true,
                            startTime = workout.startTime
                        )
                        // Keep exercises lazy-loaded by existing collectors when screen opens
                    } else if (workout?.isCompleted == true) {
                        // Workout is completed but session wasn't cleared, clear it now
                        activeSessionRepository.clearSession()
                        _activeWorkoutState.value = ActiveWorkoutState() // Reset state
                    }
                }
            }
        }
    }
    
    /**
     * Create a new workout
     */
    suspend fun createWorkout(name: String, notes: String? = null): Long? {
        return try {
            workoutRepository.createWorkout(
                name = name,
                notes = notes,
                date = Date()
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = e.message ?: "Failed to create workout"
            )
            null
        }
    }
    
    /**
     * Create a new workout and start it immediately
     */
    fun createAndStartWorkout(name: String, notes: String? = null) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val workoutId = createWorkout(name, notes)
                workoutId?.let { startWorkout(it) }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to create and start workout"
                )
            }
        }
    }
    
    /**
     * Start a workout (set as active workout)
     */
    fun startWorkout(workoutId: Long) {
        viewModelScope.launch(ioDispatcher) {
            try {
                workoutRepository.startWorkout(workoutId)
                
                val workout = workoutRepository.getWorkoutById(workoutId)
                if (workout != null) {
                    _activeWorkoutState.value = _activeWorkoutState.value.copy(
                        currentWorkout = workout,
                        isActive = true,
                        startTime = Date(),
                        isLoading = true,
                        // Clear any previous session data to avoid stale totals
                        workoutExercises = emptyList(),
                        exerciseSets = emptyMap(),
                        exerciseDetails = emptyMap()
                    )
                    // Persist active session
                    activeSessionRepository.setActiveWorkout(
                        workoutId = workoutId,
                        isPaused = false,
                        workoutElapsedSeconds = 0,
                        isResting = false,
                        restRemainingSeconds = 0,
                        draftsJson = null
                    )
                    
                    // Load workout exercises and program day name
                    loadWorkoutExercises(workoutId)
                    loadProgramDayName(workout)
                }
            } catch (e: Exception) {
                _activeWorkoutState.value = _activeWorkoutState.value.copy(
                    error = e.message ?: "Failed to start workout"
                )
            }
        }
    }
    
    /**
     * Resume an existing workout (preserve timer state)
     */
    fun resumeWorkout(workoutId: Long) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val workout = workoutRepository.getWorkoutById(workoutId)
                if (workout != null) {
                    // Only update the workout data, don't change the active state
                    // This preserves the existing session and timer
                    _activeWorkoutState.value = _activeWorkoutState.value.copy(
                        currentWorkout = workout,
                        isLoading = true,
                        // Clear maps to ensure fresh load for this workout
                        workoutExercises = emptyList(),
                        exerciseSets = emptyMap(),
                        exerciseDetails = emptyMap()
                    )
                    
                    // Load workout exercises and program day name
                    loadWorkoutExercises(workoutId)
                    loadProgramDayName(workout)
                }
            } catch (e: Exception) {
                _activeWorkoutState.value = _activeWorkoutState.value.copy(
                    error = e.message ?: "Failed to resume workout"
                )
            }
        }
    }
    
    // Event channel for workout completion
    private val _workoutCompletionEvent = MutableSharedFlow<Long>()
    val workoutCompletionEvent = _workoutCompletionEvent.asSharedFlow()

    /**
     * Finish the active workout
     */
    fun finishWorkout() {
        viewModelScope.launch(ioDispatcher) {
            try {
                val currentWorkout = _activeWorkoutState.value.currentWorkout
                if (currentWorkout != null) {
                    workoutRepository.finishWorkout(currentWorkout.id)
                    
                    // Emit workout completion event
                    _workoutCompletionEvent.emit(currentWorkout.id)
                    
                    activeSessionRepository.clearSession()
                    _activeWorkoutState.value = ActiveWorkoutState() // Reset state
                }
            } catch (e: Exception) {
                _activeWorkoutState.value = _activeWorkoutState.value.copy(
                    error = e.message ?: "Failed to finish workout"
                )
            }
        }
    }

    /**
     * Persist timer state updates for resume across screens/process death.
     */
    fun updateSessionTimers(
        workoutElapsedSeconds: Int? = null,
        isPaused: Boolean? = null,
        isResting: Boolean? = null,
        restRemainingSeconds: Int? = null
    ) {
        viewModelScope.launch(ioDispatcher) {
            activeSessionRepository.updateTimers(
                workoutElapsedSeconds = workoutElapsedSeconds,
                isPaused = isPaused,
                isResting = isResting,
                restRemainingSeconds = restRemainingSeconds
            )
        }
    }
    
    /**
     * Load exercises for a workout
     */
    private fun loadWorkoutExercises(workoutId: Long) {
        viewModelScope.launch(ioDispatcher) {
            try {
                // Set loading state
                _activeWorkoutState.value = _activeWorkoutState.value.copy(isLoading = true)
                
                workoutRepository.getWorkoutExercises(workoutId).collect { workoutExercises ->
                    try {
                        // Load all exercise details first
                        val exerciseDetails = mutableMapOf<Long, Exercise>()
                        workoutExercises.forEach { workoutExercise ->
                            exerciseRepository.getExerciseById(workoutExercise.exerciseId)?.let { exercise ->
                                exerciseDetails[workoutExercise.exerciseId] = exercise
                            }
                        }
                        
                        // Load all sets for all exercises concurrently
                        val exerciseSets = mutableMapOf<Long, List<ExerciseSet>>()
                        val setLoadingJobs = workoutExercises.map { workoutExercise ->
                            async {
                                try {
                                    val sets = workoutRepository.getExerciseSets(workoutExercise.id).first()
                                    workoutExercise.id to sets
                                } catch (e: Exception) {
                                    // If loading sets fails for an exercise, use empty list
                                    workoutExercise.id to emptyList<ExerciseSet>()
                                }
                            }
                        }
                        
                        // Wait for all sets to load
                        setLoadingJobs.awaitAll().forEach { (exerciseId, sets) ->
                            exerciseSets[exerciseId] = sets
                        }
                        
                        // Update state with all data at once
                        _activeWorkoutState.value = _activeWorkoutState.value.copy(
                            workoutExercises = workoutExercises,
                            exerciseDetails = exerciseDetails,
                            exerciseSets = exerciseSets,
                            isLoading = false,
                            error = null
                        )
                    } catch (e: Exception) {
                        _activeWorkoutState.value = _activeWorkoutState.value.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load workout data"
                        )
                    }
                }
            } catch (e: Exception) {
                _activeWorkoutState.value = _activeWorkoutState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load workout exercises"
                )
            }
        }
    }
    
    /**
     * Load program day name for the current workout
     */
    private fun loadProgramDayName(workout: Workout) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val programDayName = if (workout.programDayId != null) {
                    getProgramDayName(workout.programDayId)
                } else {
                    null
                }
                _activeWorkoutState.value = _activeWorkoutState.value.copy(
                    programDayName = programDayName
                )
            } catch (e: Exception) {
                // If we can't load the program day name, just continue without it
            }
        }
    }
    
    /**
     * Refresh sets for a specific workout exercise
     */
    private fun refreshExerciseSets(workoutExerciseId: Long) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val updatedSets = workoutRepository.getExerciseSets(workoutExerciseId).first()
                val currentState = _activeWorkoutState.value
                val updatedExerciseSets = currentState.exerciseSets.toMutableMap()
                updatedExerciseSets[workoutExerciseId] = updatedSets
                
                _activeWorkoutState.value = currentState.copy(
                    exerciseSets = updatedExerciseSets
                )
            } catch (e: Exception) {
                // If refresh fails, fall back to full reload
                val currentWorkout = _activeWorkoutState.value.currentWorkout
                if (currentWorkout != null) {
                    loadWorkoutExercises(currentWorkout.id)
                }
            }
        }
    }
    

    
    /**
     * Add exercise to active workout
     */
    fun addExerciseToWorkout(exerciseId: Long) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val currentWorkout = _activeWorkoutState.value.currentWorkout
                if (currentWorkout != null) {
                    val orderIndex = _activeWorkoutState.value.workoutExercises.size
                    
                    workoutRepository.addExerciseToWorkout(
                        workoutId = currentWorkout.id,
                        exerciseId = exerciseId,
                        orderIndex = orderIndex
                    )
                }
            } catch (e: Exception) {
                _activeWorkoutState.value = _activeWorkoutState.value.copy(
                    error = e.message ?: "Failed to add exercise"
                )
            }
        }
    }
    
    /**
     * Add set to exercise
     */
    fun addSet(
        workoutExerciseId: Long,
        weight: Double? = null,
        reps: Int? = null,
        durationSeconds: Int? = null
    ) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val currentSets = _activeWorkoutState.value.exerciseSets[workoutExerciseId] ?: emptyList()
                val setNumber = currentSets.size + 1
                
                workoutRepository.addSetToWorkoutExercise(
                    workoutExerciseId = workoutExerciseId,
                    setNumber = setNumber,
                    weight = weight,
                    reps = reps,
                    durationSeconds = durationSeconds
                )
                
                // Refresh only the sets for this specific exercise
                refreshExerciseSets(workoutExerciseId)
            } catch (e: Exception) {
                _activeWorkoutState.value = _activeWorkoutState.value.copy(
                    error = e.message ?: "Failed to add set"
                )
            }
        }
    }
    
    /**
     * Update set
     */
    fun updateSet(set: ExerciseSet) {
        viewModelScope.launch(ioDispatcher) {
            try {
                workoutRepository.updateSet(set)
                
                // Refresh only the sets for this specific exercise
                refreshExerciseSets(set.workoutExerciseId)
            } catch (e: Exception) {
                _activeWorkoutState.value = _activeWorkoutState.value.copy(
                    error = e.message ?: "Failed to update set"
                )
            }
        }
    }
    
    /**
     * Delete set
     */
    fun deleteSet(set: ExerciseSet) {
        viewModelScope.launch(ioDispatcher) {
            try {
                workoutRepository.deleteSet(set)
                
                // Refresh only the sets for this specific exercise
                refreshExerciseSets(set.workoutExerciseId)
            } catch (e: Exception) {
                _activeWorkoutState.value = _activeWorkoutState.value.copy(
                    error = e.message ?: "Failed to delete set"
                )
            }
        }
    }
    
    /**
     * Delete workout
     */
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
    
    /**
     * Search workouts
     */
    fun searchWorkouts(query: String) {
        viewModelScope.launch(ioDispatcher) {
            val allWorkouts = _uiState.value.workouts
            val filteredWorkouts = if (query.isBlank()) {
                allWorkouts
            } else {
                allWorkouts.filter { workout ->
                    workout.name.contains(query, ignoreCase = true) ||
                    workout.notes?.contains(query, ignoreCase = true) == true
                }
            }
            
            _uiState.value = _uiState.value.copy(
                filteredWorkouts = filteredWorkouts,
                searchQuery = query
            )
        }
    }
    
    fun onRestTimerComplete() {
        viewModelScope.launch(ioDispatcher) {
            updateSessionTimers(isResting = false, restRemainingSeconds = 0)
            soundManager.playRestTimerComplete()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
        _activeWorkoutState.value = _activeWorkoutState.value.copy(error = null)
    }
    
    /**
     * Get program day name by ID
     */
    suspend fun getProgramDayName(programDayId: Long): String? {
        return try {
            val programDay = programRepository.getProgramDayById(programDayId)
            programDay?.name
        } catch (e: Exception) {
            null
        }
    }
    
    fun refreshWorkouts() {
        loadWorkouts()
    }
    
    /**
     * Debug function to clear active session (for testing)
     */
    fun clearActiveSession() {
        viewModelScope.launch(ioDispatcher) {
            activeSessionRepository.clearSession()
            _activeWorkoutState.value = ActiveWorkoutState() // Reset state
        }
    }
}

/**
 * UI state for workouts screen
 */
data class WorkoutsUiState(
    val workouts: List<Workout> = emptyList(),
    val filteredWorkouts: List<Workout> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val weightUnit: String = "lbs",
    val defaultRestSeconds: Int = 120
)

/**
 * State for active workout
 */
data class ActiveWorkoutState(
    val currentWorkout: Workout? = null,
    val workoutExercises: List<WorkoutExercise> = emptyList(),
    val exerciseSets: Map<Long, List<ExerciseSet>> = emptyMap(),
    val exerciseDetails: Map<Long, Exercise> = emptyMap(),
    val isActive: Boolean = false,
    val isLoading: Boolean = false,
    val startTime: Date? = null,
    val currentExerciseIndex: Int = 0,
    val restTimer: Int = 0, // seconds
    val isResting: Boolean = false,
    val programDayName: String? = null,
    val error: String? = null
)
