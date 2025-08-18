package com.chilluminati.rackedup.presentation.progress

import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import com.chilluminati.rackedup.presentation.components.FeaturePlaceholderCard
import com.chilluminati.rackedup.presentation.components.charts.VolumeChart
import com.chilluminati.rackedup.presentation.components.charts.StrengthChart
import com.chilluminati.rackedup.presentation.components.charts.BodyMeasurementChart
import com.chilluminati.rackedup.presentation.components.charts.ConsistencyChart
import com.chilluminati.rackedup.presentation.components.QuickStatCard
import com.chilluminati.rackedup.presentation.workouts.WorkoutsViewModel
import java.util.Date
import com.chilluminati.rackedup.core.util.formatCompact

/**
 * Progress screen showing analytics, charts, and workout history
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    onNavigateToWorkoutDetail: (Long) -> Unit,
    onNavigateToActiveWorkout: (Long) -> Unit,
    viewModel: ProgressViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    initialTab: String? = null
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = remember { listOf("Overview", "Strength", "Body", "History") }

    // Handle initial tab selection
    LaunchedEffect(initialTab) {
        selectedTab = when (initialTab?.lowercase()) {
            "overview" -> 0
            "strength" -> 1
            "body" -> 2
            "history" -> 3
            else -> 0
        }
    }

    // Handle workout completion events
    val workoutsViewModel: WorkoutsViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        workoutsViewModel.workoutCompletionEvent.collect { _ ->
            // Force refresh all stats when a workout is completed
            viewModel.refreshAllStats()
        }
    }
    
    val volumeData by viewModel.volumeData.collectAsStateWithLifecycle()
    val strengthData by viewModel.strengthData.collectAsStateWithLifecycle()
    val measurementData by viewModel.measurementData.collectAsStateWithLifecycle()
    val personalRecords by viewModel.personalRecords.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val weightUnit by viewModel.weightUnit.collectAsStateWithLifecycle(initialValue = "kg")
    val workoutHistoryDisplay by viewModel.workoutHistoryDisplay.collectAsStateWithLifecycle()
    val weeklyStats by viewModel.weeklyStats.collectAsStateWithLifecycle()
    val monthlyStats by viewModel.monthlyStats.collectAsStateWithLifecycle()
    val totalAllTimeVolume by viewModel.totalAllTimeVolume.collectAsStateWithLifecycle()
    val currentStreak by viewModel.currentStreak.collectAsStateWithLifecycle()
    val longestStreak by viewModel.longestStreak.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()
    val consistencyData by viewModel.consistencyData.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        // Header
        ProgressHeader(
            volumeData = volumeData,
            strengthData = strengthData,
            personalRecords = personalRecords,
            isLoading = isLoading,
            weightUnit = weightUnit,
            weeklyStats = weeklyStats,
            currentStreak = currentStreak
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
            0 -> OverviewTab(
                volumeData = volumeData,
                strengthData = strengthData,
                personalRecords = personalRecords,
                isLoading = isLoading,
                weightUnit = weightUnit,
                weeklyStats = weeklyStats,
                monthlyStats = monthlyStats,
                totalAllTimeVolume = totalAllTimeVolume,
                currentStreak = currentStreak,
                longestStreak = longestStreak,
                achievementsUnlocked = achievements.count { it.isUnlocked },
                consistencyData = consistencyData
            )
            1 -> StrengthProgressTab(
                strengthData = strengthData,
                volumeData = volumeData,
                isLoading = isLoading,
                weightUnit = weightUnit,
                onNavigateToActiveWorkout = onNavigateToActiveWorkout
            )
            2 -> BodyProgressTab(
                measurementData = measurementData,
                isLoading = isLoading
            )
            3 -> HistoryTab(
                items = workoutHistoryDisplay,
                onNavigateToWorkoutDetail = onNavigateToWorkoutDetail
            )
        }
    }
}

@Composable
private fun ProgressHeader(
    volumeData: List<Pair<Date, Double>>,
    strengthData: List<Pair<Date, Map<String, Double>>>,
    personalRecords: List<com.chilluminati.rackedup.data.database.entity.PersonalRecord>,
    isLoading: Boolean,
    weightUnit: String,
    weeklyStats: WeeklyStats,
    currentStreak: Int
) {
    val hasData = volumeData.isNotEmpty() || strengthData.isNotEmpty() || personalRecords.isNotEmpty()
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        val gradStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        val gradEnd = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f)
        val gradient = Brush.horizontalGradient(listOf(gradStart, gradEnd))
        Column(
            modifier = Modifier
                .drawBehind { drawRect(brush = gradient) }
                .padding(16.dp)
        ) {
            Text(
                text = "This Week",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            if (isLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(4) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Loading...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            } else if (!hasData) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No progress data yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Complete your first workout to see your progress",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeeklyStatItem("Workouts", "${weeklyStats.workoutCount}", "this week")
                    WeeklyStatItem("Volume", "${(weeklyStats.totalVolume).formatCompact()}", weightUnit)
                    WeeklyStatItem("Sets", "${weeklyStats.totalSets}", "this week")
                    WeeklyStatItem("Streak", "$currentStreak", "days")
                }
            }
        }
    }
}

@Composable
private fun WeeklyStatItem(label: String, value: String, unit: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        if (unit.isNotBlank()) {
            Text(
                text = unit,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun OverviewTab(
    volumeData: List<Pair<Date, Double>>,
    strengthData: List<Pair<Date, Map<String, Double>>>,
    personalRecords: List<com.chilluminati.rackedup.data.database.entity.PersonalRecord>,
    isLoading: Boolean,
    weightUnit: String,
    weeklyStats: WeeklyStats,
    monthlyStats: MonthlyStats,
    totalAllTimeVolume: Double,
    currentStreak: Int,
    longestStreak: Int,
    achievementsUnlocked: Int,
    consistencyData: List<com.chilluminati.rackedup.data.repository.ConsistencyDataPoint>
) {
    val hasData = volumeData.isNotEmpty() || strengthData.isNotEmpty() || personalRecords.isNotEmpty()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Quick Stats (mirror Dashboard)
        item {
            Text(
                text = stringResource(R.string.quick_stats),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (!hasData && !isLoading) {
            item {
                FeaturePlaceholderCard(
                    title = "No Progress Data",
                    description = "Complete your first workout to start tracking your progress and see your statistics here.",
                    icon = Icons.AutoMirrored.Filled.TrendingUp
                )
            }
        } else {
            item {
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

        // Charts Section
        item {
            Text(
                text = "Charts & Analytics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (!hasData && !isLoading) {
            item {
                FeaturePlaceholderCard(
                    title = "Charts Coming Soon",
                    description = "Once you complete workouts, you'll see volume trends and strength progress charts here.",
                    icon = Icons.AutoMirrored.Filled.TrendingUp
                )
            }
        } else {
            item {
                VolumeChart(volumeData, Modifier, "Volume Trends", weightUnit)
            }

            item {
                StrengthChart(strengthData, listOf("Bench Press", "Squat", "Deadlift", "Overhead Press"), Modifier, "Strength Progress", weightUnit)
            }

            item {
                ConsistencyChart(data = consistencyData, modifier = Modifier, title = "Consistency (last 12 weeks)")
            }
        }

        // Personal Records
        item {
            Text(
                text = stringResource(R.string.personal_records),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (personalRecords.isEmpty() && !isLoading) {
            item {
                FeaturePlaceholderCard(
                    title = "No Personal Records Yet",
                    description = "Set personal records by completing workouts and achieving new maximums.",
                    icon = Icons.Default.Star
                )
            }
        } else {
            items(
                items = personalRecords.take(4),
                key = { it.id },
                contentType = { _ -> "pr" }
            ) { record ->
                PersonalRecordCard(
                    exerciseName = "Exercise ${record.exerciseId}",
                    weight = "${record.estimated1RM?.toInt() ?: 0} $weightUnit",
                    date = record.achievedAt.toString()
                )
            }
        }
    }
}

@Composable
private fun StrengthProgressTab(
    strengthData: List<Pair<Date, Map<String, Double>>>,
    volumeData: List<Pair<Date, Double>>,
    isLoading: Boolean,
    weightUnit: String,
    onNavigateToActiveWorkout: (Long) -> Unit
) {
    val hasData = strengthData.isNotEmpty() || volumeData.isNotEmpty()
    
    // Get active workout session
    val workoutsViewModel: WorkoutsViewModel = hiltViewModel()
    val activeSession by workoutsViewModel.activeSessionFlow.collectAsStateWithLifecycle(initialValue = null)
    val shouldShowResumeButton = activeSession?.workoutId != null && activeSession?.workoutId != 0L
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!hasData && !isLoading) {
            item {
                FeaturePlaceholderCard(
                    title = "No Strength Data",
                    description = "Complete workouts with strength exercises to see your 1RM progression charts here.",
                    icon = Icons.AutoMirrored.Filled.TrendingUp
                )
            }
        } else {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Strength Progress",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Resume button - only show if there's an active workout
                    if (shouldShowResumeButton) {
                        val workoutId = activeSession?.workoutId ?: 0L
                        Button(
                            onClick = { onNavigateToActiveWorkout(workoutId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Resume")
                        }
                    }
                }
            }

            item {
                StrengthChart(strengthData, listOf("Bench Press", "Squat", "Deadlift", "Overhead Press"), Modifier, "1RM Progression", weightUnit)
            }

            item {
                VolumeChart(volumeData, Modifier, "Volume Analysis", weightUnit)
            }
        }
    }
}

@Composable
private fun BodyProgressTab(
    measurementData: List<Pair<Date, Map<String, Double>>>,
    isLoading: Boolean
) {
    val hasData = measurementData.isNotEmpty()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!hasData && !isLoading) {
            item {
                FeaturePlaceholderCard(
                    title = "No Body Measurements",
                    description = "Add body measurements to track your physical progress over time.",
                    icon = Icons.Default.Person
                )
            }
        } else {
            item {
                BodyMeasurementChart(
                    measurementData = measurementData,
                    measurements = listOf("Weight", "Body Fat %", "Muscle Mass"),
                    title = stringResource(R.string.body_measurements)
                )
            }

            item {
                BodyMeasurementChart(
                    measurementData = measurementData,
                    measurements = listOf("Chest", "Waist", "Arms", "Legs"),
                    title = "Body Measurements"
                )
            }
        }
    }
}

@Composable
private fun HistoryTab(
    items: List<WorkoutHistoryDisplay>,
    onNavigateToWorkoutDetail: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.workout_history),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (items.isEmpty()) {
            item {
                FeaturePlaceholderCard(
                    title = "No Workout History",
                    description = "Complete your first workout to see your history here.",
                    icon = Icons.Default.History
                )
            }
        } else {
            items(
                items = items,
                key = { it.id },
                contentType = { _ -> "history" }
            ) { item ->
                val meta = buildString {
                    if (item.sets.isNotBlank()) append(item.sets)
                    if (item.duration.isNotBlank()) {
                        if (isNotEmpty()) append(" • ")
                        append(item.duration)
                    }
                }.ifBlank { null }
                com.chilluminati.rackedup.presentation.components.RecentWorkoutCard(
                    workoutName = item.title,
                    // Split program title and day if present in the title
                    programDay = item.title.split(" - ").getOrNull(1),
                    date = item.date,
                    metaText = meta,
                    onClick = { onNavigateToWorkoutDetail(item.id) }
                )
            }
        }
    }
}

@Composable
private fun ProgressSummaryCard(
    title: String,
    value: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.width(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PersonalRecordCard(
    exerciseName: String,
    weight: String,
    date: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
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
                    text = exerciseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = weight,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
