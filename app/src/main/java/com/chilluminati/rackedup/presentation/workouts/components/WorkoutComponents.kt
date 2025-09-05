package com.chilluminati.rackedup.presentation.workouts.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.derivedStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.data.database.entity.WorkoutExercise
import com.chilluminati.rackedup.data.database.entity.Exercise
import com.chilluminati.rackedup.presentation.workouts.ActiveWorkoutState
import com.chilluminati.rackedup.presentation.programs.ExercisePreviewSheet
import com.chilluminati.rackedup.presentation.components.ExerciseTypeInputFields
import com.chilluminati.rackedup.presentation.components.ExerciseInputData
import com.chilluminati.rackedup.presentation.components.determineEffectiveExerciseType
import kotlin.math.roundToInt

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
fun DraggableRestTimerCard(
    restTimer: Int,
    onTimerComplete: () -> Unit,
    onAddTime: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isAtTop by remember { mutableStateOf(true) }
    var cardHeight by remember { mutableStateOf(0) }
    var screenHeight by remember { mutableStateOf(0) }
    var isDragging by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    
    // Calculate positions
    val topPosition = with(density) { 64.dp.toPx() } // TopAppBar height
    val bottomPosition = remember {
        derivedStateOf {
            val screenHeightPx = with(density) { screenHeight.dp.toPx() }
            val bottomPadding = with(density) { 120.dp.toPx() } // Account for navigation bar + safe area
            screenHeightPx - cardHeight.toFloat() - bottomPadding
        }
    }
    
    // Animate between top and bottom positions
    val animatedOffsetY by animateFloatAsState(
        targetValue = if (isAtTop) topPosition else bottomPosition.value,
        animationSpec = tween(durationMillis = 400, easing = androidx.compose.animation.core.EaseInOutCubic),
        label = "offsetY"
    )
    
    // Get screen height and set initial position
    LaunchedEffect(configuration) {
        screenHeight = configuration.screenHeightDp
    }
    
    Box(
        modifier = modifier
            .offset { IntOffset(0, animatedOffsetY.roundToInt()) }
            .onSizeChanged { cardHeight = it.height }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        isDragging = true
                    },
                    onDragEnd = {
                        isDragging = false
                        // Toggle position when drag ends
                        isAtTop = !isAtTop
                    }
                ) { _, _ ->
                    // Just track drag state, position will be toggled on drag end
                }
            }
            .zIndex(2f) // Ensure it's above other content
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (restTimer <= 10) 
                    MaterialTheme.colorScheme.errorContainer 
                else 
                    MaterialTheme.colorScheme.secondaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Minimal drag indicator - just a subtle line
                Surface(
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = if (isDragging) 0.4f else 0.2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(3.dp)
                            .padding(vertical = 2.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Timer display
                Text(
                    text = "${restTimer / 60}:${String.format("%02d", restTimer % 60)}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (restTimer <= 10) 
                        MaterialTheme.colorScheme.onErrorContainer 
                    else 
                        MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Action buttons with larger text
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = { onAddTime(30) },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("+30s", style = MaterialTheme.typography.bodyMedium)
                    }
                    TextButton(
                        onClick = { onAddTime(60) },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("+1m", style = MaterialTheme.typography.bodyMedium)
                    }
                    TextButton(
                        onClick = onTimerComplete,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("Skip", style = MaterialTheme.typography.bodyMedium)
                    }
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
        .filter { it.isCompleted && it.weight != null && it.weight > 0 }
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
                kotlinx.coroutines.delay(1000)
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
                            if (set.isCompleted && set.weight != null && set.weight > 0) {
                                set.weight * (set.reps ?: 0)
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
                        exercise = exercise, // Pass exercise details
                        setNumber = set.setNumber,
                        weight = set.weight?.toInt()?.toString() ?: "",
                        reps = set.reps?.toString() ?: "",
                        isCompleted = set.isCompleted,
                        weightUnit = weightUnit,
                        defaultRestSeconds = defaultRestSeconds,
                        onComplete = {
                            onUpdateSet(set.copy(isCompleted = !set.isCompleted))
                        },
                        onUpdateSet = { weight, reps, distance, durationSeconds ->
                            onUpdateSet(set.copy(
                                weight = if (weight.isBlank()) null else weight.toDoubleOrNull(),
                                reps = if (reps.isBlank()) null else reps.toIntOrNull(),
                                distance = if (distance.isBlank()) null else distance.toDoubleOrNull(),
                                durationSeconds = if (durationSeconds == 0) null else durationSeconds
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
        
        // Exercise preview sheet
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
    exercise: Exercise?,
    setNumber: Int,
    weight: String,
    reps: String,
    isCompleted: Boolean,
    weightUnit: String,
    defaultRestSeconds: Int,
    repScheme: String? = null,
    onComplete: () -> Unit,
    onUpdateSet: (String, String, String, Int) -> Unit, // weight, reps, distance, durationSeconds
    onStartRest: (Int) -> Unit,
    onDeleteSet: (ExerciseSet) -> Unit
) {
    // Determine if weight is actually entered or just placeholder
    val hasActualWeight = set.weight != null && set.weight > 0
    var currentWeight by remember(weight) { 
        mutableStateOf(
            when {
                hasActualWeight -> weight
                weight == "0" -> ""
                else -> weight
            }
        ) 
    }
    var currentReps by remember(reps) { mutableStateOf(reps) }
    var showDeleteMode by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(5) }
    
    // Exercise type-specific input data
    var inputData by remember(set.id, set.weight, set.reps, set.distance, set.durationSeconds) {
        mutableStateOf(
            exercise?.let { ex ->
                when (ex.exerciseType.lowercase()) {
                    "strength" -> ExerciseInputData(
                        weight = currentWeight,
                        reps = currentReps
                    )
                    "cardio" -> {
                        val minutes = (set.durationSeconds ?: 0) / 60
                        val seconds = (set.durationSeconds ?: 0) % 60
                        ExerciseInputData(
                            distance = set.distance?.toString() ?: "",
                            durationMinutes = if (minutes > 0) minutes.toString() else "",
                            durationSeconds = if (seconds > 0) seconds.toString() else ""
                        )
                    }
                    "isometric" -> {
                        val minutes = (set.durationSeconds ?: 0) / 60
                        val seconds = (set.durationSeconds ?: 0) % 60
                        ExerciseInputData(
                            holdDurationMinutes = if (minutes > 0) minutes.toString() else "",
                            holdDurationSeconds = if (seconds > 0) seconds.toString() else ""
                        )
                    }
                    else -> ExerciseInputData(
                        weight = currentWeight,
                        reps = currentReps
                    )
                }
            } ?: ExerciseInputData(
                weight = currentWeight,
                reps = currentReps
            )
        )
    }
    
    // Check if this is an AMRAP/Until Failure exercise
    val isAmrapOrFailure = repScheme?.let { scheme ->
        scheme.equals("AMRAP", ignoreCase = true) || 
        scheme.equals("Till Failure", ignoreCase = true) ||
        scheme.equals("Until Failure", ignoreCase = true) ||
        scheme.equals("Failure", ignoreCase = true)
    } ?: false
    
    // Countdown timer for delete confirmation
    LaunchedEffect(showDeleteDialog) {
        if (showDeleteDialog) {
            countdown = 5
            while (countdown > 0) {
                kotlinx.coroutines.delay(1000)
                countdown--
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        // Clear focus when tapping outside input fields
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

            // Exercise type-specific inputs
            exercise?.let { ex ->
                val effectiveType = determineEffectiveExerciseType(ex)
                when (effectiveType) {
                    "strength", "resistance", "weight training" -> {
                        // Weight input for strength exercises
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
                                inputData = inputData.copy(weight = cleaned)
                                // Pass empty string if no weight entered, otherwise pass the cleaned value
                                val weightToSave = if (cleaned.isBlank()) "" else cleaned
                                onUpdateSet(weightToSave, currentReps, "", 0)
                            },
                            placeholder = { Text("0") },
                            suffix = { Text(" $weightUnit") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
                                disabledBorderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )

                        // Reps input for strength exercises
                        Surface(
                            color = if (isAmrapOrFailure) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (isAmrapOrFailure) {
                                // Show "UNTIL FAILURE" text for AMRAP/Until Failure exercises
                                Text(
                                    text = "UNTIL FAILURE",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                // Regular reps input field for non-AMRAP exercises
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
                                        inputData = inputData.copy(reps = cleaned)
                                        // Pass empty string if no reps entered, otherwise pass the cleaned value
                                        val repsToSave = if (cleaned.isBlank()) "" else cleaned
                                        onUpdateSet(currentWeight, repsToSave, "", 0)
                                    },
                                    placeholder = { Text("reps") },
                                    suffix = { Text(" reps") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
                                        disabledBorderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    ),
                                    textStyle = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    "cardio", "cardiovascular", "aerobic", "endurance", "plyometrics", "isometric", "static", "hold", "stretching", "flexibility" -> {
                        // Use exercise type-specific input fields for cardio and isometric
                        Column(modifier = Modifier.weight(2f)) {
                            ExerciseTypeInputFields(
                                exercise = ex,
                                inputData = inputData,
                                onInputChange = { newInputData ->
                                    inputData = newInputData
                                    // Handle exercise type-specific data conversion
                                    when (effectiveType) {
                                        "cardio", "cardiovascular", "aerobic", "endurance" -> {
                                            val totalDurationSeconds = (newInputData.durationMinutes.toIntOrNull() ?: 0) * 60 + 
                                                                     (newInputData.durationSeconds.toIntOrNull() ?: 0)
                                            val distance = newInputData.distance
                                            onUpdateSet("", "", distance, totalDurationSeconds)
                                        }
                                        "plyometrics" -> {
                                            // For plyometrics, use reps and sets
                                            val reps = newInputData.reps
                                            val sets = newInputData.sets
                                            onUpdateSet("", reps, "", 0)
                                        }
                                        "isometric", "static", "hold" -> {
                                            val totalDurationSeconds = (newInputData.holdDurationMinutes.toIntOrNull() ?: 0) * 60 + 
                                                                     (newInputData.holdDurationSeconds.toIntOrNull() ?: 0)
                                            onUpdateSet("", "", "", totalDurationSeconds)
                                        }
                                        "stretching", "flexibility" -> {
                                            val totalDurationSeconds = (newInputData.holdDurationMinutes.toIntOrNull() ?: 0) * 60 + 
                                                                     (newInputData.holdDurationSeconds.toIntOrNull() ?: 0)
                                            onUpdateSet("", "", "", totalDurationSeconds)
                                        }
                                        else -> {
                                            onUpdateSet("", "", "", 0)
                                        }
                                    }
                                },
                                weightUnit = weightUnit,
                                distanceUnit = "km"
                            )
                        }
                    }
                    else -> {
                        // Default to strength exercise inputs for unknown types
                        OutlinedTextField(
                            value = currentWeight,
                            onValueChange = {
                                val filtered = it.filter { char -> char.isDigit() }
                                val cleaned = if (filtered.startsWith("0") && filtered.length > 1) {
                                    filtered.substring(1)
                                } else {
                                    filtered
                                }
                                currentWeight = cleaned
                                val weightToSave = if (cleaned.isBlank()) "" else cleaned
                                onUpdateSet(weightToSave, currentReps, "", 0)
                            },
                            placeholder = { Text("0") },
                            suffix = { Text(" $weightUnit") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                        
                        OutlinedTextField(
                            value = currentReps,
                            onValueChange = {
                                val filtered = it.filter { char -> char.isDigit() }
                                val cleaned = if (filtered.startsWith("0") && filtered.length > 1) {
                                    filtered.substring(1)
                                } else {
                                    filtered
                                }
                                currentReps = cleaned
                                val repsToSave = if (cleaned.isBlank()) "" else cleaned
                                onUpdateSet(currentWeight, repsToSave, "", 0)
                            },
                            placeholder = { Text("reps") },
                            suffix = { Text(" reps") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } ?: run {
                // Fallback for when exercise is null - use default strength inputs
                OutlinedTextField(
                    value = currentWeight,
                    onValueChange = {
                        val filtered = it.filter { char -> char.isDigit() }
                        val cleaned = if (filtered.startsWith("0") && filtered.length > 1) {
                            filtered.substring(1)
                        } else {
                            filtered
                        }
                        currentWeight = cleaned
                        val weightToSave = if (cleaned.isBlank()) "" else cleaned
                        onUpdateSet(weightToSave, currentReps, "", 0)
                    },
                    placeholder = { Text("0") },
                    suffix = { Text(" $weightUnit") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                
                OutlinedTextField(
                    value = currentReps,
                    onValueChange = {
                        val filtered = it.filter { char -> char.isDigit() }
                        val cleaned = if (filtered.startsWith("0") && filtered.length > 1) {
                            filtered.substring(1)
                        } else {
                            filtered
                        }
                        currentReps = cleaned
                        val repsToSave = if (cleaned.isBlank()) "" else cleaned
                        onUpdateSet(currentWeight, repsToSave, "", 0)
                    },
                    placeholder = { Text("reps") },
                    suffix = { Text(" reps") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }

            // Modern complete toggle button
            val focusManager = LocalFocusManager.current
            val keyboard = LocalSoftwareKeyboardController.current
            
            if (isCompleted) {
                FilledIconButton(
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
                OutlinedIconButton(
                    onClick = {
                        onComplete()
                        if (defaultRestSeconds > 0) {
                            onStartRest(defaultRestSeconds)
                        }
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
