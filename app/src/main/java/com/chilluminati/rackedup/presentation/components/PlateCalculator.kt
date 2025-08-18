package com.chilluminati.rackedup.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// Helper functions for bar type labeling and defaults
private fun barTypeLabel(type: String): String = when (type) {
    "Standard" -> "Standard (45 lbs)"
    "Women" -> "Women's (35 lbs)"
    "Technique" -> "Technique (15 lbs)"
    "Custom" -> "Custom"
    else -> type
}

private fun barTypeDefaultWeight(type: String): String? = when (type) {
    "Standard" -> "45"
    "Women" -> "35"
    "Technique" -> "15"
    else -> null
}

/**
 * Plate Calculator component for calculating weight plate combinations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlateCalculator(
    modifier: Modifier = Modifier
) {
    var targetWeight by remember { mutableStateOf("") }
    var barWeight by remember { mutableStateOf("45") }
    var selectedBarType by remember { mutableStateOf("Standard") }
    var isExpanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    
    val barTypes = listOf("Standard", "Women", "Technique", "Custom")
    val availablePlates = listOf(45.0, 35.0, 25.0, 10.0, 5.0, 2.5)
    val selectedPlates = remember { mutableStateMapOf<Double, Int>() }
    
    ToolCard(
        title = "Plate Calculator",
        headerIcon = Icons.Default.Calculate,
        isExpanded = isExpanded,
        onToggleExpand = { isExpanded = !isExpanded },
        modifier = modifier.fillMaxWidth()
    ) {
            // Body
                // Tab selection
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Calculate Plates") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Calculate Weight") }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                when (selectedTab) {
                    0 -> CalculatePlatesTab(
                        targetWeight = targetWeight,
                        onTargetWeightChange = { targetWeight = it },
                        selectedBarType = selectedBarType,
                        barWeight = barWeight,
                        onBarWeightChange = { barWeight = it },
                        barTypes = barTypes,
                        onBarTypeChange = { type ->
                            selectedBarType = type
                            barTypeDefaultWeight(type)?.let { default -> barWeight = default }
                        }
                    )
                    1 -> CalculateWeightTab(
                        selectedPlates = selectedPlates,
                        availablePlates = availablePlates,
                        barWeight = barWeight,
                        onBarWeightChange = { barWeight = it },
                        selectedBarType = selectedBarType,
                        barTypes = barTypes,
                        onBarTypeChange = { type ->
                            selectedBarType = type
                            barTypeDefaultWeight(type)?.let { default -> barWeight = default }
                        }
                    )
                }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalculatePlatesTab(
    targetWeight: String,
    onTargetWeightChange: (String) -> Unit,
    selectedBarType: String,
    barWeight: String,
    onBarWeightChange: (String) -> Unit,
    barTypes: List<String>,
    onBarTypeChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Target weight input
        OutlinedTextField(
            value = targetWeight,
            onValueChange = onTargetWeightChange,
            label = { Text("Target Weight (lbs)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = AppTextFieldDefaults.outlinedColors()
        )
        
        // Bar type selection
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = barTypeLabel(selectedBarType),
                onValueChange = { },
                readOnly = true,
                label = { Text("Bar Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                colors = AppTextFieldDefaults.outlinedColors()
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                barTypes.forEach { barType ->
                    DropdownMenuItem(
                        text = { Text(barTypeLabel(barType)) },
                        onClick = {
                            onBarTypeChange(barType)
                            expanded = false
                        }
                    )
                }
            }
        }
        
        // Bar weight input (only show for custom)
        if (selectedBarType == "Custom") {
            OutlinedTextField(
                value = barWeight,
                onValueChange = onBarWeightChange,
                label = { Text("Bar Weight (lbs)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = AppTextFieldDefaults.outlinedColors()
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Results
        PlateCalculatorResults(
            targetWeight = targetWeight,
            barWeight = barWeight
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalculateWeightTab(
    selectedPlates: MutableMap<Double, Int>,
    availablePlates: List<Double>,
    barWeight: String,
    onBarWeightChange: (String) -> Unit,
    selectedBarType: String,
    barTypes: List<String>,
    onBarTypeChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Bar type selection
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = barTypeLabel(selectedBarType),
                onValueChange = { },
                readOnly = true,
                label = { Text("Bar Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                colors = AppTextFieldDefaults.outlinedColors()
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                barTypes.forEach { barType ->
                    DropdownMenuItem(
                        text = { Text(barTypeLabel(barType)) },
                        onClick = {
                            onBarTypeChange(barType)
                            expanded = false
                        }
                    )
                }
            }
        }
        
        // Bar weight input (only show for custom)
        if (selectedBarType == "Custom") {
            OutlinedTextField(
                value = barWeight,
                onValueChange = onBarWeightChange,
                label = { Text("Bar Weight (lbs)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Select plates (per side):",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        
        // Plate selection
        Column {
            availablePlates.forEach { plateWeight ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${plateWeight} lbs",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                val currentCount = selectedPlates[plateWeight] ?: 0
                                if (currentCount > 0) {
                                    if (currentCount - 1 == 0) {
                                        selectedPlates.remove(plateWeight)
                                    } else {
                                        selectedPlates[plateWeight] = currentCount - 1
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Remove plate",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        
                        Text(
                            text = "${selectedPlates[plateWeight] ?: 0}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        
                        IconButton(
                            onClick = {
                                val currentCount = selectedPlates[plateWeight] ?: 0
                                selectedPlates[plateWeight] = currentCount + 1
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add plate",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Calculate total weight
        WeightCalculatorResults(
            selectedPlates = selectedPlates,
            barWeight = barWeight
        )
    }
}

@Composable
private fun PlateCalculatorResults(
    targetWeight: String,
    barWeight: String
) {
    val target = targetWeight.toDoubleOrNull() ?: 0.0
    val bar = barWeight.toDoubleOrNull() ?: 45.0
    
    if (target <= 0) {
        Text(
            text = "Enter a target weight to calculate plate combinations",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
        return
    }
    
    if (target <= bar) {
        Text(
            text = "Target weight must be greater than bar weight",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        return
    }
    
    val (achievableWeight, plateCombination) = calculateAchievableWeight(target, bar)
    
    Column {
        // Target vs achievable weight
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Target weight:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "${String.format("%.1f", target)} lbs",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Achievable weight:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "${String.format("%.1f", achievableWeight)} lbs",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (achievableWeight == target) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
        
        if (achievableWeight != target) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Difference:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "${String.format("%.1f", target - achievableWeight)} lbs",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Plates needed (per side):",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        if (plateCombination.isEmpty()) {
            Text(
                text = "No plates needed",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )
        } else {
            Column {
                plateCombination.forEach { (weight, count) ->
                    if (count > 0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${weight} lbs",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "× $count",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Total weight summary
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (achievableWeight == target) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Weight:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (achievableWeight == target) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = "${String.format("%.1f", achievableWeight)} lbs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (achievableWeight == target) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@Composable
private fun WeightCalculatorResults(
    selectedPlates: MutableMap<Double, Int>,
    barWeight: String
) {
    val bar = barWeight.toDoubleOrNull() ?: 45.0
    val totalPlatesWeight = selectedPlates.entries.sumOf { (weight, count) -> weight * count * 2 } // *2 for both sides
    val totalWeight = bar + totalPlatesWeight
    
    Column {
        // Summary row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Plates weight:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "${String.format("%.1f", totalPlatesWeight)} lbs",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Bar weight:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "${String.format("%.1f", bar)} lbs",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Total weight summary
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Weight:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${String.format("%.1f", totalWeight)} lbs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/**
 * Calculate the optimal plate combination for a given weight
 */
private fun calculatePlateCombination(weightToLoad: Double): List<Pair<Double, Int>> {
    val availablePlates = listOf(45.0, 35.0, 25.0, 10.0, 5.0, 2.5)
    val weightPerSide = weightToLoad / 2.0
    val result = mutableListOf<Pair<Double, Int>>()
    
    var remainingWeight = weightPerSide
    
    for (plate in availablePlates) {
        val count = (remainingWeight / plate).toInt()
        if (count > 0) {
            result.add(Pair(plate, count))
            remainingWeight -= count * plate
        }
    }
    
    return result
}

/**
 * Calculate the achievable weight and plate combination
 */
private fun calculateAchievableWeight(targetWeight: Double, barWeight: Double): Pair<Double, List<Pair<Double, Int>>> {
    val availablePlates = listOf(45.0, 35.0, 25.0, 10.0, 5.0, 2.5)
    val weightToLoad = targetWeight - barWeight
    
    if (weightToLoad <= 0) {
        return Pair(barWeight, emptyList())
    }
    
    val weightPerSide = weightToLoad / 2.0
    val result = mutableListOf<Pair<Double, Int>>()
    
    var remainingWeight = weightPerSide
    
    for (plate in availablePlates) {
        val count = (remainingWeight / plate).toInt()
        if (count > 0) {
            result.add(Pair(plate, count))
            remainingWeight -= count * plate
        }
    }
    
    // Calculate the actual achievable weight
    val actualPlatesWeight = result.sumOf { (weight, count) -> weight * count * 2 } // *2 for both sides
    val actualTotalWeight = barWeight + actualPlatesWeight
    
    return Pair(actualTotalWeight, result)
}
