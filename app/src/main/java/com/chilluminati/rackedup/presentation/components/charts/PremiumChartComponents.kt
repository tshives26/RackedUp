package com.chilluminati.rackedup.presentation.components.charts

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlinx.coroutines.delay

/**
 * Premium chart components with enhanced visual appeal
 */

@Composable
fun PremiumProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 120.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 12.dp,
    animate: Boolean = true
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (animate) progress else progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "progressAnimation"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(size)
                .padding(strokeWidth / 2)
        ) {
            val center = Offset(this.size.width / 2, this.size.height / 2)
            val radius = (this.size.width - strokeWidth.toPx()) / 2
            
            // Background circle with gradient
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Gray.copy(alpha = 0.1f)
                    ),
                    radius = radius
                ),
                radius = radius,
                center = center,
                style = Stroke(strokeWidth.toPx())
            )
            
            // Progress arc with gradient
            if (animatedProgress > 0) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2),
                            Color(0xFF764ba2)
                        ),
                        center = center
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    ),
                    topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2),
                    size = androidx.compose.ui.geometry.Size(
                        this.size.width - strokeWidth.toPx(),
                        this.size.height - strokeWidth.toPx()
                    )
                )
            }
        }
        
        // Center content
        Text(
            text = "${(animatedProgress * 100).toInt()}%",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun PremiumBarChart(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier,
    maxValue: Float = data.maxOfOrNull { it.second } ?: 1f,
    animate: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "barChartTransition")
    val animationPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "barAnimationPhase"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            data.forEachIndexed { index, (label, value) ->
                val animatedValue by animateFloatAsState(
                    targetValue = if (animate) value else value,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "barValue$index"
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(0.3f)
                    )
                    
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .height(32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(animatedValue / maxValue)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                        )
                    }
                    
                    Text(
                        text = "${animatedValue.toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumLineChart(
    data: List<Float>,
    modifier: Modifier = Modifier,
    animate: Boolean = true
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (animate) 1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "lineProgress"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .shadow(8.dp, MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                if (data.isNotEmpty()) {
                    val maxValue = data.maxOrNull() ?: 1f
                    val minValue = data.minOrNull() ?: 0f
                    val range = maxValue - minValue
                    
                    val stepX = this.size.width / (data.size - 1).coerceAtLeast(1)
                    val points = data.mapIndexed { index, value ->
                        val x = index * stepX
                        val y = this.size.height - ((value - minValue) / range * this.size.height)
                        Offset(x, y)
                    }
                    
                    // Draw gradient background
                    val gradientPath = Path().apply {
                        moveTo(points.first().x, this@Canvas.size.height)
                        points.forEach { point ->
                            lineTo(point.x, point.y)
                        }
                        lineTo(points.last().x, this@Canvas.size.height)
                        close()
                    }
                    
                    drawPath(
                        path = gradientPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF667eea).copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
                    
                    // Draw line with animation
                    val animatedPoints = points.take((points.size * animatedProgress).toInt().coerceAtLeast(1))
                    if (animatedPoints.size > 1) {
                        val linePath = Path().apply {
                            moveTo(animatedPoints.first().x, animatedPoints.first().y)
                            animatedPoints.forEach { point ->
                                lineTo(point.x, point.y)
                            }
                        }
                        
                        drawPath(
                            path = linePath,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF667eea),
                                    Color(0xFF764ba2)
                                )
                            ),
                            style = Stroke(
                                width = 6.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                    
                    // Draw data points
                    animatedPoints.forEach { point ->
                        drawCircle(
                            color = Color(0xFF764ba2),
                            radius = 8.dp.toPx(),
                            center = point
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 4.dp.toPx(),
                            center = point
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedCounter(
    value: Int,
    modifier: Modifier = Modifier,
    suffix: String = "",
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineMedium
) {
    var animatedValue by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(value) {
        if (value > animatedValue) {
            val duration = 1000
            val steps = (value - animatedValue).coerceAtMost(50)
            val stepDuration = duration / steps
            
            repeat(steps) {
                delay(stepDuration.toLong())
                animatedValue += (value - animatedValue) / (steps - it).coerceAtLeast(1)
            }
            animatedValue = value
        } else {
            animatedValue = value
        }
    }
    
    Text(
        text = "$animatedValue$suffix",
        style = textStyle.copy(
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                )
            )
        ),
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun GlowingProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 8.dp,
    animate: Boolean = true
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (animate) progress else progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "glowingProgress"
    )
    
    val infiniteTransition = rememberInfiniteTransition(label = "glowTransition")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .shadow(4.dp, RoundedCornerShape(height / 2))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                            MaterialTheme.colorScheme.secondary.copy(alpha = glowAlpha),
                            MaterialTheme.colorScheme.tertiary.copy(alpha = glowAlpha)
                        )
                    ),
                    shape = RoundedCornerShape(height / 2)
                )
        )
    }
}
