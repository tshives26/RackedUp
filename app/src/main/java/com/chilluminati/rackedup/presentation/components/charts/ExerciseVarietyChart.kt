package com.chilluminati.rackedup.presentation.components.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExerciseVarietyChart(
    varietyData: List<Pair<Date, Int>>,
    muscleGroupData: List<Pair<Date, Int>>,
    modifier: Modifier = Modifier,
    title: String = "Workout Variety"
) {
    val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val secondaryColor = MaterialTheme.colorScheme.secondary.toArgb()
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (varietyData.isEmpty() && muscleGroupData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Complete workouts to see exercise variety",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    factory = { context ->
                        BarChart(context).apply {
                            description.isEnabled = false
                            setTouchEnabled(true)
                            setScaleEnabled(false)
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)

                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawGridLines(false)
                                textColor = onSurfaceColor
                                valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        val index = value.toInt()
                                        val data = if (varietyData.isNotEmpty()) varietyData else muscleGroupData
                                        return if (index >= 0 && index < data.size) {
                                            dateFormat.format(data[index].first)
                                        } else ""
                                    }
                                }
                            }

                            axisLeft.apply {
                                setDrawGridLines(true)
                                textColor = onSurfaceColor
                                axisMinimum = 0f
                                granularity = 1f
                            }

                            axisRight.isEnabled = false
                            legend.isEnabled = true
                            legend.textColor = onSurfaceColor
                        }
                    },
                    update = { chart ->
                        val dataSets = mutableListOf<BarDataSet>()
                        
                        // Exercise variety bars (unique exercises per workout)
                        if (varietyData.isNotEmpty()) {
                            val varietyEntries = varietyData.mapIndexed { index, (_, value) ->
                                BarEntry(index.toFloat(), value.toFloat())
                            }
                            
                            val varietyDataSet = BarDataSet(varietyEntries, "Unique Exercises").apply {
                                color = primaryColor
                                valueTextColor = onSurfaceColor
                                setDrawValues(false)
                            }
                            dataSets.add(varietyDataSet)
                        }
                        
                        // Muscle group variety bars
                        if (muscleGroupData.isNotEmpty()) {
                            val muscleEntries = muscleGroupData.mapIndexed { index, (_, value) ->
                                BarEntry(index.toFloat(), value.toFloat())
                            }
                            
                            val muscleDataSet = BarDataSet(muscleEntries, "Muscle Groups").apply {
                                color = secondaryColor
                                valueTextColor = onSurfaceColor
                                setDrawValues(false)
                            }
                            dataSets.add(muscleDataSet)
                        }
                        
                        chart.data = if (dataSets.isNotEmpty()) BarData(dataSets as List<com.github.mikephil.charting.interfaces.datasets.IBarDataSet>) else null
                        chart.invalidate()
                    }
                )
            }
            
            // Legend
            if (varietyData.isNotEmpty() || muscleGroupData.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (varietyData.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                modifier = Modifier.size(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Exercises",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    if (muscleGroupData.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                modifier = Modifier.size(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Muscle Groups",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
