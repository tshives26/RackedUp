package com.chilluminati.rackedup.presentation.progress

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chilluminati.rackedup.presentation.components.*
import com.chilluminati.rackedup.presentation.components.charts.BodyMeasurementChart
import java.text.SimpleDateFormat
import java.util.*

/**
 * Comprehensive body measurement tracking screen
 */
@Composable
fun BodyMeasurementScreen(
    modifier: Modifier = Modifier,
    viewModel: BodyMeasurementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val measurements by viewModel.measurements.collectAsStateWithLifecycle()
    val weightMeasurements by viewModel.weightMeasurements.collectAsStateWithLifecycle()
    val bodyCompositionMeasurements by viewModel.bodyCompositionMeasurements.collectAsStateWithLifecycle()
    val circumferenceMeasurements by viewModel.circumferenceMeasurements.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val weightUnit by viewModel.weightUnit.collectAsStateWithLifecycle()
    val measurementsUnit by viewModel.measurementsUnit.collectAsStateWithLifecycle()

    var showAddMeasurementDialog by remember { mutableStateOf(false) }
    var selectedMeasurement by remember { mutableStateOf<com.chilluminati.rackedup.data.database.entity.BodyMeasurement?>(null) }

    // Handle error display
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            // Could show a snackbar here
            viewModel.clearError()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header section
        item {
            BodyMeasurementHeader(
                uiState = uiState,
                onAddMeasurement = { showAddMeasurementDialog = true }
            )
        }

        if (!uiState.hasData && !isLoading) {
            // Empty state
            item {
                BodyMeasurementEmptyState(
                    onAddMeasurement = { showAddMeasurementDialog = true }
                )
            }
        } else {
            // Quick stats cards
            item {
                BodyMeasurementQuickStats(
                    uiState = uiState,
                    weightUnit = weightUnit
                )
            }

            // Weight tracking section
            if (weightMeasurements.isNotEmpty()) {
                item {
                    BodyMeasurementSection(
                        title = "Weight Tracking",
                        icon = Icons.Default.MonitorWeight,
                        description = "Track your weight changes over time"
                    )
                }
                
                item {
                    WeightChartCard(
                        weightData = viewModel.getWeightChartData(),
                        weightUnit = weightUnit
                    )
                }
            }

            // Body composition section
            if (bodyCompositionMeasurements.isNotEmpty()) {
                item {
                    BodyMeasurementSection(
                        title = "Body Composition",
                        icon = Icons.Default.Analytics,
                        description = "Monitor body fat and muscle mass"
                    )
                }
                
                item {
                    BodyCompositionChartCard(
                        compositionData = viewModel.getBodyCompositionChartData()
                    )
                }
            }

            // Circumference measurements section
            if (circumferenceMeasurements.isNotEmpty()) {
                item {
                    BodyMeasurementSection(
                        title = "Body Measurements",
                        icon = Icons.Default.Straighten,
                        description = "Track body part circumferences"
                    )
                }
                
                item {
                    CircumferenceChartCard(
                        circumferenceData = viewModel.getCircumferenceChartData()
                    )
                }
            }

            // Recent measurements list
            if (measurements.isNotEmpty()) {
                item {
                    BodyMeasurementSection(
                        title = "Recent Measurements",
                        icon = Icons.Default.History,
                        description = "Your latest body measurements"
                    )
                }
                
                items(
                    items = measurements.take(10),
                    key = { it.id }
                ) { measurement ->
                    BodyMeasurementCard(
                        measurement = measurement,
                        onEdit = { selectedMeasurement = measurement },
                        onDelete = { viewModel.deleteMeasurement(measurement) }
                    )
                }
            }
        }
    }

    // Add measurement dialog
    if (showAddMeasurementDialog) {
        AddBodyMeasurementDialog(
            onDismiss = { showAddMeasurementDialog = false },
            onMeasurementAdded = { 
                showAddMeasurementDialog = false
                viewModel.loadMeasurements()
            },
            weightUnit = weightUnit,
            measurementsUnit = measurementsUnit
        )
    }

    // Edit measurement dialog
    selectedMeasurement?.let { measurement ->
        EditBodyMeasurementDialog(
            measurement = measurement,
            onDismiss = { selectedMeasurement = null },
            onMeasurementUpdated = { 
                selectedMeasurement = null
                viewModel.loadMeasurements()
            },
            weightUnit = weightUnit,
            measurementsUnit = measurementsUnit
        )
    }
}

@Composable
private fun BodyMeasurementHeader(
    uiState: BodyMeasurementUiState,
    onAddMeasurement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Body Measurements",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (uiState.hasData) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${uiState.totalMeasurements} measurements tracked",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        if (uiState.hasData) {
            IconButton(
                onClick = onAddMeasurement,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Measurement",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun BodyMeasurementEmptyState(
    onAddMeasurement: () -> Unit
) {
    FeaturePlaceholderCard(
        title = "No Body Measurements",
        description = "Start tracking your body measurements to monitor your fitness progress over time.",
        icon = Icons.Default.Person
    )
}

@Composable
private fun BodyMeasurementQuickStats(
    uiState: BodyMeasurementUiState,
    weightUnit: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Latest weight
        QuickStatCard(
            title = "Current Weight",
            value = uiState.latestWeight?.toString() ?: "0",
            subtitle = weightUnit.uppercase(),
            modifier = Modifier.weight(1f)
        )
        
        // Latest body fat
        QuickStatCard(
            title = "Body Fat",
            value = uiState.latestBodyFat?.toString() ?: "0",
            subtitle = "%",
            modifier = Modifier.weight(1f)
        )
        
        // Total measurements
        QuickStatCard(
            title = "Total Tracked",
            value = uiState.totalMeasurements.toString(),
            subtitle = "measurements",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BodyMeasurementSection(
    title: String,
    icon: ImageVector,
    description: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WeightChartCard(
    weightData: List<Pair<Date, Double>>,
    weightUnit: String
) {
    if (weightData.isNotEmpty()) {
        GlowingCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Weight Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Convert data for chart
                val chartData = weightData.map { (date, weight) ->
                    date to mapOf("Weight" to weight)
                }
                
                BodyMeasurementChart(
                    measurementData = chartData,
                    measurements = listOf("Weight"),
                    title = "Weight ($weightUnit)"
                )
            }
        }
    }
}

@Composable
private fun BodyCompositionChartCard(
    compositionData: List<Pair<Date, Map<String, Double>>>
) {
    if (compositionData.isNotEmpty()) {
        GlowingCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Body Composition",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                BodyMeasurementChart(
                    measurementData = compositionData,
                    measurements = compositionData.flatMap { it.second.keys }.distinct(),
                    title = "Body Fat % & Muscle Mass"
                )
            }
        }
    }
}

@Composable
private fun CircumferenceChartCard(
    circumferenceData: List<Pair<Date, Map<String, Double>>>
) {
    if (circumferenceData.isNotEmpty()) {
        GlowingCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Body Measurements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                BodyMeasurementChart(
                    measurementData = circumferenceData,
                    measurements = circumferenceData.flatMap { it.second.keys }.distinct(),
                    title = "Circumference Measurements"
                )
            }
        }
    }
}

@Composable
private fun BodyMeasurementCard(
    measurement: com.chilluminati.rackedup.data.database.entity.BodyMeasurement,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = measurement.measurementType,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${measurement.value} ${measurement.unit}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                measurement.bodyPart?.let { bodyPart ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = bodyPart,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateFormat.format(measurement.measuredAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
