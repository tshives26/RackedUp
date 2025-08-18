package com.chilluminati.rackedup.presentation.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.chilluminati.rackedup.data.database.entity.Workout
import com.chilluminati.rackedup.presentation.components.EmptyStateCard
import com.chilluminati.rackedup.presentation.components.RecentWorkoutCard
import com.chilluminati.rackedup.presentation.components.SecondaryButton
import com.chilluminati.rackedup.presentation.components.GlassmorphismCard
import com.chilluminati.rackedup.presentation.components.GradientBackground
import com.chilluminati.rackedup.presentation.components.BouncyButton

/**
 * Workouts screen showing workout history and quick start options
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutsScreen(
    onNavigateToWorkoutDetail: (Long) -> Unit,
    onNavigateToActiveWorkout: (Long?) -> Unit,
    onNavigateToExerciseLibrary: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = remember { listOf("Recent", "Templates", "Favorites") }

    GradientBackground(
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header with FAB-style quick actions
            WorkoutsHeader(
                onStartQuickWorkout = { onNavigateToActiveWorkout(null) },
                onNavigateToExerciseLibrary = onNavigateToExerciseLibrary
            )

        // Tab layout
        PrimaryTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            0 -> RecentWorkoutsTab(
                workouts = uiState.workouts,
                isLoading = uiState.isLoading,
                onNavigateToWorkoutDetail = onNavigateToWorkoutDetail,
                weightUnit = uiState.weightUnit
            )
            1 -> WorkoutTemplatesTab(onNavigateToActiveWorkout = onNavigateToActiveWorkout)
            2 -> FavoriteWorkoutsTab()
        }
    }
    }
}

@Composable
private fun WorkoutsHeader(
    onStartQuickWorkout: () -> Unit,
    onNavigateToExerciseLibrary: () -> Unit
) {
    GlassmorphismCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        backgroundAlpha = 0.15f
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Quick Start",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BouncyButton(
                    onClick = onStartQuickWorkout,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Quick Start",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                BouncyButton(
                    onClick = onNavigateToExerciseLibrary,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Exercises",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentWorkoutsTab(
    workouts: List<Workout>,
    isLoading: Boolean,
    onNavigateToWorkoutDetail: (Long) -> Unit,
    weightUnit: String = "lbs"
) {
    val dateFormatter = remember { java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (workouts.isEmpty()) {
            item {
                EmptyStateCard(
                    title = stringResource(R.string.no_workouts_yet),
                    description = "Start your first workout to see it here!",
                    icon = Icons.Default.FitnessCenter
                )
            }
        } else {
            items(
                items = workouts,
                key = { it.id },
                contentType = { _ -> "workout" }
            ) { workout ->
                run {
                    val programName = workout.name.split(" - ").firstOrNull() ?: workout.name
                    val programDay = workout.name.split(" - ").getOrNull(1)
                    val meta = buildString {
                        val sets = workout.totalSets
                        if (sets > 0) append("$sets sets")
                        workout.durationMinutes?.let { mins ->
                            if (isNotEmpty()) append(" • ")
                            append("${mins} min")
                        }
                    }.ifBlank { null }
                    val volLabel = if (workout.totalVolume > 0.0) {
                        "Total volume: ${String.format("%.0f", workout.totalVolume)} $weightUnit"
                    } else null

                    RecentWorkoutCard(
                        workoutName = programName,
                        programDay = programDay,
                        date = dateFormatter.format(workout.date),
                        metaText = meta,
                        volumeLabel = volLabel,
                        onClick = { onNavigateToWorkoutDetail(workout.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkoutTemplatesTab(
    onNavigateToActiveWorkout: (Long?) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Mock workout templates
        val templates = listOf(
            Triple("Push Pull Legs", "3 day split", "Beginner"),
            Triple("Upper Lower", "4 day split", "Intermediate"),
            Triple("Full Body", "3 day split", "Beginner"),
            Triple("5/3/1", "4 day program", "Advanced"),
            Triple("Starting Strength", "3 day program", "Beginner")
        )

        items(templates) { (name, type, level) ->
            WorkoutTemplateCard(
                templateName = name,
                templateType = type,
                level = level,
                onClick = { onNavigateToActiveWorkout(System.currentTimeMillis()) }
            )
        }
    }
}

@Composable
private fun FavoriteWorkoutsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            EmptyStateCard(
                title = "No Favorites Yet",
                description = "Add workouts to your favorites to see them here!",
                icon = Icons.Default.Favorite
            )
        }
        
        // TODO: When favorites are implemented, navigate to workout details when a favorite is clicked
    }
}

@Composable
private fun WorkoutTemplateCard(
    templateName: String,
    templateType: String,
    level: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = templateName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = templateType,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                AssistChip(
                    onClick = { },
                    label = { Text(level) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }
        }
    }
}
