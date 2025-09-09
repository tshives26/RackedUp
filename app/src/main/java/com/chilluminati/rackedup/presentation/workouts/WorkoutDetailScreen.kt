package com.chilluminati.rackedup.presentation.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.data.database.entity.WorkoutExercise
import com.chilluminati.rackedup.data.database.entity.ExerciseSet
import com.chilluminati.rackedup.data.database.entity.Exercise
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Detailed view of a specific workout session
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(
    workoutId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToExerciseDetail: (Long) -> Unit,
    onNavigateToExerciseEdit: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: WorkoutDetailViewModel = hiltViewModel(),
    isEditMode: Boolean = false
) {
    val weightUnit by viewModel.weightUnit.collectAsStateWithLifecycle(initialValue = "lbs")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // State for delete confirmation dialog
    var showDeleteExerciseDialog by remember { mutableStateOf(false) }
    var exerciseToDelete by remember { mutableStateOf<WorkoutExercise?>(null) }
    
    // Load workout details when screen loads
    LaunchedEffect(workoutId) {
        viewModel.loadWorkoutDetail(workoutId)
    }
    
    // Show error snackbar if there's an error
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            // In a real app, you'd show a snackbar here
            viewModel.clearError()
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { 
                Text(
                    text = if (isEditMode) "Edit Workout" else (uiState.workout?.name ?: "Workout Details"),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                ) 
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            },
            actions = {
                if (isEditMode) {
                    IconButton(
                        onClick = {
                            // TODO: Save changes
                            onNavigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save changes"
                        )
                    }
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (uiState.workout != null) {
                // Workout Summary
                item {
                    WorkoutSummaryCard(uiState, weightUnit)
                }

                // Exercise List
                if (uiState.workoutExercises.isNotEmpty()) {
                    item {
                        Text(
                            text = "Exercises",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(uiState.workoutExercises) { workoutExercise ->
                        val exercise = uiState.exerciseDetails[workoutExercise.id]
                        val sets = uiState.exerciseSets[workoutExercise.id] ?: emptyList()
                        
                        ExerciseDetailCard(
                            workoutExercise = workoutExercise,
                            exercise = exercise,
                            sets = sets,
                            weightUnit = weightUnit,
                            onClick = { 
                                if (isEditMode) {
                                    // In edit mode, clicking opens edit options
                                    // TODO: Navigate to exercise edit screen
                                } else {
                                    exercise?.let { onNavigateToExerciseDetail(it.id) }
                                }
                            },
                            onEditExercise = { workoutExercise ->
                                onNavigateToExerciseEdit(workoutExercise.id)
                            },
                            onDeleteExercise = { workoutExercise ->
                                exerciseToDelete = workoutExercise
                                showDeleteExerciseDialog = true
                            },
                            showEditOptions = isEditMode
                        )
                    }
                }

                // Workout Notes
                uiState.workout?.notes?.let { notes ->
                    if (notes.isNotBlank()) {
                        item {
                            WorkoutNotesCard(notes)
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = "Workout not found",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        
        // Delete exercise confirmation dialog
        if (showDeleteExerciseDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteExerciseDialog = false },
                title = { Text("Delete Exercise") },
                text = { 
                    Text(
                        "Are you sure you want to delete this exercise from the workout? This action cannot be undone."
                    ) 
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            exerciseToDelete?.let { exercise ->
                                viewModel.deleteExercise(exercise.id)
                            }
                            showDeleteExerciseDialog = false
                            exerciseToDelete = null
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteExerciseDialog = false
                            exerciseToDelete = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun WorkoutSummaryCard(uiState: WorkoutDetailUiState, weightUnit: String) {
    val workout = uiState.workout ?: return
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateFormat.format(workout.date),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = workout.durationMinutes?.let { "${it} minutes" } ?: "In Progress",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Exercises", uiState.totalExercises.toString())
                StatItem("Sets", uiState.totalSets.toString())
                StatItem("Volume", formatVolume(uiState.totalVolume, weightUnit))
                StatItem("PR's", uiState.personalRecordCount.toString())
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
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

@Composable
private fun ExerciseDetailCard(
    workoutExercise: WorkoutExercise,
    exercise: Exercise?,
    sets: List<ExerciseSet>,
    weightUnit: String,
    onClick: () -> Unit,
    onEditExercise: (WorkoutExercise) -> Unit = {},
    onDeleteExercise: (WorkoutExercise) -> Unit = {},
    showEditOptions: Boolean = false
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = exercise?.name ?: "Exercise ${workoutExercise.exerciseId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "${sets.size} sets",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (showEditOptions) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box {
                            var expanded by remember { mutableStateOf(false) }
                            
                            IconButton(
                                onClick = { expanded = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Edit") },
                                    leadingIcon = { 
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = null
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        onEditExercise(workoutExercise)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete") },
                                    leadingIcon = { 
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        onDeleteExercise(workoutExercise)
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            // Show sets in a clean, readable format
            if (sets.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    sets.forEachIndexed { index, set ->
                        val weight = set.weight?.let { "${it.toInt()} $weightUnit" } ?: ""
                        val reps = set.reps?.toString() ?: ""
                        val duration = set.durationSeconds?.let { "${it}s" } ?: ""
                        
                        val setText = when {
                            weight.isNotEmpty() && reps.isNotEmpty() -> "$weight × $reps reps"
                            weight.isNotEmpty() -> weight
                            reps.isNotEmpty() -> "$reps reps"
                            duration.isNotEmpty() -> duration
                            else -> "Set ${index + 1}"
                        }
                        
                        Text(
                            text = "Set ${index + 1}: $setText",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Text(
                    text = "No sets recorded",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun WorkoutNotesCard(notes: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Notes,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Workout Notes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = notes,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Format volume with proper number formatting
 */
private fun formatVolume(volume: Double, weightUnit: String): String {
    return when {
        volume >= 1000 -> String.format("%.1fK %s", volume / 1000, weightUnit)
        volume >= 100 -> String.format("%.0f %s", volume, weightUnit)
        else -> String.format("%.0f %s", volume, weightUnit)
    }
}
