@file:Suppress("UNRESOLVED_REFERENCE")
package com.chilluminati.rackedup.presentation.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.chilluminati.rackedup.presentation.components.FullScreenImageDialog
import androidx.compose.ui.window.Dialog
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.data.database.entity.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExerciseLibraryViewModel = hiltViewModel()
) {
    val exercise by viewModel.getExerciseById(exerciseId).collectAsStateWithLifecycle(initialValue = null)

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(exercise?.name ?: "Exercise #$exerciseId") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            },
            actions = {
                IconButton(onClick = { exercise?.let { viewModel.toggleFavorite(it.id, !it.isFavorite) } }) {
                    Icon(
                        imageVector = if (exercise?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle favorite"
                    )
                }
            }
        )

        if (exercise == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            exercise?.let { ex ->
                item { ExerciseHeaderCard(exercise = ex) }
                item { ExerciseInstructionsCard(exercise = ex) }
            }
        }
    }
}

@Composable
private fun ExerciseHeaderCard(exercise: Exercise) {
    var showImage by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!exercise.imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = exercise.imageUrl,
                        contentDescription = exercise.name,
                        modifier = Modifier.size(72.dp).let { it }.clickable { showImage = true },
                        onSuccess = {}
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = listOfNotNull(
                            exercise.category.takeIf { it.isNotBlank() }?.replaceFirstChar { it.uppercase() },
                            exercise.equipment.takeIf { it.isNotBlank() }?.replaceFirstChar { it.uppercase() }
                        ).joinToString(" • "),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (exercise.difficultyLevel.isNotBlank()) {
                    AssistChip(onClick = { }, label = { Text(exercise.difficultyLevel.replaceFirstChar { it.uppercase() }) })
                }
                exercise.force?.let { AssistChip(onClick = { }, label = { Text(it.replaceFirstChar { c -> c.uppercase() }) }) }
                exercise.mechanic?.let { AssistChip(onClick = { }, label = { Text(it.replaceFirstChar { c -> c.uppercase() }) }) }
            }

            if (exercise.muscleGroups.isNotEmpty() || exercise.secondaryMuscles.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Primary: ${exercise.muscleGroups.map { it.replaceFirstChar { c -> c.uppercase() } }.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (exercise.secondaryMuscles.isNotEmpty()) {
                        Text(
                            "Secondary: ${exercise.secondaryMuscles.map { it.replaceFirstChar { c -> c.uppercase() } }.joinToString(", ")}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }

    if (showImage && !exercise.imageUrl.isNullOrEmpty()) {
        FullScreenImageDialog(imageUrl = exercise.imageUrl, contentDescription = exercise.name) {
            showImage = false
        }
    }
}

@Composable
private fun ExerciseInstructionsCard(exercise: Exercise) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Instructions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (exercise.instructionSteps.isNotEmpty()) {
                exercise.instructionSteps.forEachIndexed { idx, step ->
                    Text(text = "${idx + 1}. $step", style = MaterialTheme.typography.bodyMedium)
                }
            } else if (!exercise.instructions.isNullOrBlank()) {
                Text(text = exercise.instructions.orEmpty(), style = MaterialTheme.typography.bodyMedium)
            } else {
                Text(text = "No instructions available.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
