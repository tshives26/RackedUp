package com.chilluminati.rackedup.presentation.programs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilluminati.rackedup.data.repository.ProgramRepository
import com.chilluminati.rackedup.data.repository.WorkoutRepository
import com.chilluminati.rackedup.data.repository.ExerciseRepository
import com.chilluminati.rackedup.data.database.entity.Program
import com.chilluminati.rackedup.data.database.entity.ProgramDay
import com.chilluminati.rackedup.data.database.entity.ProgramExercise
import com.chilluminati.rackedup.data.database.entity.Exercise
import com.chilluminati.rackedup.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import android.util.Log
import com.chilluminati.rackedup.presentation.programs.ProgramTemplatesSystem

/**
 * ViewModel for programs and routine management
 */
@HiltViewModel
class ProgramsViewModel @Inject constructor(
    private val programRepository: ProgramRepository,
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    companion object {
        private const val MIN_DAYS = 1
        private const val MAX_DAYS = 7
        
        // Store the program ID that we're currently editing to survive navigation/state resets
        // Using companion object to survive ViewModel recreation
        private var currentEditingProgramId: Long? = null
    }
    
    private val _uiState = MutableStateFlow(ProgramsUiState())
    val uiState: StateFlow<ProgramsUiState> = _uiState.asStateFlow()
    
    private val _builderState = MutableStateFlow(ProgramBuilderState())
    val builderState: StateFlow<ProgramBuilderState> = _builderState.asStateFlow()
    
    // Exercise data for program builder
    private val _availableExercises = MutableStateFlow<List<Exercise>>(emptyList())
    val availableExercises: StateFlow<List<Exercise>> = _availableExercises.asStateFlow()

    // Exercise categories
    private val _exerciseCategories = MutableStateFlow<List<String>>(emptyList())
    val exerciseCategories: StateFlow<List<String>> = _exerciseCategories.asStateFlow()

    // Temporary negative IDs for unsaved ProgramDays to keep per-day state distinct in builder
    private var nextTempDayId: Long = -1L

    init {
        loadPrograms()
        loadExercises()
    }
    
    private fun loadPrograms() {
        viewModelScope.launch(ioDispatcher) {
            try {
                Log.d("ProgramsViewModel", "loadPrograms: started")
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Load all programs
                programRepository.getAllPrograms().collect { allPrograms ->
                    // Show only user-created programs in "My Programs"
                    val userPrograms = allPrograms.filter { it.isCustom }
                    val templatePrograms = allPrograms.filter { it.isTemplate }
                    val activeProgram = allPrograms.firstOrNull { it.isActive }
                    // Compute last completed day label for the active program
                    val lastCompletedLabel: String? = if (activeProgram != null) {
                        try {
                            val workouts = workoutRepository.getAllWorkoutsList()
                            val latest = workouts
                                .filter { it.programId == activeProgram.id && (it.endTime != null || it.isCompleted) }
                                .maxByOrNull { it.date.time }
                            val programDayId = latest?.programDayId
                            if (programDayId != null) {
                                val days = programRepository.getProgramDays(activeProgram.id)
                                val matched = days.firstOrNull { it.id == programDayId }
                                matched?.let { d ->
                                    "Last completed: Day ${d.dayNumber} - ${d.name}"
                                }
                            } else null
                        } catch (e: Exception) { null }
                    } else null
                    Log.d(
                        "ProgramsViewModel",
                        "loadPrograms: emitted total=${allPrograms.size} user=${userPrograms.size} templates=${templatePrograms.size}"
                    )
                    
                    _uiState.value = _uiState.value.copy(
                        userPrograms = userPrograms,
                        templatePrograms = templatePrograms,
                        activeProgram = activeProgram,
                        activeProgramLastCompleted = lastCompletedLabel,
                        isLoading = false,
                        error = null
                    )
                }
                
            } catch (e: Exception) {
                Log.e("ProgramsViewModel", "loadPrograms: error", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load programs"
                )
            }
        }
    }

    private fun loadExercises() {
        viewModelScope.launch(ioDispatcher) {
            try {
                // Load all exercises
                exerciseRepository.getAllExercises().collect { exercises ->
                    _availableExercises.value = exercises
                }
                
                // Load exercise categories
                val categories = exerciseRepository.getAllCategories()
                _exerciseCategories.value = categories
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load exercises"
                )
            }
        }
    }
    
    /**
     * Get program details including days and exercises
     */
    fun loadProgramDetails(programId: Long) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val program = programRepository.getProgramById(programId)
                if (program == null) {
                    _uiState.value = _uiState.value.copy(
                        error = "Program not found"
                    )
                    return@launch
                }

                val rawDays = programRepository.getProgramDays(programId)
                // Create a map of day number to existing day
                val existingDaysByNumber = rawDays
                    .groupBy { it.dayNumber }
                    .mapValues { (_, days) -> days.minByOrNull { it.id }!! }

                // Create or use existing days for all required days
                val programDays = (1..program.daysPerWeek).map { dayNumber ->
                    existingDaysByNumber[dayNumber] ?: ProgramDay(
                        id = 0,
                        programId = programId,
                        dayNumber = dayNumber,
                        weekNumber = 1,
                        name = "Day $dayNumber"
                    )
                }
                
                val programWithDays = mutableMapOf<ProgramDay, List<ProgramExercise>>()
                programDays.forEach { day ->
                    val exercises = if (day.id != 0L) {
                        programRepository.getProgramExercises(day.id)
                    } else {
                        emptyList()
                    }
                    programWithDays[day] = exercises
                }
                // Compute last-completed date per day and most recently completed day
                val allWorkouts = workoutRepository.getAllWorkoutsList()
                val lastCompletedByDay: Map<Long, Date> = programDays
                    .mapNotNull { day ->
                        val latest = allWorkouts
                            .filter { it.programId == programId && it.programDayId == day.id && it.isCompleted }
                            .maxByOrNull { it.date.time }
                        latest?.let { day.id to it.date }
                    }
                    .toMap()
                val mostRecentCompletedDayId: Long? = lastCompletedByDay.maxByOrNull { it.value.time }?.key

                _uiState.value = _uiState.value.copy(
                    selectedProgram = program,
                    selectedProgramDays = programDays,
                    selectedProgramExercises = programWithDays,
                    selectedProgramLastCompletedByDay = lastCompletedByDay,
                    selectedProgramMostRecentCompletedDayId = mostRecentCompletedDayId
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load program details"
                )
            }
        }
    }
    
    /**
     * Start a workout from a program day
     */
    fun startWorkoutFromProgram(programId: Long, programDayId: Long) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val program = programRepository.getProgramById(programId)
                val programDay = programRepository.getProgramDays(programId)
                    .find { it.id == programDayId }
                
                if (program != null && programDay != null) {
                    val workoutName = "${program.name} - ${programDay.name}"
                    val workoutId = workoutRepository.createWorkout(
                        name = workoutName,
                        date = Date(),
                        programId = programId,
                        programDayId = programDayId
                    )
                    
                    // Add exercises from program day to workout
                    val programExercises = programRepository.getProgramExercises(programDayId)
                    
                    // Get last workout for this program day to prefill weights
                    val lastWorkout = workoutRepository.getLastWorkoutForProgramDay(programId, programDayId)
                    
                    programExercises.forEachIndexed { index, programExercise ->
                        // Get weights from last workout if available
                        val lastSets = lastWorkout?.let { workout ->
                            workoutRepository.getExerciseSetsFromWorkout(workout.id, programExercise.exerciseId)
                        } ?: emptyList()
                        
                        workoutRepository.addExerciseToWorkout(
                            workoutId = workoutId,
                            exerciseId = programExercise.exerciseId,
                            orderIndex = index,
                            programExercise = programExercise,
                            lastSets = lastSets
                        )
                    }
                    
                    // Start the workout
                    workoutRepository.startWorkout(workoutId)
                    _uiState.value = _uiState.value.copy(lastCreatedWorkoutId = workoutId)
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to start workout from program"
                )
            }
        }
    }
    
    /**
     * Copy a template program for customization
     */
    fun copyTemplateProgram(templateId: Long, newName: String) {
        viewModelScope.launch(ioDispatcher) {
            try {
                programRepository.copyTemplateProgram(templateId, newName)
                loadPrograms() // Refresh to show new program
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to copy program"
                )
            }
        }
    }
    
    /**
     * Start creating a new custom program
     */
    fun startNewProgram() {
        Log.d("ProgramsViewModel", "startNewProgram: Called - this will reset builder state!")
        
        // CRITICAL FIX: Don't reset if we're currently editing a program
        if (Companion.currentEditingProgramId != null) {
            Log.d("ProgramsViewModel", "startNewProgram: BLOCKED - currently editing programId=${Companion.currentEditingProgramId}")
            return
        }
        
        val stackTrace = Thread.currentThread().stackTrace
        Log.d("ProgramsViewModel", "startNewProgram: Stack trace:")
        stackTrace.take(10).forEach { element ->
            Log.d("ProgramsViewModel", "  at ${element.className}.${element.methodName}(${element.fileName}:${element.lineNumber})")
        }
        
        val initialDay = ProgramDay(
            id = nextTempDayId--,
            programId = 0,
            name = "Day 1",
            dayNumber = 1,
            weekNumber = 1
        )
        _builderState.value = ProgramBuilderState(
            isCreating = true,
            programName = "",
            programDescription = "",
            difficultyLevel = "Beginner",
            programType = "",
            durationWeeks = 8,
            durationEnabled = false,
            programDays = listOf(initialDay)
        )
        Log.d("ProgramsViewModel", "startNewProgram: Builder state reset to new program")
    }
    
    /**
     * Update program builder info
     */
    fun updateProgramInfo(
        name: String,
        description: String,
        difficultyLevel: String,
        programType: String,
        durationWeeks: Int,
        durationEnabled: Boolean
    ) {
        Log.d(
            "ProgramsViewModel",
            "updateProgramInfo: name='${name}', difficulty='${difficultyLevel}', type='${programType}', weeks=${durationWeeks}, enabled=${durationEnabled}"
        )
        _builderState.value = _builderState.value.copy(
            programName = name,
            programDescription = description,
            difficultyLevel = difficultyLevel,
            programType = programType,
            durationWeeks = if (durationEnabled) durationWeeks else 0,
            durationEnabled = durationEnabled
        )
    }
    
    /**
     * Update duration enabled state
     */
    fun updateDurationEnabled(enabled: Boolean) {
        val currentState = _builderState.value
        val newDurationWeeks = if (enabled && currentState.durationWeeks == 0) 8 else currentState.durationWeeks
        _builderState.value = currentState.copy(
            durationEnabled = enabled,
            durationWeeks = newDurationWeeks
        )
    }
    
    /**
     * Add day to program builder
     */
    fun addProgramDay(dayName: String, dayNumber: Int) {
        val currentDays = _builderState.value.programDays.toMutableList()
        val newDay = ProgramDay(
            id = nextTempDayId--, // Unique temp ID; real ID assigned on save
            programId = 0,
            name = dayName,
            dayNumber = dayNumber,
            weekNumber = 1
        )
        currentDays.add(newDay)
        
        _builderState.value = _builderState.value.copy(
            programDays = currentDays
        )
    }

         /**
      * Replace or align program days with provided names, preserving existing days and their
      * exercises when possible. Adds/removes days as needed. Avoids duplicating days
      * when the user revisits the schedule step.
      */
     fun setProgramDays(dayNames: List<String>) {
         Log.d("ProgramsViewModel", "setProgramDays: names=${dayNames.joinToString()} size=${dayNames.size}")
         val currentState = _builderState.value
         val currentDays = currentState.programDays.toMutableList()
         Log.d("ProgramsViewModel", "setProgramDays: currentDays=${currentDays.map { it.name }.joinToString()}")
         val updatedDays = mutableListOf<ProgramDay>()
         val updatedExercises = currentState.programExercises.toMutableMap()

         // Enforce bounds and ensure at least one day exists
         val clampedSize = dayNames.size.coerceIn(MIN_DAYS, MAX_DAYS)
         val effectiveNames = if (dayNames.isEmpty()) listOf("Day 1") else dayNames.take(clampedSize)

         // Update or create up to effectiveNames.size
         for (i in effectiveNames.indices) {
             val name = effectiveNames[i]
             if (i < currentDays.size) {
                 // Keep existing day ID to preserve exercises
                 val existing = currentDays[i]
                 updatedDays.add(existing.copy(name = name, dayNumber = i + 1))
             } else {
                 // Add new day with unique temp ID
                 val newDay = ProgramDay(
                     id = nextTempDayId--,
                     programId = 0,
                     name = name,
                     dayNumber = i + 1,
                     weekNumber = 1
                 )
                 updatedDays.add(newDay)
             }
         }

         // Remove extra days (and their exercises) if any
         if (currentDays.size > effectiveNames.size) {
             val toRemove = currentDays.drop(effectiveNames.size)
             toRemove.forEach { day ->
                 updatedExercises.remove(day.id)
             }
         }

         _builderState.value = currentState.copy(
             programDays = updatedDays,
             programExercises = updatedExercises
         )
         Log.d("ProgramsViewModel", "setProgramDays: applied days=${updatedDays.size}")
         Log.d("ProgramsViewModel", "setProgramDays: final days=${updatedDays.map { it.name }.joinToString()}")
     }
    
    /**
     * Remove day from program builder
     */
    fun removeProgramDay(dayIndex: Int) {
        val currentDays = _builderState.value.programDays.toMutableList()
        if (currentDays.size <= MIN_DAYS) return
        if (dayIndex in currentDays.indices) {
            currentDays.removeAt(dayIndex)
                    // Update day numbers
        currentDays.forEachIndexed { index, day ->
            currentDays[index] = day.copy(dayNumber = index + 1)
        }
            
            _builderState.value = _builderState.value.copy(
                programDays = currentDays
            )
        }
    }

    fun moveProgramDay(fromIndex: Int, toIndex: Int) {
        val days = _builderState.value.programDays.toMutableList()
        if (fromIndex !in days.indices || toIndex !in days.indices || fromIndex == toIndex) return
        val item = days.removeAt(fromIndex)
        days.add(toIndex, item)
        // re-number days
        val renumbered = days.mapIndexed { idx, d -> d.copy(dayNumber = idx + 1) }
        _builderState.value = _builderState.value.copy(programDays = renumbered)
    }

    /** Toggle a day as Rest Day in the builder. When enabled, clear that day's exercises. */
    fun toggleRestDay(dayIndex: Int, isRestDay: Boolean) {
        val current = _builderState.value
        val days = current.programDays.toMutableList()
        val target = days.getOrNull(dayIndex) ?: return

        days[dayIndex] = target.copy(isRestDay = isRestDay)

        val exercisesByDay = current.programExercises.toMutableMap()
        if (isRestDay) {
            // Remove exercises for this day so nothing is shown/saved
            exercisesByDay.remove(target.id)
        }

        _builderState.value = current.copy(
            programDays = days,
            programExercises = exercisesByDay
        )
    }
    
    /**
     * Add exercise to program day
     */
    fun addExerciseToProgramDay(dayIndex: Int, exerciseId: Long) {
        val currentState = _builderState.value
        // Do not allow adding exercises to a rest day
        if (currentState.programDays.getOrNull(dayIndex)?.isRestDay == true) return

        val currentExercises = currentState.programExercises.toMutableMap()
        val dayId = currentState.programDays.getOrNull(dayIndex)?.id ?: return
        
        val existingExercises = currentExercises[dayId]?.toMutableList() ?: mutableListOf()
        val newExercise = ProgramExercise(
            id = 0,
            programDayId = dayId,
            exerciseId = exerciseId,
            sets = "3",
            reps = "10",
            restTimeSeconds = null,
            orderIndex = existingExercises.size
        )
        
        existingExercises.add(newExercise)
        currentExercises[dayId] = existingExercises
        
        _builderState.value = currentState.copy(
            programExercises = currentExercises
        )
    }

    /**
     * Remove exercise from program day
     */
    fun removeExerciseFromProgramDay(dayIndex: Int, exerciseIndex: Int) {
        val currentExercises = _builderState.value.programExercises.toMutableMap()
        val dayId = _builderState.value.programDays.getOrNull(dayIndex)?.id ?: return
        
        val existingExercises = currentExercises[dayId]?.toMutableList() ?: return
        if (exerciseIndex in existingExercises.indices) {
            existingExercises.removeAt(exerciseIndex)
            // Update order indices
            existingExercises.forEachIndexed { index, exercise ->
                existingExercises[index] = exercise.copy(orderIndex = index)
            }
            currentExercises[dayId] = existingExercises
            
            _builderState.value = _builderState.value.copy(
                programExercises = currentExercises
            )
        }
    }

    fun moveExerciseWithinDay(dayIndex: Int, fromIndex: Int, toIndex: Int) {
        val currentState = _builderState.value
        val dayId = currentState.programDays.getOrNull(dayIndex)?.id ?: return
        val list = currentState.programExercises[dayId]?.toMutableList() ?: return
        if (fromIndex !in list.indices) return
        val target = when {
            toIndex < 0 -> 0
            toIndex > list.lastIndex -> list.lastIndex
            else -> toIndex
        }
        if (fromIndex == target) return
        val item = list.removeAt(fromIndex)
        list.add(target, item)
        val renumbered = list.mapIndexed { idx, e -> e.copy(orderIndex = idx) }
        val newMap = currentState.programExercises.toMutableMap()
        newMap[dayId] = renumbered
        _builderState.value = currentState.copy(programExercises = newMap)
    }

    /**
     * Update exercise configuration (sets, reps, rest time)
     */
    fun updateExerciseConfiguration(
        dayIndex: Int,
        exerciseIndex: Int,
        sets: String,
        reps: String,
        restTimeSeconds: Int
    ) {
        val currentState = _builderState.value
        val currentExercises = currentState.programExercises.toMutableMap()
        val dayId = currentState.programDays.getOrNull(dayIndex)?.id ?: return
        
        val existingExercises = currentExercises[dayId]?.toMutableList() ?: return
        if (exerciseIndex !in existingExercises.indices) return
        
        val updatedExercise = existingExercises[exerciseIndex].copy(
            sets = sets,
            reps = reps.ifBlank { null },
            restTimeSeconds = restTimeSeconds,
            tillFailure = reps == "Till Failure"
        )
        
        existingExercises[exerciseIndex] = updatedExercise
        currentExercises[dayId] = existingExercises
        
        _builderState.value = currentState.copy(
            programExercises = currentExercises
        )
    }

    /**
     * Get exercises for a specific program day
     */
    fun getExercisesForDay(dayIndex: Int): List<ProgramExercise> {
        val dayId = _builderState.value.programDays.getOrNull(dayIndex)?.id ?: return emptyList()
        return _builderState.value.programExercises[dayId] ?: emptyList()
    }

    /**
     * Get exercise details by ID
     */
    fun getExerciseById(exerciseId: Long): Exercise? {
        return _availableExercises.value.find { it.id == exerciseId }
    }
    
    /**
     * Validate program builder state and return error message if invalid
     */
    private fun validateProgramBuilder(state: ProgramBuilderState): String? {
        return when {
            state.programName.isBlank() -> "Program name is required"
            state.programType.isBlank() -> "Please select a program type"
            state.programDays.isEmpty() -> "At least one training day is required"
            state.programDays.any { it.name.isBlank() } -> "All training days must have names"
            state.programDays.all { it.isRestDay } -> "At least one training day must have exercises"
            state.programDays.any { !it.isRestDay && state.programExercises[it.id].orEmpty().isEmpty() } -> 
                "All non-rest days must have at least one exercise"
            else -> null
        }
    }

    /**
     * Save the program being built
     */
    fun saveProgramBuilder() {
        val snapshot = _builderState.value
        viewModelScope.launch(ioDispatcher) {
            try {
                val builderState = snapshot
                Log.d(
                    "ProgramsViewModel",
                    "saveProgramBuilder: name='${builderState.programName}', days=${builderState.programDays.size}, totalExercises=${builderState.programExercises.values.sumOf { it.size }}"
                )
                Log.d(
                    "ProgramsViewModel",
                    "saveProgramBuilder: day names=${builderState.programDays.map { it.name }.joinToString()}"
                )
                
                // Validate required fields
                val validationError = validateProgramBuilder(builderState)
                if (validationError != null) {
                    _builderState.value = builderState.copy(error = validationError)
                    return@launch
                }
                
                val editingId = builderState.editingProgramId
                if (editingId == null) {
                    // Create
                    val programId = programRepository.createProgram(
                        name = builderState.programName,
                        description = builderState.programDescription,
                        durationWeeks = if (builderState.durationEnabled) builderState.durationWeeks else null,
                        difficultyLevel = builderState.difficultyLevel,
                        programType = builderState.programType,
                        daysPerWeek = builderState.programDays.size
                    )
                    Log.d("ProgramsViewModel", "saveProgramBuilder: created programId=$programId")
                    // Persist the built structure for this program
                    programRepository.saveProgramStructure(
                        programId = programId,
                        days = builderState.programDays,
                        exercisesByTempDayId = builderState.programExercises
                    )
                    Log.d("ProgramsViewModel", "saveProgramBuilder: structure saved for programId=$programId")
                } else {
                    // Update existing program and replace structure
                    val current = programRepository.getProgramById(editingId)
                    if (current != null) {
                        val updated = current.copy(
                            name = builderState.programName,
                            description = builderState.programDescription,
                            durationWeeks = if (builderState.durationEnabled) builderState.durationWeeks else null,
                            difficultyLevel = builderState.difficultyLevel,
                            programType = builderState.programType,
                            daysPerWeek = builderState.programDays.size,
                            isCustom = true,
                            isTemplate = false
                        )
                        programRepository.updateProgram(updated)
                        programRepository.replaceProgramStructure(
                            programId = editingId,
                            days = builderState.programDays,
                            exercisesByTempDayId = builderState.programExercises
                        )
                        Log.d("ProgramsViewModel", "saveProgramBuilder: updated programId=$editingId")
                    }
                }
                
                // Clear editing flag and mark as saved
                Companion.currentEditingProgramId = null
                _builderState.value = builderState.copy(saved = true)
                
                // Refresh programs list
                loadPrograms()
                
            } catch (e: Exception) {
                Log.e("ProgramsViewModel", "saveProgramBuilder: error", e)
                _builderState.value = _builderState.value.copy(
                    error = e.message ?: "Failed to save program"
                )
            }
        }
    }

    fun saveProgramBuilderAsTemplate() {
        val snapshot = _builderState.value
        viewModelScope.launch(ioDispatcher) {
            try {
                // Validate required fields
                val validationError = validateProgramBuilder(snapshot)
                if (validationError != null) {
                    _builderState.value = snapshot.copy(error = validationError)
                    return@launch
                }
                
                val programId = programRepository.createProgram(
                    name = snapshot.programName.ifBlank { "Program" },
                    description = snapshot.programDescription,
                    durationWeeks = if (snapshot.durationEnabled) snapshot.durationWeeks else null,
                    difficultyLevel = snapshot.difficultyLevel,
                    programType = snapshot.programType,
                    daysPerWeek = snapshot.programDays.size
                )
                // Mark program as template
                programRepository.updateProgram(
                    com.chilluminati.rackedup.data.database.entity.Program(
                        id = programId,
                        name = snapshot.programName.ifBlank { "Program" },
                        description = snapshot.programDescription,
                        durationWeeks = if (snapshot.durationEnabled) snapshot.durationWeeks else null,
                        difficultyLevel = snapshot.difficultyLevel,
                        programType = snapshot.programType,
                        daysPerWeek = snapshot.programDays.size,
                        isTemplate = true,
                        isCustom = false
                    )
                )
                programRepository.saveProgramStructure(programId, snapshot.programDays, snapshot.programExercises)
                Companion.currentEditingProgramId = null
                _builderState.value = snapshot.copy(saved = true)
                loadPrograms()
            } catch (e: Exception) {
                _builderState.value = _builderState.value.copy(error = e.message ?: "Failed to save template")
            }
        }
    }
    
    /**
     * Cancel program building
     */
    fun cancelProgramBuilder() {
        Log.d("ProgramsViewModel", "cancelProgramBuilder: Called - this will reset builder state!")
        Companion.currentEditingProgramId = null
        _builderState.value = ProgramBuilderState()
    }
    
    fun getCurrentEditingProgramId(): Long? = Companion.currentEditingProgramId
    
    fun restoreEditStateIfNeeded() {
        val editId = Companion.currentEditingProgramId
        if (editId != null && (_builderState.value.editingProgramId == null || _builderState.value.programName.isBlank())) {
            Log.d("ProgramsViewModel", "restoreEditStateIfNeeded: Restoring edit state for programId=$editId")
            beginEditProgram(editId)
        }
    }
    
    /**
     * Delete a custom program
     */
    fun deleteProgram(program: Program) {
        viewModelScope.launch(ioDispatcher) {
            try {
                programRepository.deleteProgram(program)
                loadPrograms() // Refresh
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to delete program"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
        _builderState.value = _builderState.value.copy(error = null)
    }

    fun clearSaved() {
        _builderState.value = _builderState.value.copy(saved = false)
    }
    
    fun refreshPrograms() {
        loadPrograms()
    }

    fun consumeLastCreatedWorkoutId() {
        _uiState.value = _uiState.value.copy(lastCreatedWorkoutId = null)
    }

    fun setActiveProgram(program: Program) {
        viewModelScope.launch(ioDispatcher) {
            try {
                // Ensure start date is captured for achievements
                programRepository.startProgram(program.id)
                loadPrograms()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to set active program"
                )
            }
        }
    }

    fun finishActiveProgram(programId: Long) {
        viewModelScope.launch(ioDispatcher) {
            try {
                programRepository.finishProgram(programId)
                loadPrograms()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to finish program"
                )
            }
        }
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedProgram = null,
            selectedProgramDays = emptyList(),
            selectedProgramExercises = emptyMap()
        )
    }

    fun beginEditProgram(programId: Long) {
        Log.d("ProgramsViewModel", "beginEditProgram: Starting edit for programId=$programId")
        
        // Store the editing program ID to survive navigation/resets
        Companion.currentEditingProgramId = programId
        
        // Set editing state immediately to prevent race condition
        _builderState.value = ProgramBuilderState(
            isCreating = true,
            editingProgramId = programId
        )
        Log.d("ProgramsViewModel", "beginEditProgram: Set initial editing state")
        
        viewModelScope.launch(ioDispatcher) {
            try {
                Log.d("ProgramsViewModel", "beginEditProgram: Loading program data...")
                val program = programRepository.getProgramById(programId)
                if (program == null) {
                    Log.e("ProgramsViewModel", "beginEditProgram: Program not found for id=$programId")
                    return@launch
                }
                Log.d("ProgramsViewModel", "beginEditProgram: Loaded program: name='${program.name}', type='${program.programType}', difficulty='${program.difficultyLevel}'")
                
                val days = programRepository.getProgramDays(programId)
                Log.d("ProgramsViewModel", "beginEditProgram: Loaded ${days.size} days: ${days.map { "Day ${it.dayNumber}: ${it.name}" }}")

                val exercisesByDay: MutableMap<Long, List<ProgramExercise>> = mutableMapOf()
                days.forEach { day ->
                    val ex = programRepository.getProgramExercises(day.id)
                    exercisesByDay[day.id] = ex
                    Log.d("ProgramsViewModel", "beginEditProgram: Day ${day.dayNumber} has ${ex.size} exercises")
                }

                val finalState = ProgramBuilderState(
                    isCreating = true,
                    programName = program.name,
                    programDescription = program.description ?: "",
                    difficultyLevel = program.difficultyLevel,
                    programType = program.programType,
                    durationWeeks = program.durationWeeks ?: 8,
                    durationEnabled = program.durationWeeks != null,
                    programDays = days.sortedBy { it.dayNumber },
                    programExercises = exercisesByDay,
                    editingProgramId = program.id
                )
                
                Log.d("ProgramsViewModel", "beginEditProgram: Setting final state with name='${finalState.programName}', days=${finalState.programDays.size}")
                _builderState.value = finalState
                Log.d("ProgramsViewModel", "beginEditProgram: State updated successfully")
                
            } catch (e: Exception) {
                Log.e("ProgramsViewModel", "beginEditProgram: Error loading program", e)
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load program for editing"
                )
            }
        }
    }

    fun startFromTemplate(templateId: String) {
        val template = ProgramTemplatesSystem.getTemplateById(templateId) ?: return
        // Prepare resolver that maps common synonyms and performs fuzzy lookup
        val resolver: (String) -> Exercise? = { raw -> resolveExercise(raw) }
        val tempIdSeed = -1L
        var nextId = tempIdSeed
        val days = template.days.mapIndexed { index, d ->
            val isRest = d.exercises.isEmpty() || d.name.contains("rest", ignoreCase = true)
            ProgramDay(
                id = nextId--,
                programId = 0,
                dayNumber = index + 1,
                weekNumber = 1,
                name = d.name,
                isRestDay = isRest
            )
        }
        val map: MutableMap<Long, List<ProgramExercise>> = mutableMapOf()
        days.zip(template.days).forEach { (day, dayTemplate) ->
            val list = if (day.isRestDay) emptyList() else dayTemplate.exercises.mapIndexedNotNull { idx, exT ->
                val resolved = resolver(exT.exerciseName) ?: return@mapIndexedNotNull null
                val (normSets, normReps) = normalizeSetsAndReps(exT.sets, exT.reps)
                ProgramExercise(
                    id = 0,
                    programDayId = day.id,
                    exerciseId = resolved.id,
                    orderIndex = idx,
                    sets = normSets,
                    reps = normReps,
                    weightPercentage = exT.weightPercentage,
                    restTimeSeconds = exT.restTimeSeconds,
                    rpeTarget = exT.rpeTarget,
                    progressionScheme = exT.progressionScheme,
                    progressionIncrement = exT.progressionIncrement,
                    isSuperset = false,
                    supersetId = null,
                    notes = exT.notes
                )
            }
            map[day.id] = list
        }
        _builderState.value = ProgramBuilderState(
            isCreating = true,
            programName = template.name,
            programDescription = template.description,
            difficultyLevel = template.difficultyLevel,
            programType = template.programType,
            durationWeeks = template.durationWeeks,
            durationEnabled = false,
            programDays = days,
            programExercises = map,
            editingProgramId = null
        )
    }

    /** Create and save a user-customizable program copied from a template into My Programs. */
    fun createProgramFromTemplate(templateId: String) {
        val template = ProgramTemplatesSystem.getTemplateById(templateId) ?: return
        val resolver: (String) -> Exercise? = { raw -> resolveExercise(raw) }
        viewModelScope.launch(ioDispatcher) {
            try {
                // Build temp structure
                var nextId = -1L
                val days = template.days.mapIndexed { index, d ->
                    val isRest = d.exercises.isEmpty() || d.name.contains("rest", ignoreCase = true)
                    ProgramDay(
                        id = nextId--,
                        programId = 0,
                        dayNumber = index + 1,
                        weekNumber = 1,
                        name = d.name,
                        isRestDay = isRest
                    )
                }
                val map: MutableMap<Long, List<ProgramExercise>> = mutableMapOf()
                days.zip(template.days).forEach { (day, dayTemplate) ->
                    val exercises: List<ProgramExercise> = if (days.first { it.id == day.id }.isRestDay) emptyList() else dayTemplate.exercises.mapIndexedNotNull { idx, exT ->
                        val resolved = resolver(exT.exerciseName) ?: return@mapIndexedNotNull null
                        val (normSets, normReps) = normalizeSetsAndReps(exT.sets, exT.reps)
                        ProgramExercise(
                            id = 0,
                            programDayId = day.id,
                            exerciseId = resolved.id,
                            orderIndex = idx,
                            sets = normSets,
                            reps = normReps,
                            weightPercentage = exT.weightPercentage,
                            restTimeSeconds = exT.restTimeSeconds,
                            rpeTarget = exT.rpeTarget,
                            progressionScheme = exT.progressionScheme,
                            progressionIncrement = exT.progressionIncrement,
                            isSuperset = false,
                            supersetId = null,
                            notes = exT.notes
                        )
                    }
                    map[day.id] = exercises
                }

                // Create program entry
                val programId = programRepository.createProgram(
                    name = template.name,
                    description = template.description,
                    durationWeeks = template.durationWeeks,
                    difficultyLevel = template.difficultyLevel,
                    programType = template.programType,
                    daysPerWeek = days.size
                )

                // Persist structure
                programRepository.saveProgramStructure(programId, days, map)

                // Refresh UI
                loadPrograms()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to create program from template"
                )
            }
        }
    }

    /** Resolve an exercise name to an existing exercise in our DB using synonyms and fuzzy matching. */
    private fun resolveExercise(rawName: String): Exercise? {
        val all = _availableExercises.value
        if (all.isEmpty()) return null

        fun normalize(s: String): String = s.trim().lowercase()
            .replace("-", " ")
            .replace("×", "x")
            .replace("&", "and")
            .replace(Regex("\\s+"), " ")

        val nameNorm = normalize(rawName)
        val byExact = all.firstOrNull { normalize(it.name) == nameNorm }
        if (byExact != null) return byExact

        val synonyms = mapOf(
            // Existing synonyms
            "ohp" to "overhead press",
            "press" to "overhead press",
            "pullups" to "pull-ups",
            "chinups" to "pull-ups",
            "lat pulldowns" to "lat pulldown",
            "row" to "barbell row",
            "bb row" to "barbell row",
            "rdl" to "romanian deadlift",
            "back extension" to "back extension",
            "hyperextension" to "back extension",
            "hamstring curl" to "leg curl",
            "leg curls" to "leg curl",
            "calf raise" to "calf raise",
            "calf raises" to "calf raise",
            "hanging leg raise" to "hanging leg raise",
            "leg raise" to "hanging leg raise",
            "db row" to "dumbbell row",
            "incline db press" to "incline dumbbell press",
            "pushups" to "push-ups",
            "bodyweight squat" to "bodyweight squat",
            "pike pushups" to "pike push-ups",
            "tricep pushdown" to "tricep pushdown",
            
            // Additional synonyms for template exercises
            "pull-ups" to "pull up",
            "push-ups" to "push up",
            "chin-ups" to "pull up",
            "triceps pushdowns" to "tricep pushdown",
            "triceps pushdown" to "tricep pushdown",
            "lateral raises" to "lateral raise",
            "lateral raise" to "lateral raise",
            "face pull" to "face pull",
            "dumbbell curl" to "bicep curl",
            "hammer curl" to "hammer curl",
            "barbell curl" to "bicep curl",
            "walking lunge" to "lunge",
            "bulgarian split squat" to "bulgarian split squat",
            "bulgarian split squats" to "bulgarian split squat",
            "incline dumbbell press" to "incline dumbbell press",
            "incline bench press" to "incline bench press",
            "front squat" to "front squat",
            "leg press" to "leg press",
            "skullcrusher" to "lying tricep extension",
            "dips" to "dip",
            "bench dips" to "dip",
            "diamond push-ups" to "diamond push up",
            "pike push-ups" to "pike push up",
            "inverted rows" to "inverted row",
            "towel rows" to "inverted row",
            "superman hold" to "superman",
            "hollow body hold" to "hollow body hold",
            "squats" to "squat",
            "glute bridge" to "glute bridge",
            "calf raises" to "calf raise",
            "squat (5/3/1 sets)" to "squat",
            "deadlift (5/3/1 sets)" to "deadlift",
            "bench press (5/3/1 sets)" to "bench press",
            "overhead press (5/3/1 sets)" to "overhead press"
        )

        val canonical = synonyms[nameNorm] ?: nameNorm
        val byCanonical = all.firstOrNull { normalize(it.name) == canonical }
        if (byCanonical != null) return byCanonical

        // Fuzzy: startsWith / contains any key tokens
        val tokens = nameNorm.split(" ").filter { it.isNotBlank() }
        val byTokens = all.firstOrNull { e ->
            val en = normalize(e.name)
            tokens.all { t -> en.contains(t) }
        }
        if (byTokens != null) return byTokens

        // Fallback: partial contains by main head noun
        val head = tokens.lastOrNull() ?: nameNorm
        val result = all.firstOrNull { normalize(it.name).contains(head) }
        
        // Log when exercise resolution fails to help with debugging
        if (result == null) {
            Log.w("ProgramsViewModel", "Failed to resolve exercise: '$rawName' (normalized: '$nameNorm')")
            Log.d("ProgramsViewModel", "Available exercises: ${all.take(5).map { it.name }}")
        } else {
            Log.d("ProgramsViewModel", "Resolved '$rawName' -> '${result.name}'")
        }
        
        return result
    }

    /**
     * Normalize template-provided sets/reps specs like "3x5", "4x6-8" into separate fields.
     * - Sets becomes just the count (e.g., 3, 4)
     * - Reps becomes a single number; for ranges, choose median (floor of average)
     */
    private fun normalizeSetsAndReps(setsSpec: String, repsSpec: String?): Pair<String, String> {
        val setsCount = extractLeadingInt(setsSpec) ?: 3
        // Prefer explicit reps, else take part after 'x' in setsSpec
        val repsValue = extractRepsInt(repsSpec)
            ?: extractRepsInt(afterX(setsSpec))
            ?: 10
        return setsCount.toString() to repsValue.toString()
    }

    private fun extractLeadingInt(text: String?): Int? {
        if (text.isNullOrBlank()) return null
        return Regex("(\\d+)").find(text)?.groupValues?.getOrNull(1)?.toIntOrNull()
    }

    private fun afterX(text: String?): String? {
        if (text.isNullOrBlank()) return null
        val parts = text.lowercase().split('x')
        return if (parts.size >= 2) parts[1] else null
    }

    private fun extractRepsInt(spec: String?): Int? {
        if (spec.isNullOrBlank()) return null
        val s = spec.lowercase()
        // Range like 6-8, 30-60s -> median
        val range = Regex("(\\d+)\\s*-\\s*(\\d+)").find(s)
        if (range != null) {
            val a = range.groupValues.getOrNull(1)?.toIntOrNull()
            val b = range.groupValues.getOrNull(2)?.toIntOrNull()
            if (a != null && b != null) return (a + b) / 2
        }
        // Single number (ignore trailing non-digits like 's')
        return Regex("(\\d+)").find(s)?.groupValues?.getOrNull(1)?.toIntOrNull()
    }
}

/**
 * UI state for programs screen
 */
data class ProgramsUiState(
    val userPrograms: List<Program> = emptyList(),
    val templatePrograms: List<Program> = emptyList(),
    val selectedProgram: Program? = null,
    val selectedProgramDays: List<ProgramDay> = emptyList(),
    val selectedProgramExercises: Map<ProgramDay, List<ProgramExercise>> = emptyMap(),
    // Map of ProgramDay.id -> last completed date for that day (if any)
    val selectedProgramLastCompletedByDay: Map<Long, java.util.Date> = emptyMap(),
    // The ProgramDay.id that was most recently completed across the program (if any)
    val selectedProgramMostRecentCompletedDayId: Long? = null,
    val activeProgram: Program? = null,
    val activeProgramLastCompleted: String? = null,
    val lastCreatedWorkoutId: Long? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * State for program builder
 */
data class ProgramBuilderState(
    val isCreating: Boolean = false,
    val programName: String = "",
    val programDescription: String = "",
    val difficultyLevel: String = "Beginner",
    val programType: String = "",
    val durationWeeks: Int = 8,
    val durationEnabled: Boolean = false,
    val programDays: List<ProgramDay> = emptyList(),
    val programExercises: Map<Long, List<ProgramExercise>> = emptyMap(),
    // If not null, we're editing an existing program and should update/replace instead of creating
    val editingProgramId: Long? = null,
    val error: String? = null,
    val saved: Boolean = false
)
