package com.chilluminati.rackedup.presentation.components.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun VolumeChart(
    volumeData: List<Pair<Date, Double>>,
    modifier: Modifier = Modifier,
    title: String = "Volume Trends",
    weightUnit: String = "kg"
) {
    val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val accentFillColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f).toArgb()
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    // Removed unused chartFillColor

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
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (volumeData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data available",
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
                                        return if (index >= 0 && index < volumeData.size) {
                                            dateFormat.format(volumeData[index].first)
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
                            legend.isEnabled = false
                        }
                    },
                    update = { chart ->
                        // Create entries from data
                        val entries = volumeData.mapIndexed { index, (_, volume) ->
                            Entry(index.toFloat(), volume.toFloat())
                        }
                        
                        // Create dataset
                        val dataSet = LineDataSet(entries, "Volume").apply {
                            color = primaryColor
                            setCircleColor(primaryColor)
                            lineWidth = 2f
                            circleRadius = 4f
                            setDrawFilled(true)
                            // Blend primary line with secondary accent fill for a modern look
                            fillColor = accentFillColor
                            valueTextColor = onSurfaceColor
                            setDrawValues(false)
                        }
                        
                        // Set data and refresh
                        chart.data = LineData(dataSet)
                        chart.invalidate()
                    }
                )
            }
        }
    }
}