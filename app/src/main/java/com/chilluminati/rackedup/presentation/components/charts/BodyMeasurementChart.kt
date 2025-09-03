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
fun BodyMeasurementChart(
    measurementData: List<Pair<Date, Map<String, Double>>>,
    measurements: List<String>,
    modifier: Modifier = Modifier,
    title: String = "Body Measurements"
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
            // Title removed to give chart more space

            if (measurementData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No measurement data available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
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
                                        return if (index >= 0 && index < measurementData.size) {
                                            dateFormat.format(measurementData[index].first)
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
                                        return "%.1f".format(value)
                                    }
                                }
                            }
                            
                            axisRight.isEnabled = false
                            
                            // Configure legend - disabled since it's obvious what the chart shows
                            legend.apply {
                                isEnabled = false
                            }
                        }
                    },
                    update = { chart ->
                        val dataSets = mutableListOf<LineDataSet>()
                        
                        measurements.forEachIndexed { measurementIndex, measurement ->
                            val entries = measurementData.mapIndexed { dataIndex, (_, values) ->
                                Entry(dataIndex.toFloat(), values[measurement]?.toFloat() ?: 0f)
                            }.filter { it.y > 0f } // Only show entries with actual data
                            
                            if (entries.isNotEmpty()) {
                                val dataSet = LineDataSet(entries, measurement).apply {
                                    color = colors[measurementIndex % colors.size]
                                    setCircleColor(colors[measurementIndex % colors.size])
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
            
            // No custom legend needed - the chart's built-in legend is sufficient
        }
    }
}