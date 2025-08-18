package com.chilluminati.rackedup.presentation.programs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectProgramDayScreen(
    programId: Long,
    onNavigateBack: () -> Unit,
    onDaySelectedStart: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProgramsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Ensure program details are loaded
    LaunchedEffect(programId) {
        viewModel.loadProgramDetails(programId)
    }

    // Navigate when a workout gets created and started
    LaunchedEffect(uiState.lastCreatedWorkoutId) {
        val id = uiState.lastCreatedWorkoutId
        if (id != null) {
            viewModel.consumeLastCreatedWorkoutId()
            onDaySelectedStart(id)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Select Day") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = uiState.selectedProgram?.name ?: "Program",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Choose a day to start",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(uiState.selectedProgramDays.sortedBy { it.dayNumber }) { day ->
                val exercises = uiState.selectedProgramExercises.entries
                    .firstOrNull { it.key.id == day.id }
                    ?.value ?: emptyList()
                val exerciseCount = exercises.size
                val totalSets = exercises.sumOf { ex -> ex.sets.split("x").firstOrNull()?.toIntOrNull() ?: 0 }
                val repsSummary: String? = run {
                    val distinct = exercises.mapNotNull { it.reps }.toSet().filter { it.isNotBlank() }
                    when {
                        distinct.isEmpty() -> null
                        distinct.size == 1 -> distinct.first()
                        else -> "varied"
                    }
                }
                val completedDate = uiState.selectedProgramLastCompletedByDay[day.id]
                val isMostRecent = uiState.selectedProgramMostRecentCompletedDayId == day.id
                val isRestDay = day.isRestDay

                DayCard(
                    title = day.name,
                    dayNumber = day.dayNumber,
                    exerciseCount = exerciseCount,
                    totalSets = totalSets,
                    repsSummary = repsSummary,
                    completedDate = completedDate,
                    isMostRecent = isMostRecent,
                    isRestDay = isRestDay,
                    onClick = { viewModel.startWorkoutFromProgram(programId, day.id) }
                )
            }
        }
    }
}


@Composable
private fun DayCard(
    title: String,
    dayNumber: Int,
    exerciseCount: Int,
    totalSets: Int,
    repsSummary: String?,
    completedDate: java.util.Date?,
    isMostRecent: Boolean,
    isRestDay: Boolean,
    onClick: () -> Unit
) {
    val container = if (isRestDay) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.secondaryContainer
    val onContainer = if (isRestDay) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSecondaryContainer
    val outlineAlpha = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.12f)

    val cardColors = CardDefaults.elevatedCardColors(containerColor = container)
    val cardElevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)

    if (isRestDay) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = cardColors,
            elevation = cardElevation
        ) {
            CardContent(
                title = title,
                dayNumber = dayNumber,
                exerciseCount = exerciseCount,
                totalSets = totalSets,
                repsSummary = repsSummary,
                completedDate = completedDate,
                isMostRecent = isMostRecent,
                isRestDay = true,
                onContainer = onContainer,
                outlineAlpha = outlineAlpha
            )
        }
    } else {
        ElevatedCard(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            colors = cardColors,
            elevation = cardElevation
        ) {
            CardContent(
                title = title,
                dayNumber = dayNumber,
                exerciseCount = exerciseCount,
                totalSets = totalSets,
                repsSummary = repsSummary,
                completedDate = completedDate,
                isMostRecent = isMostRecent,
                isRestDay = false,
                onContainer = onContainer,
                outlineAlpha = outlineAlpha
            )
        }
    }
}

@Composable
private fun CardContent(
    title: String,
    dayNumber: Int,
    exerciseCount: Int,
    totalSets: Int,
    repsSummary: String?,
    completedDate: java.util.Date?,
    isMostRecent: Boolean,
    isRestDay: Boolean,
    onContainer: androidx.compose.ui.graphics.Color,
    outlineAlpha: androidx.compose.ui.graphics.Color
) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header row
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                // Day bullet
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 2.dp
                ) {
                    Box(Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = dayNumber.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = onContainer,
                    modifier = Modifier.weight(1f)
                )
                if (isMostRecent && !isRestDay) {
                    Text(
                        text = "Last Completed",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(Modifier.width(8.dp))
                }
                if (!isRestDay) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = onContainer.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            if (isRestDay) {
                // Minimal presentation for rest days: no chips or footer
                Text(
                    text = "Rest day",
                    color = onContainer.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                // Metadata chips row
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = {},
                        label = { Text(formatCount(exerciseCount, "exercise")) },
                        leadingIcon = { Icon(Icons.Default.FitnessCenter, contentDescription = null) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = outlineAlpha)
                    )
                    AssistChip(
                        onClick = {},
                        label = { Text(formatCount(totalSets, "set")) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = outlineAlpha)
                    )
                    if (repsSummary != null) {
                        val repsLabel = repsSummary.toIntOrNull()?.let { formatCount(it, "rep") } ?: "varied reps"
                        AssistChip(
                            onClick = {},
                            label = { Text(repsLabel) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = outlineAlpha)
                        )
                    }
                }

                // Footer: last completed date (subtle)
                if (completedDate != null) {
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = null,
                            tint = onContainer.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.width(6.dp))
                        val fmt = java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault())
                        Text(
                            text = "Last completed: ${fmt.format(completedDate)}",
                            color = onContainer.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Not completed yet",
                        color = onContainer.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.alpha(0.9f)
                    )
                }
            }
        }
    }

private fun formatCount(count: Int, singular: String, plural: String = singular + "s"): String {
    val label = if (count == 1) singular else plural
    return "$count $label"
}


