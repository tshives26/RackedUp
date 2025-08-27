package com.chilluminati.rackedup.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chilluminati.rackedup.data.repository.VolumeBasedPersonalRecord
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VolumeBasedPersonalRecordsCard(
    personalRecords: List<VolumeBasedPersonalRecord>,
    modifier: Modifier = Modifier,
    title: String = "Personal Records",
    weightUnit: String = "kg",
    onNavigateToPRs: (() -> Unit)? = null
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    var showAllRecordsDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (personalRecords.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Personal Records Yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "Complete workouts to see your volume PRs",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                // Group by category and render in a non-scrollable container to avoid nested scrollables
                val recordsByCategory = personalRecords.groupBy { it.exerciseCategory }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    recordsByCategory.forEach { (category, records) ->
                        CategorySection(
                            category = category,
                            records = records,
                            dateFormat = dateFormat,
                            weightUnit = weightUnit,
                            onShowMore = { 
                                if (onNavigateToPRs != null) {
                                    onNavigateToPRs()
                                } else {
                                    showAllRecordsDialog = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Show all records dialog (fallback when navigation is not available)
    if (showAllRecordsDialog) {
        AllRecordsDialog(
            personalRecords = personalRecords,
            weightUnit = weightUnit,
            onDismiss = { showAllRecordsDialog = false }
        )
    }
}

@Composable
private fun CategorySection(
    category: String,
    records: List<VolumeBasedPersonalRecord>,
    dateFormat: SimpleDateFormat,
    weightUnit: String,
    onShowMore: () -> Unit
) {
    val displayRecords = records.take(3)
    val hasMoreRecords = records.size > 3
    
    Column {
        Text(
            text = category.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        displayRecords.forEach { record ->
            PersonalRecordItem(
                record = record,
                dateFormat = dateFormat,
                weightUnit = weightUnit
            )
        }
        
        if (hasMoreRecords) {
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = onShowMore,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Show more (${records.size - 3} more)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun PersonalRecordItem(
    record: VolumeBasedPersonalRecord,
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

@Composable
private fun AllRecordsDialog(
    personalRecords: List<VolumeBasedPersonalRecord>,
    weightUnit: String,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    val filteredRecords = remember(personalRecords, searchQuery) {
        if (searchQuery.isBlank()) {
            personalRecords
        } else {
            personalRecords.filter { record ->
                record.exerciseName.contains(searchQuery, ignoreCase = true) ||
                record.exerciseCategory.contains(searchQuery, ignoreCase = true) ||
                record.equipment.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    val recordsByCategory = remember(filteredRecords) {
        filteredRecords.groupBy { it.exerciseCategory }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "All Personal Records",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Search field
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
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Records list
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (filteredRecords.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (searchQuery.isBlank()) "No personal records found" else "No records match your search",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        recordsByCategory.forEach { (category, records) ->
                            item {
                                Text(
                                    text = category.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            items(records) { record ->
                                PersonalRecordItem(
                                    record = record,
                                    dateFormat = dateFormat,
                                    weightUnit = weightUnit
                                )
                            }
                            
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
