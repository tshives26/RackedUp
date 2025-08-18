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
fun WorkoutDensityChart(
    densityData: List<Pair<Date, Double>>,
    efficiencyData: List<Pair<Date, Double>>,
    modifier: Modifier = Modifier,
    title: String = "Workout Efficiency",
    weightUnit: String = "kg"
) {
    val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val tertiaryColor = MaterialTheme.colorScheme.tertiary.toArgb()
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

            if (densityData.isEmpty() && efficiencyData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Complete timed workouts to see efficiency metrics",
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
                                        val data = if (densityData.isNotEmpty()) densityData else efficiencyData
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
                        
                        // Workout Density line (volume per minute)
                        if (densityData.isNotEmpty()) {
                            val densityEntries = densityData.mapIndexed { index, (_, value) ->
                                Entry(index.toFloat(), value.toFloat())
                            }
                            
                            val densityDataSet = LineDataSet(densityEntries, "Volume/min ($weightUnit)").apply {
                                color = primaryColor
                                setCircleColor(primaryColor)
                                lineWidth = 2f
                                circleRadius = 4f
                                setDrawFilled(false)
                                valueTextColor = onSurfaceColor
                                setDrawValues(false)
                            }
                            dataSets.add(densityDataSet)
                        }
                        
                        // Workout Efficiency line (sets per minute)
                        if (efficiencyData.isNotEmpty()) {
                            val efficiencyEntries = efficiencyData.mapIndexed { index, (_, value) ->
                                Entry(index.toFloat(), value.toFloat())
                            }
                            
                            val efficiencyDataSet = LineDataSet(efficiencyEntries, "Sets/min").apply {
                                color = tertiaryColor
                                setCircleColor(tertiaryColor)
                                lineWidth = 2f
                                circleRadius = 4f
                                setDrawFilled(false)
                                valueTextColor = onSurfaceColor
                                setDrawValues(false)
                            }
                            dataSets.add(efficiencyDataSet)
                        }
                        
                        chart.data = if (dataSets.isNotEmpty()) LineData(dataSets as List<com.github.mikephil.charting.interfaces.datasets.ILineDataSet>) else null
                        chart.invalidate()
                    }
                )
            }
            
            // Legend
            if (densityData.isNotEmpty() || efficiencyData.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (densityData.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                modifier = Modifier.size(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Volume/min",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    if (efficiencyData.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Card(
                                modifier = Modifier.size(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary
                                )
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sets/min",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
