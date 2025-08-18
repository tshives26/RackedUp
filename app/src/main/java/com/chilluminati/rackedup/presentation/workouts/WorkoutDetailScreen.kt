package com.chilluminati.rackedup.presentation.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
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
    modifier: Modifier = Modifier,
    viewModel: WorkoutDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
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
                Text(uiState.workout?.name ?: "Workout Details") 
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
                uiState.workout?.let { workout ->
                    // Favorite toggle
                    IconButton(onClick = { 
                        viewModel.updateWorkout(workout.copy(isFavorite = !workout.isFavorite))
                    }) {
                        Icon(
                            imageVector = if (workout.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Add to favorites",
                            tint = if (workout.isFavorite) MaterialTheme.colorScheme.error else LocalContentColor.current
                        )
                    }
                    // Quick 1-5 rating chips to enable rating achievements
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (1..5).forEach { r ->
                            AssistChip(
                                onClick = { viewModel.updateWorkout(workout.copy(rating = r)) },
                                label = { Text(r.toString()) }
                            )
                        }
                    }
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share workout"
                    )
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
                    WorkoutSummaryCard(uiState)
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
                            onClick = { 
                                exercise?.let { onNavigateToExerciseDetail(it.id) }
                            }
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
    }
}

@Composable
private fun WorkoutSummaryCard(uiState: WorkoutDetailUiState) {
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
                StatItem("Volume", "${String.format("%.0f", uiState.totalVolume)} lbs")
                StatItem("PR's", uiState.personalRecords.toString())
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
    onClick: () -> Unit
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
                Text(
                    text = "${sets.size} sets",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            
            // Show set performance
            val performance = sets.mapIndexed { index, set ->
                val weight = set.weight?.let { "${it.toInt()} lbs" } ?: ""
                val reps = set.reps?.toString() ?: ""
                when {
                    weight.isNotEmpty() && reps.isNotEmpty() -> "$weight × $reps"
                    weight.isNotEmpty() -> weight
                    reps.isNotEmpty() -> "$reps reps"
                    set.durationSeconds != null -> "${set.durationSeconds}s"
                    else -> "Set ${index + 1}"
                }
            }.joinToString(", ")
            
            Text(
                text = performance.ifEmpty { "No sets recorded" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
