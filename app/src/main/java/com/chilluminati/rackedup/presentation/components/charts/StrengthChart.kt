package com.chilluminati.rackedup.presentation.components.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StrengthChart(
    strengthData: List<Pair<Date, Map<String, Double>>>,
    exercises: List<String>,
    modifier: Modifier = Modifier,
    title: String = "Strength Progress (1RM)",
    weightUnit: String = "kg"
) {
    val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    val colors = listOf(
        MaterialTheme.colorScheme.primary.toArgb(),
        MaterialTheme.colorScheme.secondary.toArgb(),
        MaterialTheme.colorScheme.tertiary.toArgb(),
        MaterialTheme.colorScheme.error.toArgb()
    )
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

            if (strengthData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No strength data available",
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
                        LineChart(context).apply {
                            // Configure chart appearance
                            description.isEnabled = false
                            setTouchEnabled(true)
                            isDragEnabled = true
                            setScaleEnabled(true)
                            setPinchZoom(true)
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            
                            // Configure X axis
                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawGridLines(false)
                                textColor = onSurfaceColor
                                valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        val index = value.toInt()
                                        return if (index >= 0 && index < strengthData.size) {
                                            dateFormat.format(strengthData[index].first)
                                        } else ""
                                    }
                                }
                            }
                            
                            // Configure Y axis
                            axisLeft.apply {
                                setDrawGridLines(true)
                                textColor = onSurfaceColor
                                valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return "${value.toInt()} $weightUnit"
                                    }
                                }
                            }
                            
                            axisRight.isEnabled = false
                            
                            // Configure legend
                            legend.apply {
                                isEnabled = true
                                textColor = onSurfaceColor
                            }
                        }
                    },
                    update = { chart ->
                        val dataSets = mutableListOf<LineDataSet>()
                        
                        exercises.forEachIndexed { exerciseIndex, exercise ->
                            val entries = strengthData.mapIndexed { dataIndex, (_, values) ->
                                Entry(dataIndex.toFloat(), values[exercise]?.toFloat() ?: 0f)
                            }.filter { it.y > 0f } // Only show entries with actual data
                            
                            if (entries.isNotEmpty()) {
                                val dataSet = LineDataSet(entries, exercise).apply {
                                    color = colors[exerciseIndex % colors.size]
                                    setCircleColor(colors[exerciseIndex % colors.size])
                                    lineWidth = 2f
                                    circleRadius = 4f
                                    setDrawFilled(false)
                                    valueTextColor = onSurfaceColor
                                    setDrawValues(false)
                                }
                                dataSets.add(dataSet)
                            }
                        }
                        
                        // Set data and refresh
                        chart.data = if (dataSets.isNotEmpty()) LineData(dataSets as List<com.github.mikephil.charting.interfaces.datasets.ILineDataSet>) else null
                        chart.invalidate()
                    }
                )
            }
            
            // Legend for exercises
            if (exercises.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    exercises.take(4).forEachIndexed { index, exercise ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = Modifier.size(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = when (index % 4) {
                                        0 -> MaterialTheme.colorScheme.primary
                                        1 -> MaterialTheme.colorScheme.secondary
                                        2 -> MaterialTheme.colorScheme.tertiary
                                        else -> MaterialTheme.colorScheme.error
                                    }
                                )
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = exercise,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}