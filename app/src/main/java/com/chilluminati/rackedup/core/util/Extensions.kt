package com.chilluminati.rackedup.core.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Utility extensions for the app
 */

// Date extensions
fun Date.toFormattedString(pattern: String = "MMM dd, yyyy"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

fun Long.toDateString(): String {
    return Date(this).toFormattedString()
}

// Number extensions
fun Double.toOneDecimalPlace(): Double {
    return (this * 10.0).roundToInt() / 10.0
}

fun Double.toTwoDecimalPlaces(): Double {
    return (this * 100.0).roundToInt() / 100.0
}

fun Float.toOneDecimalPlace(): Float {
    return (this * 10.0f).roundToInt() / 10.0f
}

// Weight conversion extensions
fun Double.lbsToKg(): Double = this * 0.453592
fun Double.kgToLbs(): Double = this * 2.20462

// Compact number formatting (e.g., 1.2k, 3.4M). Avoids using "k" for small values like 0
fun Double.formatCompact(): String {
    val absValue = kotlin.math.abs(this)
    return when {
        absValue >= 1_000_000 -> {
            val millions = this / 1_000_000.0
            val rounded = (millions * 10.0).roundToInt() / 10.0
            if (rounded % 1.0 == 0.0) "${rounded.toInt()}M" else "${rounded}M"
        }
        absValue >= 1_000 -> {
            val thousands = this / 1_000.0
            val rounded = (thousands * 10.0).roundToInt() / 10.0
            if (rounded % 1.0 == 0.0) "${rounded.toInt()}k" else "${rounded}k"
        }
        else -> {
            if (this % 1.0 == 0.0) this.toInt().toString() else this.toOneDecimalPlace().toString()
        }
    }
}

// Time formatting extensions
fun Int.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    
    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else -> String.format("%02d:%02d", minutes, seconds)
    }
}

fun Long.formatDuration(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    
    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m"
        else -> "< 1m"
    }
}

// String extensions
fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar { 
            if (it.isLowerCase()) it.titlecase() else it.toString() 
        }
    }
}

// One Rep Max calculations
fun calculateOneRepMax(weight: Double, reps: Int): Double {
    return when {
        reps == 1 -> weight
        reps in 2..10 -> weight * (1 + reps / 30.0) // Epley formula
        else -> weight * (reps / (reps - 1.0)).pow(1.5) // Modified formula for higher reps
    }
}

// Volume calculations
fun calculateVolume(weight: Double, reps: Int, sets: Int): Double {
    return weight * reps * sets
}

// Training intensity
fun calculateIntensity(workingWeight: Double, oneRepMax: Double): Double {
    return if (oneRepMax > 0) (workingWeight / oneRepMax) * 100 else 0.0
}

// Resource helpers for Compose
@Composable
fun getStringResource(resId: Int): String {
    return LocalContext.current.getString(resId)
}

// Collection extensions
fun <T> List<T>.takeIfNotEmpty(): List<T>? = if (isEmpty()) null else this

fun <T> Collection<T>.isNotEmpty(): Boolean = !isEmpty()

// Safe navigation extensions
inline fun <T> T?.ifNotNull(action: (T) -> Unit) {
    if (this != null) action(this)
}

// Progress calculation
fun calculateProgress(current: Double, target: Double): Float {
    return if (target > 0) (current / target).toFloat().coerceIn(0f, 1f) else 0f
}

// Keyboard handling extensions
@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}
