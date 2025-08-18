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
fun ProgressionChart(
    improvementData: List<Pair<Date, Double>>,
    progressionRateData: List<Pair<Date, Double>>,
    modifier: Modifier = Modifier,
    title: String = "Progression Tracking",
    weightUnit: String = "kg"
) {
    val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val errorColor = MaterialTheme.colorScheme.error.toArgb()
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

            if (improvementData.isEmpty() && progressionRateData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Complete multiple workouts to see progression trends",
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
                            description.isEnabled = false
                            setTouchEnabled(true)
                            isDragEnabled = true
                            setScaleEnabled(true)
                            setPinchZoom(true)
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            
                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawGridLines(false)
                                textColor = onSurfaceColor
                                valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        val index = value.toInt()
                                        val data = if (improvementData.isNotEmpty()) improvementData else progressionRateData
                                        return if (index >= 0 && index < data.size) {
                                            dateFormat.format(data[index].first)
                                        } else ""
                                    }
                                }
                            }
                            
                            axisLeft.apply {
                                setDrawGridLines(true)
                                textColor = onSurfaceColor
                                valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return "${value.toInt()}%"
                                    }
                                }
                            }
                            
                            axisRight.isEnabled = false
                            legend.isEnabled = true
                            legend.textColor = onSurfaceColor
                        }
                    },
                    update = { chart ->
                        val dataSets = mutableListOf<LineDataSet>()
                        
                        // Improvement percentage line
                        if (improvementData.isNotEmpty()) {
                            val improvementEntries = improvementData.mapIndexed { index, (_, value) ->
                                Entry(index.toFloat(), value.toFloat())
                            }
                            
                            val improvementDataSet = LineDataSet(improvementEntries, "Improvement (%)").apply {
                                color = primaryColor
                                setCircleColor(primaryColor)
                                lineWidth = 2f
                                circleRadius = 4f
                                setDrawFilled(false)
                                valueTextColor = onSurfaceColor
                                setDrawValues(false)
                            }
                            dataSets.add(improvementDataSet)
                        }
                        
                        // Progression rate line (weekly improvement)
                        if (progressionRateData.isNotEmpty()) {
                            val progressionEntries = progressionRateData.mapIndexed { index, (_, value) ->
                                Entry(index.toFloat(), value.toFloat())
                            }
                            
                            val progressionDataSet = LineDataSet(progressionEntries, "Weekly Progress (%)").apply {
                                color = errorColor
                                setCircleColor(errorColor)
                                lineWidth = 2f
                                circleRadius = 4f
                                setDrawFilled(false)
                                valueTextColor = onSurfaceColor
                                setDrawValues(false)
                            }
                            dataSets.add(progressionDataSet)
                        }
                        
                        chart.data = if (dataSets.isNotEmpty()) LineData(dataSets as List<com.github.mikephil.charting.interfaces.datasets.ILineDataSet>) else null
                        chart.invalidate()
                    }
                )
            }
            
            // Legend
            if (improvementData.isNotEmpty() || progressionRateData.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (improvementData.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                modifier = Modifier.size(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Improvement",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    if (progressionRateData.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                modifier = Modifier.size(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Weekly Progress",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
