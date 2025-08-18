package com.chilluminati.rackedup.presentation.workouts

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.data.database.entity.Exercise
import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.data.database.entity.WorkoutExercise
import com.chilluminati.rackedup.presentation.components.FullScreenImageDialog
import com.chilluminati.rackedup.presentation.programs.ExercisePreviewSheet
import com.chilluminati.rackedup.presentation.workouts.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

/**
 * Active workout session screen with live tracking
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    workoutId: Long? = null,
    onNavigateBack: () -> Unit,
    onWorkoutComplete: () -> Unit,
    onNavigateToExerciseLibrary: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val weightUnit = uiState.weightUnit
    val activeWorkoutState by viewModel.activeWorkoutState.collectAsStateWithLifecycle()
    
    // Timer states (restore from persisted session if present)
    val persistedSession by viewModel.activeSessionFlow.collectAsStateWithLifecycle(initialValue = null)
    var workoutTimer by remember { mutableIntStateOf(0) }
    var restTimer by remember { mutableIntStateOf(0) }
    var isResting by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    
    // Update timer states when screen initially composes or workoutId changes,
    // and "catch up" elapsed time. Avoid reacting to every DataStore tick to prevent jitter.
    LaunchedEffect(persistedSession?.workoutId) {
        persistedSession?.let { session ->
            var newElapsed = session.workoutElapsedSeconds
            var newRest = session.restRemainingSeconds

            // If not paused, derive the delta from lastTickEpochMs so timer keeps running in background
            if (!session.isPaused && session.lastTickEpochMs > 0L) {
                val deltaSeconds = ((System.currentTimeMillis() - session.lastTickEpochMs) / 1000L).toInt().coerceAtLeast(0)
                newElapsed += deltaSeconds
                if (session.isResting) {
                    newRest = (session.restRemainingSeconds - deltaSeconds).coerceAtLeast(0)
                }
            }

            workoutTimer = newElapsed
            restTimer = newRest
            isResting = session.isResting && newRest > 0
            isPaused = session.isPaused

            // Persist the caught-up values once so subsequent resumes are consistent
            viewModel.updateSessionTimers(
                workoutElapsedSeconds = newElapsed,
                isResting = isResting,
                restRemainingSeconds = newRest
            )
        }
    }
    
    // Start workout timer (robust to state changes)
    LaunchedEffect(activeWorkoutState.isActive, isPaused) {
        while (activeWorkoutState.isActive) {
            if (!isPaused) {
                delay(1000)
                workoutTimer++
                viewModel.updateSessionTimers(workoutElapsedSeconds = workoutTimer)
            } else {
                // Poll less frequently while paused to avoid tight loop
                delay(250)
            }
        }
    }
    
    // Rest timer countdown
    LaunchedEffect(isResting, restTimer) {
        if (isResting && restTimer > 0) {
            while (isResting && restTimer > 0) {
                delay(1000)
                restTimer--
                viewModel.updateSessionTimers(
                    isResting = true,
                    restRemainingSeconds = restTimer
                )
            }
            if (restTimer == 0) {
                isResting = false
                viewModel.onRestTimerComplete()
            }
        }
    }
    
    // Initialize workout if needed
    LaunchedEffect(workoutId) {
        if (workoutId != null && !activeWorkoutState.isActive) {
            // Check if there's already an active session for this workout
            val existingSession = viewModel.activeSessionFlow.first()
            if (existingSession?.workoutId == workoutId) {
                // Resume existing workout (preserve timer)
                viewModel.resumeWorkout(workoutId)
            } else {
                // Start new workout
                viewModel.startWorkout(workoutId)
            }
        } else if (workoutId == null && !activeWorkoutState.isActive) {
            // Create a quick workout
            viewModel.createAndStartWorkout("Quick Workout")
        } else if (workoutId != null && activeWorkoutState.isActive && activeWorkoutState.currentWorkout?.id != workoutId) {
            // If we have a different workout ID than the current active workout, start the new one
            viewModel.startWorkout(workoutId)
        }
    }

    // Handle selected exercise from library
    val navController = androidx.navigation.compose.rememberNavController()
    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.get<Long>("selected_exercise_id")?.let { exerciseId ->
            // Clear the saved state to avoid re-adding on config changes
            navController.currentBackStackEntry?.savedStateHandle?.remove<Long>("selected_exercise_id")
            // Add the exercise to the workout
            viewModel.addExerciseToWorkout(exerciseId)
        }
    }

    val ime = WindowInsets.ime
    val density = LocalDensity.current
    val isKeyboardVisible by remember(ime, density) {
        derivedStateOf { ime.getBottom(density) > 0 }
    }
    
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    
    // Note: Avoid auto-scrolling on keyboard show; it caused jumpiness when focusing inputs

    Box(modifier = modifier.fillMaxSize()) {
        // Rest Timer Overlay
        AnimatedVisibility(
            visible = isResting,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 64.dp) // Height of TopAppBar + some spacing
                .padding(horizontal = 16.dp)
                .zIndex(1f)
        ) {
            RestTimerCard(
                restTimer = restTimer,
                onTimerComplete = {
                    isResting = false
                    viewModel.updateSessionTimers(isResting = false, restRemainingSeconds = 0)
                },
                onAddTime = { seconds ->
                    restTimer += seconds
                    viewModel.updateSessionTimers(restRemainingSeconds = restTimer)
                }
            )
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Active Workout") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                // Finish the workout first, then navigate
                                viewModel.finishWorkout()
                                onWorkoutComplete()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(
                                text = "Finish",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .windowInsetsPadding(
                        if (isKeyboardVisible) {
                            WindowInsets.ime
                        } else {
                            WindowInsets(0, 0, 0, 0)
                        }
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                            keyboard?.hide()
                        })
                    },
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = listState
            ) {
                // Workout Timer Header
                item {
                    WorkoutTimerCard(
                        workoutTimer = workoutTimer,
                        isPaused = isPaused,
                        onPauseClick = {
                            isPaused = !isPaused
                            viewModel.updateSessionTimers(isPaused = isPaused)
                        }
                    )
                }

            // Current Workout Summary
            item {
                CurrentWorkoutSummary(activeWorkoutState = activeWorkoutState, weightUnit = weightUnit)
            }

            // Exercise List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
            Text(
                text = "Exercises",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
                    IconButton(onClick = onNavigateToExerciseLibrary) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_exercise)
                        )
                    }
                }
            }

            // Workout exercises from ViewModel
            itemsIndexed(
                items = activeWorkoutState.workoutExercises,
                key = { _, ex -> ex.id }
            ) { _, workoutExercise ->
                ActiveExerciseCard(
                    workoutExercise = workoutExercise,
                    exerciseSets = activeWorkoutState.exerciseSets[workoutExercise.id] ?: emptyList(),
                    exerciseDetails = activeWorkoutState.exerciseDetails,
                    isCompleted = false, // TODO: Determine completion logic
                    weightUnit = weightUnit,
                    defaultRestSeconds = uiState.defaultRestSeconds,
                                    onStartRest = { restTimeSeconds: Int ->
                    isResting = true
                    restTimer = restTimeSeconds
                    viewModel.updateSessionTimers(isResting = true, restRemainingSeconds = restTimer)
                },
                onAddSet = { weight: Double?, reps: Int?, durationSeconds: Int? ->
                    viewModel.addSet(
                        workoutExerciseId = workoutExercise.id,
                        weight = weight,
                        reps = reps,
                        durationSeconds = durationSeconds
                    )
                },
                onUpdateSet = { set: ExerciseSet ->
                    viewModel.updateSet(set)
                },
                onDeleteExercise = { exercise: WorkoutExercise ->
                    // TODO: Implement delete exercise functionality
                },
                onDeleteSet = { set: ExerciseSet ->
                    viewModel.deleteSet(set)
                }
                )
            }

            // Add Exercise Button
            item {
                OutlinedButton(
                    onClick = onNavigateToExerciseLibrary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.add_exercise))
                }
            }
        }
    }
}

@Composable
fun WorkoutTimerCard(
    workoutTimer: Int,
    isPaused: Boolean,
    onPauseClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isPaused) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isPaused) "Workout Paused" else "Workout Time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isPaused) 
                        MaterialTheme.colorScheme.onSurfaceVariant 
                    else 
                        MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${workoutTimer / 60}:${String.format("%02d", workoutTimer % 60)}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isPaused) 
                        MaterialTheme.colorScheme.onSurfaceVariant 
                    else 
                        MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
                IconButton(onClick = onPauseClick) {
                    Icon(
                        imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (isPaused) "Resume timer" else "Pause timer",
                        tint = if (isPaused) 
                            MaterialTheme.colorScheme.onSurfaceVariant 
                        else 
                            MaterialTheme.colorScheme.onPrimaryContainer
                    )
            }
        }
    }
}

@Composable
fun RestTimerCard(
    restTimer: Int,
    onTimerComplete: () -> Unit,
    onAddTime: (Int) -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (restTimer <= 10) 
                MaterialTheme.colorScheme.errorContainer 
            else 
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rest Timer",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (restTimer <= 10) 
                    MaterialTheme.colorScheme.onErrorContainer 
                else 
                    MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${restTimer / 60}:${String.format("%02d", restTimer % 60)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = if (restTimer <= 10) 
                    MaterialTheme.colorScheme.onErrorContainer 
                else 
                    MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = { onAddTime(30) }) {
                    Text("Add 30s")
                }
                TextButton(onClick = { onAddTime(60) }) {
                    Text("Add 1m")
                }
                TextButton(onClick = onTimerComplete) {
                    Text("Skip")
                }
            }
        }
    }
}

@Composable
fun CurrentWorkoutSummary(
    activeWorkoutState: ActiveWorkoutState,
    weightUnit: String
) {
    val totalSets = activeWorkoutState.exerciseSets.values.sumOf { it.size }
    val totalVolume = activeWorkoutState.exerciseSets.values.flatten()
        .filter { it.isCompleted }
        .sumOf { set ->
            val weight = set.weight ?: 0.0
            val reps = set.reps ?: 0
            weight * reps
        }
    val totalExercises = activeWorkoutState.workoutExercises.size
    val completedExercises = activeWorkoutState.workoutExercises.count { exercise ->
        val sets = activeWorkoutState.exerciseSets[exercise.id] ?: emptyList()
        sets.isNotEmpty() && sets.all { it.isCompleted }
    }
    val completedSets = activeWorkoutState.exerciseSets.values.flatten().count { it.isCompleted }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Program title and day
            val workoutName = activeWorkoutState.currentWorkout?.name ?: "Quick Workout"
            val nameParts = workoutName.split(" - ")
            val programTitle = nameParts.firstOrNull() ?: workoutName
            
            // Use the program day name from the database if available, otherwise fallback to parsing
            val finalProgramDay = activeWorkoutState.programDayName ?: run {
                val programDay = nameParts.getOrNull(1)
                if (programDay == null && activeWorkoutState.currentWorkout?.programDayId != null) {
                    // Fallback parsing for existing workouts
                    val words = workoutName.split(" ")
                    if (words.size >= 3 && words.last().equals("Day", ignoreCase = true)) {
                        val dayNumber = words.dropLast(1).lastOrNull()?.toIntOrNull()
                        if (dayNumber != null && dayNumber in 1..7) {
                            "Day $dayNumber"
                        } else {
                            words.dropLast(1).joinToString(" ")
                        }
                    } else {
                        null
                    }
                } else {
                    programDay
                }
            }
            
            Text(
                text = programTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Show program day if available
                    if (finalProgramDay != null) {
                        Text(
                            text = finalProgramDay,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "$completedExercises/$totalExercises Exercises Complete",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$completedSets/$totalSets Sets Complete",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${totalVolume.toInt()} $weightUnit Volume",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
@Suppress("UNUSED_PARAMETER")
fun ActiveExerciseCard(
    workoutExercise: WorkoutExercise,
    exerciseSets: List<ExerciseSet>,
    exerciseDetails: Map<Long, Exercise>,
    isCompleted: Boolean,
    weightUnit: String,
    defaultRestSeconds: Int,
    onStartRest: (Int) -> Unit,
    onAddSet: (Double?, Int?, Int?) -> Unit,
    onUpdateSet: (ExerciseSet) -> Unit,
    onDeleteExercise: (WorkoutExercise) -> Unit,
    onDeleteSet: (ExerciseSet) -> Unit
) {
    val exercise = exerciseDetails[workoutExercise.exerciseId]
    val exerciseName = exercise?.name ?: "Exercise ${workoutExercise.exerciseId}"
    
    // Delete confirmation state
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showFinalConfirmation by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(5) }
    var previewExercise by remember { mutableStateOf<Exercise?>(null) }
    
    // Countdown timer for final confirmation
    LaunchedEffect(showFinalConfirmation) {
        if (showFinalConfirmation) {
            countdown = 5
            while (countdown > 0) {
                delay(1000)
                countdown--
            }
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            else 
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCompleted) 0.dp else 2.dp
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exerciseName,
                    style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    showDeleteDialog = true
                                },
                                onTap = {
                                    previewExercise = exercise
                                }
                            )
                        }
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        // Progress chip
                        Surface(
                            color = if (isCompleted)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else
                                MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "${exerciseSets.count { it.isCompleted }}/${exerciseSets.size} Sets",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isCompleted)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        
                        // Volume chip
                        val volume = exerciseSets.sumOf { set ->
                            if (set.isCompleted) {
                                (set.weight ?: 0.0) * (set.reps ?: 0)
                            } else 0.0
                        }
                        Surface(
                            color = if (isCompleted)
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                            else
                                MaterialTheme.colorScheme.tertiaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "${volume.toInt()} $weightUnit",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isCompleted)
                                    MaterialTheme.colorScheme.tertiary
                                else
                                    MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Show all sets
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    exerciseSets.forEach { set ->
                    SetRow(
                            set = set,
                            setNumber = set.setNumber,
                            weight = set.weight?.toInt()?.toString() ?: "",
                            reps = set.reps?.toString() ?: "",
                        isCompleted = set.isCompleted,
                        weightUnit = weightUnit,
                        defaultRestSeconds = defaultRestSeconds,
                        onComplete = {
                            onUpdateSet(set.copy(isCompleted = !set.isCompleted))
                        },
                        onUpdateSet = { weight, reps ->
                            onUpdateSet(set.copy(
                                weight = weight.toDoubleOrNull(),
                                reps = reps.toIntOrNull()
                            ))
                        },
                        onStartRest = onStartRest,
                        onDeleteSet = onDeleteSet
                    )
                }
                
                // Add Set button at the bottom
                TextButton(
                    onClick = { onAddSet(null, null, null) },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Set", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        
        // Delete confirmation dialogs
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Exercise") },
                text = { Text("Are you sure you want to delete \"$exerciseName\"? This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            showFinalConfirmation = true
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
        
        if (showFinalConfirmation) {
            AlertDialog(
                onDismissRequest = { 
                    showFinalConfirmation = false
                    countdown = 5
                },
                title = { Text("Final Confirmation") },
                text = { 
                    Text(
                        if (countdown > 0) {
                            "Are you absolutely sure? Click OK in $countdown seconds to confirm deletion."
                        } else {
                            "Click OK to permanently delete \"$exerciseName\"."
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (countdown <= 0) {
                                onDeleteExercise(workoutExercise)
                                showFinalConfirmation = false
                            }
                        },
                        enabled = countdown <= 0,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                            disabledContentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.38f)
                        )
                    ) {
                        Text(if (countdown > 0) "Delete ($countdown)" else "Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { 
                            showFinalConfirmation = false
                            countdown = 5
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        val preview = previewExercise
        if (preview != null) {
            ExercisePreviewSheet(
                exercise = preview,
                onDismiss = { previewExercise = null }
            )
        }
    }
}

@Composable
fun SetRow(
    set: ExerciseSet,
    setNumber: Int,
    weight: String,
    reps: String,
    isCompleted: Boolean,
    weightUnit: String,
    defaultRestSeconds: Int,
    onComplete: () -> Unit,
    onUpdateSet: (String, String) -> Unit,
    onStartRest: (Int) -> Unit,
    onDeleteSet: (ExerciseSet) -> Unit
) {
    var currentWeight by remember(weight) { mutableStateOf(if (weight.isBlank()) "0" else weight) }
    var currentReps by remember(reps) { mutableStateOf(reps) }
    var showDeleteMode by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(5) }
    
    val focusManager = LocalFocusManager.current
    val weightFocusRequester = remember { FocusRequester() }
    val repsFocusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    
    // Countdown timer for delete confirmation
    LaunchedEffect(showDeleteDialog) {
        if (showDeleteDialog) {
            countdown = 5
            while (countdown > 0) {
                delay(1000)
                countdown--
            }
        }
    }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            // Only allow swipe from right to left (EndToStart)
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                showDeleteDialog = true
                false
            } else {
                false
            }
        },
        positionalThreshold = { totalDistance -> totalDistance * 0.85f }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete set",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        enableDismissFromStartToEnd = false,
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                // Clear focus when tapping outside input fields
                                focusManager.clearFocus()
                            }
                        )
                    },
                color = if (isCompleted)
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                else
                    MaterialTheme.colorScheme.surface,
                shape = RectangleShape
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Set number or delete button
                    Surface(
                        color = if (showDeleteMode)
                            MaterialTheme.colorScheme.errorContainer
                        else if (isCompleted)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else
                            MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { showDeleteMode = true },
                                onTap = {
                                    if (showDeleteMode) {
                                        showDeleteDialog = true
                                    }
                                }
                            )
                        }
                    ) {
                        if (showDeleteMode) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Delete set",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .width(32.dp)
                                    .padding(vertical = 4.dp)
                            )
                        } else {
                            Text(
                                text = "$setNumber",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier
                                    .width(32.dp)
                                    .padding(vertical = 4.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Delete confirmation dialog
                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = {
                                showDeleteDialog = false
                                showDeleteMode = false
                                countdown = 5
                            },
                            title = { Text("Delete Set") },
                            text = {
                                Text(
                                    if (countdown > 0) {
                                        "Are you sure you want to delete set $setNumber? Click Delete in $countdown seconds to confirm."
                                    } else {
                                        "Are you sure you want to delete set $setNumber?"
                                    }
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        if (countdown <= 0) {
                                            onDeleteSet(set)
                                            showDeleteDialog = false
                                            showDeleteMode = false
                                        }
                                    },
                                    enabled = countdown <= 0,
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error,
                                        disabledContentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.38f)
                                    )
                                ) {
                                    Text(if (countdown > 0) "Delete ($countdown)" else "Delete")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        showDeleteDialog = false
                                        showDeleteMode = false
                                        countdown = 5
                                    }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }

                    // Weight input
                    OutlinedTextField(
                        value = currentWeight,
                        onValueChange = {
                            // Only allow whole numbers and prevent leading zeros
                            val filtered = it.filter { char -> char.isDigit() }
                            val cleaned = if (filtered.startsWith("0") && filtered.length > 1) {
                                filtered.substring(1)
                            } else {
                                filtered
                            }
                            currentWeight = cleaned
                            onUpdateSet(cleaned, currentReps)
                        },
                        placeholder = { Text("0") },
                        suffix = { Text(" $weightUnit") },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(weightFocusRequester),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboard?.hide()
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    // Reps input
                    OutlinedTextField(
                        value = currentReps,
                        onValueChange = {
                            // Only allow whole numbers and prevent leading zeros
                            val filtered = it.filter { char -> char.isDigit() }
                            val cleaned = if (filtered.startsWith("0") && filtered.length > 1) {
                                filtered.substring(1)
                            } else {
                                filtered
                            }
                            currentReps = cleaned
                            onUpdateSet(currentWeight, cleaned)
                        },
                        placeholder = { Text("reps") },
                        suffix = {
                            // Always show explicit unit to avoid ambiguity
                            Text(" reps")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(repsFocusRequester),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboard?.hide()
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    // Modern complete toggle button
                    if (isCompleted) {
                        androidx.compose.material3.FilledIconButton(
                            onClick = {
                                onComplete()
                                focusManager.clearFocus()
                                keyboard?.hide()
                            },
                            modifier = Modifier.size(40.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Unmark set"
                            )
                        }
                    } else {
                        androidx.compose.material3.OutlinedIconButton(
                            onClick = {
                                onComplete()
                                onStartRest(defaultRestSeconds)
                                focusManager.clearFocus()
                                keyboard?.hide()
                            },
                            modifier = Modifier.size(40.dp),
                            colors = IconButtonDefaults.outlinedIconButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Complete set"
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SetInputRow(
    setNumber: Int,
    weightUnit: String,
    onStartRest: (Int) -> Unit,
    onAddSet: (Double?, Int?, Int?) -> Unit
) {
    var weight by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set $setNumber:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(60.dp)
        )
        
        OutlinedTextField(
            value = weight,
            onValueChange = { 
                // Only allow whole numbers and prevent leading zeros
                val filtered = it.filter { char -> char.isDigit() }
                val cleaned = if (filtered.startsWith("0") && filtered.length > 1) {
                    filtered.substring(1)
                } else {
                    filtered
                }
                weight = cleaned
            },
            label = { Text("Weight") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            suffix = { Text(weightUnit) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        
                    OutlinedTextField(
            value = reps,
            onValueChange = { 
                // Only allow whole numbers and prevent leading zeros
                val filtered = it.filter { char -> char.isDigit() }
                val cleaned = if (filtered.startsWith("0") && filtered.length > 1) {
                    filtered.substring(1)
                } else {
                    filtered
                }
                reps = cleaned
            },
            label = { Text("Reps") },
            suffix = { Text(" reps") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        
        ElevatedButton(
            onClick = {
                // Add the set first
                onAddSet(
                    weight.toDoubleOrNull(),
                    reps.toIntOrNull(),
                    null
                )
                // Clear inputs
                weight = ""
                reps = ""
                // Start rest timer (default 120 seconds)
                onStartRest(120)
            },
            contentPadding = PaddingValues(8.dp),
            enabled = weight.isNotBlank() || reps.isNotBlank(),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Complete set and start rest timer",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun SetSummaryRow(
    setNumber: Int,
    weight: String,
    reps: String,
    weightUnit: String,
    isCompleted: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set $setNumber:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(60.dp)
        )
        Text(
            text = if (weight.isNotBlank() && reps.isNotBlank()) {
                "$weight $weightUnit × $reps"
            } else if (weight.isNotBlank()) {
                "$weight $weightUnit"
            } else if (reps.isNotBlank()) {
                "$reps reps"
            } else {
                "No data"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        if (isCompleted) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Set completed",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
}