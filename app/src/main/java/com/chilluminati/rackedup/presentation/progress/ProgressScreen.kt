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
import com.chilluminati.rackedup.presentation.components.charts.UniversalStrengthChart
import com.chilluminati.rackedup.presentation.components.charts.WorkoutDensityChart
import com.chilluminati.rackedup.presentation.components.charts.ProgressionChart
import com.chilluminati.rackedup.presentation.components.charts.ExerciseVarietyChart
import com.chilluminati.rackedup.presentation.components.QuickStatCard
import com.chilluminati.rackedup.presentation.components.VolumeBasedPersonalRecordsCard
import com.chilluminati.rackedup.data.repository.VolumeBasedPersonalRecord
import com.chilluminati.rackedup.presentation.workouts.WorkoutsViewModel
import com.chilluminati.rackedup.presentation.progress.BodyMeasurementScreen
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import com.chilluminati.rackedup.core.util.formatCompact
import com.chilluminati.rackedup.presentation.components.GradientBackground
import com.chilluminati.rackedup.presentation.components.GlassmorphismCard

/**
 * Progress screen showing analytics, charts, and workout history
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    onNavigateToWorkoutDetail: (Long) -> Unit,
    onEditWorkout: (Long) -> Unit = {},
    onDeleteWorkout: (Long) -> Unit = {},
    onNavigateToWorkoutEdit: (Long) -> Unit = {},
    onNavigateToPRs: (() -> Unit)? = null,
    viewModel: ProgressViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    initialTab: String? = null
) {
    val weightUnit by viewModel.weightUnit.collectAsStateWithLifecycle(initialValue = "lbs")
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = remember { listOf("Overview", "Body", "PRs", "History") }

    // Handle initial tab selection
    LaunchedEffect(initialTab) {
        selectedTab = when (initialTab?.lowercase()) {
            "overview" -> 0
            "body" -> 1
            "records" -> 2
            "history" -> 3
            else -> 0
        }
    }

    // Create a callback to switch to PRs tab internally
    val switchToPRsTab = remember {
        { selectedTab = 2 }
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
    val personalRecords by viewModel.personalRecords.collectAsStateWithLifecycle()
    val workoutHistoryDisplay by viewModel.workoutHistoryDisplay.collectAsStateWithLifecycle()
    val weeklyStats by viewModel.weeklyStats.collectAsStateWithLifecycle()
    val monthlyStats by viewModel.monthlyStats.collectAsStateWithLifecycle()
    val currentStreak by viewModel.currentStreak.collectAsStateWithLifecycle()
    val consistencyData by viewModel.consistencyData.collectAsStateWithLifecycle()
    
    // Universal progress metrics
    val universalStrengthData by viewModel.universalStrengthData.collectAsStateWithLifecycle()
    val volumeLoadData by viewModel.volumeLoadData.collectAsStateWithLifecycle()
    val workoutDensityData by viewModel.workoutDensityData.collectAsStateWithLifecycle()
    val workoutEfficiencyData by viewModel.workoutEfficiencyData.collectAsStateWithLifecycle()
    val progressionData by viewModel.progressionData.collectAsStateWithLifecycle()
    val weeklyProgressData by viewModel.weeklyProgressData.collectAsStateWithLifecycle()
    val exerciseVarietyData by viewModel.exerciseVarietyData.collectAsStateWithLifecycle()
    val muscleGroupVarietyData by viewModel.muscleGroupVarietyData.collectAsStateWithLifecycle()
    val volumeBasedPersonalRecords by viewModel.volumeBasedPersonalRecords.collectAsStateWithLifecycle()
    
    // State for delete confirmation dialog
    var showDeleteDialog by remember { mutableStateOf(false) }
    var workoutToDelete by remember { mutableStateOf<Long?>(null) }

    GradientBackground(
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
        ProgressHeader(
            volumeData = volumeData,
            strengthData = strengthData,
            personalRecords = personalRecords,
            weightUnit = weightUnit,
            weeklyStats = weeklyStats,
            currentStreak = currentStreak,
            universalStrengthData = universalStrengthData,
            volumeLoadData = volumeLoadData,
            workoutDensityData = workoutDensityData,
            volumeBasedPersonalRecords = volumeBasedPersonalRecords
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
                weightUnit = weightUnit,
                weeklyStats = weeklyStats,
                monthlyStats = monthlyStats,
                consistencyData = consistencyData,
                universalStrengthData = universalStrengthData,
                volumeLoadData = volumeLoadData,
                workoutDensityData = workoutDensityData,
                workoutEfficiencyData = workoutEfficiencyData,
                progressionData = progressionData,
                weeklyProgressData = weeklyProgressData,
                exerciseVarietyData = exerciseVarietyData,
                muscleGroupVarietyData = muscleGroupVarietyData,
                                 volumeBasedPersonalRecords = volumeBasedPersonalRecords,
                 switchToPRsTab = switchToPRsTab
            )
            1 -> BodyProgressTab()
            2 -> PersonalRecordsTab(
                volumeBasedPersonalRecords = volumeBasedPersonalRecords,
                weightUnit = weightUnit
            )
            3 -> HistoryTab(
                items = workoutHistoryDisplay,
                weightUnit = weightUnit,
                onNavigateToWorkoutDetail = onNavigateToWorkoutDetail,
                onEditWorkout = { workoutId ->
                    // Navigate to workout detail screen for editing
                    onNavigateToWorkoutEdit(workoutId)
                },
                onDeleteWorkout = { workoutId ->
                    workoutToDelete = workoutId
                    showDeleteDialog = true
                }
            )
        }
        
        // Delete confirmation dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Workout") },
                text = { Text("Are you sure you want to delete this workout? This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            workoutToDelete?.let { workoutId ->
                                viewModel.deleteWorkout(workoutId)
                            }
                            showDeleteDialog = false
                            workoutToDelete = null
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            workoutToDelete = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
    }
}

@Composable
private fun ProgressHeader(
    volumeData: List<Pair<Date, Double>>,
    strengthData: List<Pair<Date, Map<String, Double>>>,
    personalRecords: List<com.chilluminati.rackedup.data.database.entity.PersonalRecord>,
    weightUnit: String,
    weeklyStats: WeeklyStats,
    currentStreak: Int,
    universalStrengthData: List<Pair<Date, Double>>,
    volumeLoadData: List<Pair<Date, Double>>,
    workoutDensityData: List<Pair<Date, Double>>,
    volumeBasedPersonalRecords: List<com.chilluminati.rackedup.data.repository.VolumeBasedPersonalRecord>
) {
    val hasData = volumeData.isNotEmpty() || strengthData.isNotEmpty() || personalRecords.isNotEmpty() || 
                  universalStrengthData.isNotEmpty() || volumeLoadData.isNotEmpty() || workoutDensityData.isNotEmpty() ||
                  volumeBasedPersonalRecords.isNotEmpty()
    
    GlassmorphismCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        backgroundAlpha = 0.15f
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "This Week",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            if (!hasData) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No progress data yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "Complete your first workout to see your progress",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
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
    weightUnit: String,
    weeklyStats: WeeklyStats,
    monthlyStats: MonthlyStats,
    consistencyData: List<com.chilluminati.rackedup.data.repository.ConsistencyDataPoint>,
    universalStrengthData: List<Pair<Date, Double>>,
    volumeLoadData: List<Pair<Date, Double>>,
    workoutDensityData: List<Pair<Date, Double>>,
    workoutEfficiencyData: List<Pair<Date, Double>>,
    progressionData: List<Pair<Date, Double>>,
    weeklyProgressData: List<Pair<Date, Double>>,
    exerciseVarietyData: List<Pair<Date, Int>>,
    muscleGroupVarietyData: List<Pair<Date, Int>>,
    volumeBasedPersonalRecords: List<com.chilluminati.rackedup.data.repository.VolumeBasedPersonalRecord>,
    switchToPRsTab: () -> Unit
) {
    val hasData = volumeData.isNotEmpty() || strengthData.isNotEmpty() || personalRecords.isNotEmpty() || 
                  universalStrengthData.isNotEmpty() || volumeLoadData.isNotEmpty() || workoutDensityData.isNotEmpty() ||
                  volumeBasedPersonalRecords.isNotEmpty()
    
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
                text = "Quick Stats",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (!hasData) {
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

        if (!hasData) {
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
                UniversalStrengthChart(
                    relativeStrengthData = universalStrengthData,
                    volumeLoadData = volumeLoadData,
                    modifier = Modifier,
                    title = "Universal Strength Progress",
                    weightUnit = weightUnit
                )
            }

            item {
                WorkoutDensityChart(
                    densityData = workoutDensityData,
                    efficiencyData = workoutEfficiencyData,
                    modifier = Modifier,
                    title = "Workout Efficiency",
                    weightUnit = weightUnit
                )
            }

            item {
                ProgressionChart(
                    improvementData = progressionData,
                    progressionRateData = weeklyProgressData,
                    modifier = Modifier,
                    title = "Progression Tracking"
                )
            }

            item {
                ExerciseVarietyChart(
                    varietyData = exerciseVarietyData,
                    muscleGroupData = muscleGroupVarietyData,
                    modifier = Modifier,
                    title = "Workout Variety"
                )
            }

            item {
                ConsistencyChart(data = consistencyData, modifier = Modifier, title = "Consistency (last 12 weeks)")
            }
        }

        // Personal Records
        item {
            VolumeBasedPersonalRecordsCard(
                personalRecords = volumeBasedPersonalRecords,
                modifier = Modifier,
                title = "Volume Personal Records",
                weightUnit = weightUnit,
                onNavigateToPRs = switchToPRsTab
            )
        }
    }
}



@Composable
private fun PersonalRecordsTab(
    volumeBasedPersonalRecords: List<com.chilluminati.rackedup.data.repository.VolumeBasedPersonalRecord>,
    weightUnit: String
) {
    var searchQuery by remember { mutableStateOf("") }
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    val filteredRecords = remember(volumeBasedPersonalRecords, searchQuery) {
        if (searchQuery.isBlank()) {
            volumeBasedPersonalRecords
        } else {
            volumeBasedPersonalRecords.filter { record ->
                record.exerciseName.contains(searchQuery, ignoreCase = true) ||
                record.exerciseCategory.contains(searchQuery, ignoreCase = true) ||
                record.equipment.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    val recordsByCategory = remember(filteredRecords) {
        filteredRecords.groupBy { it.exerciseCategory }
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Volume Personal Records",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search exercises...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true
            )
        }

        if (volumeBasedPersonalRecords.isEmpty()) {
            item {
                FeaturePlaceholderCard(
                    title = "No Personal Records Yet",
                    description = "Complete workouts with exercises to see your volume personal records here.",
                    icon = Icons.Default.Star
                )
            }
        } else if (filteredRecords.isEmpty()) {
            item {
                FeaturePlaceholderCard(
                    title = "No Records Found",
                    description = if (searchQuery.isBlank()) "No personal records found" else "No records match your search",
                    icon = Icons.Default.Search
                )
            }
        } else {
            // Create a flat list with category headers and record items
            val items = mutableListOf<Any>()
            recordsByCategory.forEach { (category, records) ->
                items.add(category) // Add category as header
                items.addAll(records) // Add all records in this category
                items.add("spacer") // Add spacer
            }
            
            items(items, key = { item ->
                when (item) {
                    is String -> "category_$item"
                    is VolumeBasedPersonalRecord -> "record_${item.exerciseId}_${item.achievedAt.time}"
                    else -> item.hashCode().toString()
                }
            }) { item ->
                when (item) {
                    is String -> {
                        if (item == "spacer") {
                            Spacer(modifier = Modifier.height(8.dp))
                        } else {
                            Text(
                                text = item.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    is VolumeBasedPersonalRecord -> {
                        PersonalRecordItem(
                            record = item,
                            dateFormat = dateFormat,
                            weightUnit = weightUnit
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BodyProgressTab() {
    BodyMeasurementScreen()
}

@Composable
private fun HistoryTab(
    items: List<WorkoutHistoryDisplay>,
    weightUnit: String,
    onNavigateToWorkoutDetail: (Long) -> Unit,
    onEditWorkout: (Long) -> Unit = {},
    onDeleteWorkout: (Long) -> Unit = {}
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
                    text = "Workout History",
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
                
                val volumeLabel = if (item.volume > 0.0) {
                    "Total volume: ${formatVolume(item.volume, weightUnit)}"
                } else null
                
                com.chilluminati.rackedup.presentation.components.RecentWorkoutCard(
                    workoutName = item.title,
                    // Split program title and day if present in the title
                    programDay = item.title.split(" - ").getOrNull(1),
                    date = item.date,
                    metaText = meta,
                    volumeLabel = volumeLabel,
                    onClick = { onNavigateToWorkoutDetail(item.id) },
                    onEdit = { onEditWorkout(item.id) },
                    onDelete = { onDeleteWorkout(item.id) },
                    showEditOptions = true
                )
            }
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

@Composable
private fun PersonalRecordItem(
    record: com.chilluminati.rackedup.data.repository.VolumeBasedPersonalRecord,
    dateFormat: SimpleDateFormat,
    weightUnit: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = record.exerciseName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (record.equipment.lowercase()) {
                            "barbell" -> Icons.Default.FitnessCenter
                            "dumbbell" -> Icons.Default.SportsGymnastics
                            "machine" -> Icons.Default.Build
                            "bodyweight" -> Icons.Default.Person
                            "cable" -> Icons.Default.Cable
                            else -> Icons.Default.FitnessCenter
                        },
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = record.equipment.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = dateFormat.format(record.achievedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${record.volume.toInt()} $weightUnit",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "${record.weight.toInt()} × ${record.reps}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
