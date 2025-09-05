package com.chilluminati.rackedup.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chilluminati.rackedup.data.database.entity.Exercise

/**
 * Exercise type-specific input field components for different exercise types
 */

/**
 * Helper function to determine the effective exercise type based on various fields
 * Based on Free Exercise DB categories: "strength", "cardio", "plyometrics", "stretching"
 */
fun determineEffectiveExerciseType(exercise: Exercise): String {
    val exerciseTypeNormalized = exercise.exerciseType.lowercase().trim()
    val categoryNormalized = exercise.category.lowercase().trim()
    val nameNormalized = exercise.name.lowercase()
    
    return when {
        // First priority: explicit exerciseType field
        exerciseTypeNormalized.isNotBlank() && exerciseTypeNormalized != "null" -> exerciseTypeNormalized
        
        // Second priority: Free Exercise DB categories
        categoryNormalized == "cardio" -> "cardio"
        categoryNormalized == "stretching" -> "stretching" 
        categoryNormalized == "plyometrics" -> "plyometrics"
        categoryNormalized == "strength" -> "strength"
        
        // Third priority: Category contains keywords
        categoryNormalized.contains("cardio") || categoryNormalized.contains("aerobic") -> "cardio"
        categoryNormalized.contains("stretch") || categoryNormalized.contains("flexibility") -> "stretching"
        categoryNormalized.contains("plyometric") || categoryNormalized.contains("explosive") -> "plyometrics"
        categoryNormalized.contains("isometric") || categoryNormalized.contains("static") -> "isometric"
        
        // Fourth priority: Name-based detection for isometric exercises
        nameNormalized.contains("plank") -> "isometric"
        nameNormalized.contains("hold") -> "isometric"
        nameNormalized.contains("squeeze") -> "isometric"
        nameNormalized.contains("wall sit") -> "isometric"
        nameNormalized.contains("bridge") && nameNormalized.contains("hold") -> "isometric"
        nameNormalized.contains("static") -> "isometric"
        nameNormalized.contains("isometric") -> "isometric"
        
        // Fifth priority: Name-based detection for cardio
        nameNormalized.contains("run") || nameNormalized.contains("jog") -> "cardio"
        nameNormalized.contains("bike") || nameNormalized.contains("cycle") -> "cardio"
        nameNormalized.contains("swim") -> "cardio"
        nameNormalized.contains("treadmill") || nameNormalized.contains("elliptical") -> "cardio"
        
        // Sixth priority: Name-based detection for plyometrics
        nameNormalized.contains("jump") -> "plyometrics"
        nameNormalized.contains("hop") -> "plyometrics"
        nameNormalized.contains("bound") -> "plyometrics"
        nameNormalized.contains("leap") -> "plyometrics"
        nameNormalized.contains("explosive") -> "plyometrics"
        nameNormalized.contains("box jump") -> "plyometrics"
        nameNormalized.contains("burpee") -> "plyometrics"
        
        // Seventh priority: Name-based detection for stretching
        nameNormalized.contains("stretch") -> "stretching"
        nameNormalized.contains("flexibility") -> "stretching"
        
        // Default: strength training
        else -> "strength"
    }
}

/**
 * Data class to hold exercise input values for different types
 */
data class ExerciseInputData(
    // Strength exercise fields
    val weight: String = "",
    val reps: String = "",
    val sets: String = "",
    
    // Cardio exercise fields
    val distance: String = "",
    val durationMinutes: String = "",
    val durationSeconds: String = "",
    val pace: String = "", // For speed tracking (min/km or min/mile)
    
    // Isometric exercise fields
    val holdDurationMinutes: String = "",
    val holdDurationSeconds: String = ""
)

/**
 * Get the appropriate input fields based on exercise type
 */
@Composable
fun ExerciseTypeInputFields(
    exercise: Exercise,
    inputData: ExerciseInputData,
    onInputChange: (ExerciseInputData) -> Unit,
    weightUnit: String = "lbs",
    distanceUnit: String = "miles",
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false
) {
    val effectiveType = determineEffectiveExerciseType(exercise)
    when (effectiveType) {
        "strength", "resistance", "weight training" -> StrengthExerciseInputs(
            inputData = inputData,
            onInputChange = onInputChange,
            weightUnit = weightUnit,
            modifier = modifier,
            isReadOnly = isReadOnly
        )
        "cardio", "cardiovascular", "aerobic", "endurance" -> CardioExerciseInputs(
            inputData = inputData,
            onInputChange = onInputChange,
            distanceUnit = distanceUnit,
            modifier = modifier,
            isReadOnly = isReadOnly
        )
        "plyometrics" -> PlyometricsExerciseInputs(
            inputData = inputData,
            onInputChange = onInputChange,
            modifier = modifier,
            isReadOnly = isReadOnly
        )
        "isometric", "static", "hold" -> IsometricExerciseInputs(
            inputData = inputData,
            onInputChange = onInputChange,
            modifier = modifier,
            isReadOnly = isReadOnly
        )
        "stretching", "flexibility" -> StretchingExerciseInputs(
            inputData = inputData,
            onInputChange = onInputChange,
            modifier = modifier,
            isReadOnly = isReadOnly
        )
        else -> StrengthExerciseInputs( // Default to strength for unknown types
            inputData = inputData,
            onInputChange = onInputChange,
            weightUnit = weightUnit,
            modifier = modifier,
            isReadOnly = isReadOnly
        )
    }
}

/**
 * Input fields for strength exercises (weight × reps)
 */
@Composable
private fun StrengthExerciseInputs(
    inputData: ExerciseInputData,
    onInputChange: (ExerciseInputData) -> Unit,
    weightUnit: String,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = inputData.weight,
            onValueChange = { value ->
                val filtered = value.filter { it.isDigit() || it == '.' }
                onInputChange(inputData.copy(weight = filtered))
            },
            label = { Text("Weight") },
            suffix = { Text(weightUnit) },
            placeholder = { Text("0") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            readOnly = isReadOnly,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = AppTextFieldDefaults.outlinedColors()
        )
        
        OutlinedTextField(
            value = inputData.reps,
            onValueChange = { value ->
                val filtered = value.filter { it.isDigit() }
                onInputChange(inputData.copy(reps = filtered))
            },
            label = { Text("Reps") },
            placeholder = { Text("0") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            readOnly = isReadOnly,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = AppTextFieldDefaults.outlinedColors()
        )
    }
}

/**
 * Input fields for cardio exercises (distance, duration, pace)
 */
@Composable
private fun CardioExerciseInputs(
    inputData: ExerciseInputData,
    onInputChange: (ExerciseInputData) -> Unit,
    distanceUnit: String,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Distance input
        OutlinedTextField(
            value = inputData.distance,
            onValueChange = { value ->
                val filtered = value.filter { it.isDigit() || it == '.' }
                onInputChange(inputData.copy(distance = filtered))
            },
            label = { Text("Distance") },
            suffix = { Text(distanceUnit) },
            placeholder = { Text("0.0") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            readOnly = isReadOnly,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = AppTextFieldDefaults.outlinedColors()
        )
        
        // Duration inputs (minutes and seconds)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputData.durationMinutes,
                onValueChange = { value ->
                    val filtered = value.filter { it.isDigit() }
                    onInputChange(inputData.copy(durationMinutes = filtered))
                },
                label = { Text("Minutes") },
                placeholder = { Text("0") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                readOnly = isReadOnly,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = AppTextFieldDefaults.outlinedColors()
            )
            
            OutlinedTextField(
                value = inputData.durationSeconds,
                onValueChange = { value ->
                    val filtered = value.filter { it.isDigit() }.take(2) // Max 59 seconds
                    val seconds = filtered.toIntOrNull() ?: 0
                    val validSeconds = if (seconds > 59) "59" else filtered
                    onInputChange(inputData.copy(durationSeconds = validSeconds))
                },
                label = { Text("Seconds") },
                placeholder = { Text("0") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                readOnly = isReadOnly,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = AppTextFieldDefaults.outlinedColors()
            )
        }
    }
}

/**
 * Input fields for plyometrics exercises (reps and sets, but focused on explosive movements)
 */
@Composable
private fun PlyometricsExerciseInputs(
    inputData: ExerciseInputData,
    onInputChange: (ExerciseInputData) -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Explosive Reps & Sets",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputData.sets,
                onValueChange = { value ->
                    val filtered = value.filter { it.isDigit() }
                    onInputChange(inputData.copy(sets = filtered))
                },
                label = { Text("Sets") },
                placeholder = { Text("3") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                readOnly = isReadOnly,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = AppTextFieldDefaults.outlinedColors()
            )
            
            OutlinedTextField(
                value = inputData.reps,
                onValueChange = { value ->
                    val filtered = value.filter { it.isDigit() }
                    onInputChange(inputData.copy(reps = filtered))
                },
                label = { Text("Reps") },
                placeholder = { Text("10") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                readOnly = isReadOnly,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = AppTextFieldDefaults.outlinedColors()
            )
        }
    }
}

/**
 * Input fields for isometric exercises (hold duration)
 */
@Composable
private fun IsometricExerciseInputs(
    inputData: ExerciseInputData,
    onInputChange: (ExerciseInputData) -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Hold Duration",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputData.holdDurationMinutes,
                onValueChange = { value ->
                    val filtered = value.filter { it.isDigit() }
                    onInputChange(inputData.copy(holdDurationMinutes = filtered))
                },
                label = { Text("Minutes") },
                placeholder = { Text("0") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                readOnly = isReadOnly,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = AppTextFieldDefaults.outlinedColors()
            )
            
            OutlinedTextField(
                value = inputData.holdDurationSeconds,
                onValueChange = { value ->
                    val filtered = value.filter { it.isDigit() }.take(2) // Max 59 seconds
                    val seconds = filtered.toIntOrNull() ?: 0
                    val validSeconds = if (seconds > 59) "59" else filtered
                    onInputChange(inputData.copy(holdDurationSeconds = validSeconds))
                },
                label = { Text("Seconds") },
                placeholder = { Text("0") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                readOnly = isReadOnly,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = AppTextFieldDefaults.outlinedColors()
            )
        }
    }
}

/**
 * Input fields for stretching exercises (hold duration)
 */
@Composable
private fun StretchingExerciseInputs(
    inputData: ExerciseInputData,
    onInputChange: (ExerciseInputData) -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Stretch Duration",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputData.holdDurationMinutes,
                onValueChange = { value ->
                    val filtered = value.filter { it.isDigit() }
                    onInputChange(inputData.copy(holdDurationMinutes = filtered))
                },
                label = { Text("Minutes") },
                placeholder = { Text("0") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                readOnly = isReadOnly,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = AppTextFieldDefaults.outlinedColors()
            )
            
            OutlinedTextField(
                value = inputData.holdDurationSeconds,
                onValueChange = { value ->
                    val filtered = value.filter { it.isDigit() }.take(2) // Max 59 seconds
                    val seconds = filtered.toIntOrNull() ?: 0
                    val validSeconds = if (seconds > 59) "59" else filtered
                    onInputChange(inputData.copy(holdDurationSeconds = validSeconds))
                },
                label = { Text("Seconds") },
                placeholder = { Text("30") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                readOnly = isReadOnly,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = AppTextFieldDefaults.outlinedColors()
            )
        }
    }
}

/**
 * Utility function to convert ExerciseInputData to database values
 */
fun ExerciseInputData.toDatabaseValues(exerciseType: String): Map<String, Any?> {
    return when (exerciseType.lowercase()) {
        "strength", "resistance", "weight training" -> mapOf(
            "weight" to weight.toDoubleOrNull(),
            "reps" to reps.toIntOrNull(),
            "sets" to sets.toIntOrNull()
        )
        "cardio", "cardiovascular", "aerobic", "endurance" -> {
            val totalDurationSeconds = (durationMinutes.toIntOrNull() ?: 0) * 60 + 
                                     (durationSeconds.toIntOrNull() ?: 0)
            mapOf(
                "distance" to distance.toDoubleOrNull(),
                "duration_seconds" to if (totalDurationSeconds > 0) totalDurationSeconds else null
            )
        }
        "plyometrics" -> mapOf(
            "reps" to reps.toIntOrNull(),
            "sets" to sets.toIntOrNull()
        )
        "isometric", "static", "hold" -> {
            val totalDurationSeconds = (holdDurationMinutes.toIntOrNull() ?: 0) * 60 + 
                                     (holdDurationSeconds.toIntOrNull() ?: 0)
            mapOf(
                "duration_seconds" to if (totalDurationSeconds > 0) totalDurationSeconds else null
            )
        }
        "stretching", "flexibility" -> {
            val totalDurationSeconds = (holdDurationMinutes.toIntOrNull() ?: 0) * 60 + 
                                     (holdDurationSeconds.toIntOrNull() ?: 0)
            mapOf(
                "duration_seconds" to if (totalDurationSeconds > 0) totalDurationSeconds else null
            )
        }
        else -> mapOf(
            "weight" to weight.toDoubleOrNull(),
            "reps" to reps.toIntOrNull()
        )
    }
}

/**
 * Companion object for ExerciseInputData factory methods
 */
object ExerciseInputDataFactory {
    /**
     * Create ExerciseInputData from database values
     */
    fun fromDatabaseValues(
        exerciseType: String,
        weight: Double? = null,
        reps: Int? = null,
        sets: Int? = null,
        distance: Double? = null,
        durationSeconds: Int? = null
    ): ExerciseInputData {
        return when (exerciseType.lowercase()) {
            "strength", "resistance", "weight training" -> ExerciseInputData(
                weight = weight?.toString() ?: "",
                reps = reps?.toString() ?: "",
                sets = sets?.toString() ?: ""
            )
            "cardio", "cardiovascular", "aerobic", "endurance" -> {
                val minutes = (durationSeconds ?: 0) / 60
                val seconds = (durationSeconds ?: 0) % 60
                ExerciseInputData(
                    distance = distance?.toString() ?: "",
                    durationMinutes = if (minutes > 0) minutes.toString() else "",
                    durationSeconds = if (seconds > 0) seconds.toString() else ""
                )
            }
            "plyometrics" -> ExerciseInputData(
                reps = reps?.toString() ?: "",
                sets = sets?.toString() ?: ""
            )
            "isometric", "static", "hold" -> {
                val minutes = (durationSeconds ?: 0) / 60
                val seconds = (durationSeconds ?: 0) % 60
                ExerciseInputData(
                    holdDurationMinutes = if (minutes > 0) minutes.toString() else "",
                    holdDurationSeconds = if (seconds > 0) seconds.toString() else ""
                )
            }
            "stretching", "flexibility" -> {
                val minutes = (durationSeconds ?: 0) / 60
                val seconds = (durationSeconds ?: 0) % 60
                ExerciseInputData(
                    holdDurationMinutes = if (minutes > 0) minutes.toString() else "",
                    holdDurationSeconds = if (seconds > 0) seconds.toString() else ""
                )
            }
            else -> ExerciseInputData(
                weight = weight?.toString() ?: "",
                reps = reps?.toString() ?: ""
            )
        }
    }
}
