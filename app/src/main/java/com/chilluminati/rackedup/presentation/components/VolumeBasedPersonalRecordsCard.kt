package com.chilluminati.rackedup.presentation.components

import androidx.compose.foundation.layout.*
 
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
    weightUnit: String = "kg"
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
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
                            records = records.take(5), // Show top 5 per category
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
private fun CategorySection(
    category: String,
    records: List<VolumeBasedPersonalRecord>,
    dateFormat: SimpleDateFormat,
    weightUnit: String
) {
    Column {
        Text(
            text = category,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        records.forEach { record ->
            PersonalRecordItem(
                record = record,
                dateFormat = dateFormat,
                weightUnit = weightUnit
            )
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
                        text = record.equipment,
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
