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
fun ConsistencyChart(
    data: List<com.chilluminati.rackedup.data.repository.ConsistencyDataPoint>,
    modifier: Modifier = Modifier,
    title: String = "Workout Consistency"
) {
    val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
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
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (data.isEmpty()) {
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
                                        return if (index >= 0 && index < data.size) {
                                            dateFormat.format(data[index].weekStart)
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
                            legend.isEnabled = false
                        }
                    },
                    update = { chart ->
                        val entries = data.mapIndexed { index, point ->
                            BarEntry(index.toFloat(), point.workoutCount.toFloat())
                        }

                        val dataSet = BarDataSet(entries, "Workouts").apply {
                            color = primaryColor
                            valueTextColor = onSurfaceColor
                            setDrawValues(false)
                        }

                        chart.data = BarData(dataSet)
                        chart.invalidate()
                    }
                )
            }
        }
    }
}


