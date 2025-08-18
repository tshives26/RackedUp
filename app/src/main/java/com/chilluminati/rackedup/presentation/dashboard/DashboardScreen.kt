package com.chilluminati.rackedup.presentation.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.Calendar
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.presentation.components.FeaturePlaceholderCard
import com.chilluminati.rackedup.presentation.components.QuickStatCard
import com.chilluminati.rackedup.presentation.components.RecentWorkoutCard
 
import com.chilluminati.rackedup.presentation.profile.AchievementsViewModel
import com.chilluminati.rackedup.presentation.profile.AchievementsDialog
import com.chilluminati.rackedup.core.util.formatCompact

/**
 * Dashboard screen showing overview of user's fitness progress and quick actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToPrograms: () -> Unit,
    onNavigateToProgressHistory: () -> Unit,
    onStartWorkout: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Handle error state
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Show error snackbar or dialog
            viewModel.clearError()
        }
    }
    
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 640.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Header
        item {
            ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                val gradStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                val gradEnd = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f)
                val gradient = androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(gradStart, gradEnd))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawRect(brush = gradient)
                        }
                        .padding(16.dp)
                ) {
                    DashboardHeader(
                        profile = uiState.profile,
                        greeting = viewModel.getGreeting(),
                        motivationalMessage = uiState.motivationalMessage
                    )
                }
            }
        }

        // Current Program Overview
        item {
            val (computedDaysCompleted, computedLastLabel) = remember(uiState.activeProgram, uiState.recentWorkouts) {
                val program = uiState.activeProgram
                if (program != null) {
                    val completed = uiState.recentWorkouts.filter { it.programId == program.id && (it.endTime != null || it.isCompleted) }
                    val days = completed.mapNotNull { it.programDayId }.distinct().size
                    val latest = completed.maxByOrNull { it.date.time }
                    val label: String? = latest?.let { "Last: ${SimpleDateFormat("MMM d", Locale.getDefault()).format(it.date)}" }
                    days to label
                } else 0 to null
            }
            CurrentProgramSection(
                activeProgram = uiState.activeProgram,
                daysCompleted = computedDaysCompleted,
                lastCompletedLabel = computedLastLabel,
                onNavigateToPrograms = onNavigateToPrograms,
                onStartProgramDaySelection = { programId -> onStartWorkout(programId) }
            )
        }

        // Plate Calculator moved to Profile > Tools

        // Quick Stats
        item {
            QuickStatsSection(
                weeklyStats = uiState.weeklyStats,
                monthlyStats = uiState.monthlyStats,
                isLoading = uiState.isLoading
            )
        }

        // Recent Workouts
        item {
            RecentWorkoutsSection(
                recentWorkouts = uiState.recentWorkouts,
                onNavigateToProgressHistory = onNavigateToProgressHistory,
                isLoading = uiState.isLoading,
                weightUnit = uiState.weightUnit,
                viewModel = viewModel
            )
        }

        // Achievements
        item {
            AchievementsSection()
        }
        }
    }
}

@Composable
private fun DashboardHeader(
    profile: com.chilluminati.rackedup.data.database.entity.UserProfile? = null,
    greeting: String = "Good morning!",
    motivationalMessage: String = "Ready to crush your goals?"
) {
    Column {
        Text(
            text = if (profile != null) "$greeting, ${profile.name}!" else greeting,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = motivationalMessage,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun InfoPill(icon: ImageVector, label: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CurrentProgramSection(
    activeProgram: com.chilluminati.rackedup.data.database.entity.Program? = null,
    daysCompleted: Int = 0,
    lastCompletedLabel: String? = null,
    onNavigateToPrograms: () -> Unit,
    onStartProgramDaySelection: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Current Program",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (activeProgram == null) {
            FeaturePlaceholderCard(
                title = "No Active Program",
                description = "Create or copy a template to start a program.",
                icon = Icons.Default.FitnessCenter
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onNavigateToPrograms) {
                Text("Browse Programs")
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else {
            // Premium look: soft dual gradient with border and glow
            val gradStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
            val gradEnd = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.10f)
            val gradient = androidx.compose.ui.graphics.Brush.linearGradient(
                colors = listOf(gradStart, gradEnd),
                start = androidx.compose.ui.geometry.Offset.Zero,
                end = androidx.compose.ui.geometry.Offset.Infinite
            )
				ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ) {
					Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind { drawRoundRect(brush = gradient, cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)) }
							.padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 12.dp),
						horizontalAlignment = Alignment.CenterHorizontally
                ) {
						Row(
							modifier = Modifier.fillMaxWidth(),
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.Center
						) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeProgram.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
						if (!activeProgram.description.isNullOrBlank()) {
							Spacer(modifier = Modifier.height(6.dp))
							Text(
								text = activeProgram.description ?: "No description available",
								style = MaterialTheme.typography.bodyMedium,
								color = MaterialTheme.colorScheme.onSurfaceVariant,
								textAlign = TextAlign.Center,
								modifier = Modifier.fillMaxWidth()
							)
						}
						Spacer(modifier = Modifier.height(12.dp))
						Box(
							modifier = Modifier.fillMaxWidth(),
							contentAlignment = Alignment.Center
						) {
							FlowRow(
								horizontalArrangement = Arrangement.spacedBy(16.dp),
								verticalArrangement = Arrangement.spacedBy(6.dp),
								maxItemsInEachRow = 2
							) {
								MetaItem(Icons.Default.CheckCircle, "$daysCompleted days complete")
								MetaItem(Icons.Default.Event, lastCompletedLabel ?: "No days completed yet")
							}
						}
						Spacer(modifier = Modifier.height(16.dp))
						Button(
							onClick = { onStartProgramDaySelection(activeProgram.id) },
							modifier = Modifier
								.fillMaxWidth(0.9f)
								.align(Alignment.CenterHorizontally),
							contentPadding = PaddingValues(vertical = 14.dp)
						) {
                        Icon(Icons.Default.PlayArrow, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Start Workout")
                    }
                    TextButton(
                        onClick = onNavigateToPrograms,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Manage Program")
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                    }
                }
            }
        }
    }
}
@Composable
private fun MetaItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
@Composable
private fun QuickStatsSection(
    weeklyStats: WeeklyStats = WeeklyStats(),
    monthlyStats: MonthlyStats = MonthlyStats(),
    isLoading: Boolean = false
) {
    Column {
        Text(
            text = stringResource(R.string.quick_stats),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        if (isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(88.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickStatCard(
                    title = "Workouts",
                    value = "${monthlyStats.workoutCount}",
                    subtitle = "This month",
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Workouts",
                    value = "${weeklyStats.workoutCount}",
                    subtitle = "This week",
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Sets",
                    value = "${weeklyStats.totalSets}",
                    subtitle = "This week",
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Volume",
                    value = "${(weeklyStats.totalVolume).formatCompact()}",
                    subtitle = "this week",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RecentWorkoutsSection(
    recentWorkouts: List<com.chilluminati.rackedup.data.database.entity.Workout> = emptyList(),
    onNavigateToProgressHistory: () -> Unit,
    isLoading: Boolean = false,
    weightUnit: String = "lbs",
    viewModel: DashboardViewModel = hiltViewModel()
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.recent_workouts),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onNavigateToProgressHistory) {
                Text("View All")
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        
        if (isLoading) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
                if (it < 2) Spacer(modifier = Modifier.height(8.dp))
            }
        } else if (recentWorkouts.isEmpty()) {
            FeaturePlaceholderCard(
                title = "No Recent Workouts",
                description = "Start your first workout to see it here!",
                icon = Icons.Default.FitnessCenter
            )
        } else {
            val dateFormatter = remember { java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()) }
            recentWorkouts.take(3).forEachIndexed { index, workout ->
                // Extract program name and day from workout name
                val nameParts = workout.name.split(" - ")
                val programName = nameParts.firstOrNull() ?: workout.name
                val programDay = nameParts.getOrNull(1)
                
                // State to hold the program day name from database
                var finalProgramDay by remember { mutableStateOf<String?>(programDay) }
                
                // Load program day name from database if available
                LaunchedEffect(workout.id, workout.programDayId) {
                    if (finalProgramDay == null && workout.programDayId != null) {
                        try {
                            val dbProgramDayName = viewModel.getProgramDayName(workout.programDayId)
                            finalProgramDay = dbProgramDayName
                                                 } catch (e: Exception) {
                             // Fallback to parsing if database query fails
                             val words = workout.name.split(" ")
                             if (words.size >= 3 && words.last().equals("Day", ignoreCase = true)) {
                                 val dayNumber = words.dropLast(1).lastOrNull()?.toIntOrNull()
                                 finalProgramDay = if (dayNumber != null && dayNumber in 1..7) {
                                     "Day $dayNumber"
                                 } else {
                                     words.dropLast(1).joinToString(" ")
                                 }
                             }
                         }
                    }
                }
                
                // Build meta and labeled volume per new card layout
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
                    programDay = finalProgramDay,
                    date = dateFormatter.format(workout.date),
                    metaText = meta,
                    volumeLabel = volLabel,
                    onClick = { /* TODO: Navigate to workout detail */ }
                )
                if (index < recentWorkouts.size - 1 && index < 2) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun AchievementsSection(viewModel: AchievementsViewModel = hiltViewModel()) {
    val states by viewModel.achievements.collectAsStateWithLifecycle()
    val unlocked = states.count { it.isUnlocked }
    val total = states.size
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().clickable { showDialog = true }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.achievements),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            AssistChip(
                onClick = { showDialog = true },
                label = { Text("$unlocked/$total") },
                leadingIcon = { Icon(Icons.Default.Star, null) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (unlocked == 0) {
            FeaturePlaceholderCard(
                title = "No Achievements Yet",
                description = "Start training to unlock your first achievement.",
                icon = Icons.Default.Star,
                modifier = Modifier
                    .fillMaxWidth()
                    .let { it }
            )
        } else {
            // Compact horizontally scrollable unlocked list
            val unlockedStates = states.filter { it.isUnlocked }.sortedByDescending { it.unlockedAt }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(unlockedStates) { st ->
                    AchievementPill(title = st.definition.title, date = st.unlockedAt)
                }
            }
        }
    }

    if (showDialog) {
        AchievementsDialog(states = states, unlocked = unlocked, total = total, onDismiss = { showDialog = false })
    }
}

@Composable
private fun AchievementPill(title: String, date: java.util.Date?) {
    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                Text(
                    date?.let { java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault()).format(it) } ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
