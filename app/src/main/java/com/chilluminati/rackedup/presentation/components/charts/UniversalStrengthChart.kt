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
fun UniversalStrengthChart(
    relativeStrengthData: List<Pair<Date, Double>>,
    volumeLoadData: List<Pair<Date, Double>>,
    modifier: Modifier = Modifier,
    title: String = "Universal Strength Progress",
    weightUnit: String = "kg"
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

            if (relativeStrengthData.isEmpty() && volumeLoadData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Complete workouts to see your strength progress",
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
                                        val data = if (relativeStrengthData.isNotEmpty()) relativeStrengthData else volumeLoadData
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
                                        return "${value.toInt()}"
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
                        
                        // Relative Strength line (weight as % of body weight)
                        if (relativeStrengthData.isNotEmpty()) {
                            val relativeEntries = relativeStrengthData.mapIndexed { index, (_, value) ->
                                Entry(index.toFloat(), value.toFloat())
                            }
                            
                            val relativeDataSet = LineDataSet(relativeEntries, "Relative Strength (%)").apply {
                                color = primaryColor
                                setCircleColor(primaryColor)
                                lineWidth = 2f
                                circleRadius = 4f
                                setDrawFilled(false)
                                valueTextColor = onSurfaceColor
                                setDrawValues(false)
                            }
                            dataSets.add(relativeDataSet)
                        }
                        
                        // Volume Load line (total volume per workout)
                        if (volumeLoadData.isNotEmpty()) {
                            val volumeEntries = volumeLoadData.mapIndexed { index, (_, value) ->
                                Entry(index.toFloat(), value.toFloat())
                            }
                            
                            val volumeDataSet = LineDataSet(volumeEntries, "Volume Load ($weightUnit)").apply {
                                color = secondaryColor
                                setCircleColor(secondaryColor)
                                lineWidth = 2f
                                circleRadius = 4f
                                setDrawFilled(false)
                                valueTextColor = onSurfaceColor
                                setDrawValues(false)
                            }
                            dataSets.add(volumeDataSet)
                        }
                        
                        chart.data = if (dataSets.isNotEmpty()) LineData(dataSets as List<com.github.mikephil.charting.interfaces.datasets.ILineDataSet>) else null
                        chart.invalidate()
                    }
                )
            }
            
            // Legend
            if (relativeStrengthData.isNotEmpty() || volumeLoadData.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (relativeStrengthData.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                modifier = Modifier.size(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Relative Strength",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    if (volumeLoadData.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                modifier = Modifier.size(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Volume Load",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
