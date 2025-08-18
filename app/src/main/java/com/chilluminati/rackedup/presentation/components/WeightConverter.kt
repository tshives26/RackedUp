package com.chilluminati.rackedup.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions

/**
 * Simple lb ↔ kg converter card for quick conversions.
 */
@Composable
fun WeightConverter(modifier: Modifier = Modifier) {
    var poundsText by remember { mutableStateOf("") }
    var kilogramsText by remember { mutableStateOf("") }
    var lastEdited by remember { mutableStateOf(EditedField.None) }
    var isExpanded by remember { mutableStateOf(false) }

    ToolCard(
        title = "Weight Converter",
        headerIcon = Icons.Default.SwapHoriz,
        isExpanded = isExpanded,
        onToggleExpand = { isExpanded = !isExpanded },
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            OutlinedTextField(
                value = poundsText,
                onValueChange = { newText ->
                    poundsText = newText
                    lastEdited = EditedField.Pounds
                    val value = newText.toDoubleOrNull()
                    kilogramsText = if (value == null && newText.isNotBlank()) {
                        kilogramsText // keep previous until valid
                    } else if (value == null) {
                        ""
                    } else {
                        formatDecimal(poundsToKilograms(value))
                    }
                },
                label = { Text("Pounds (lb)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = AppTextFieldDefaults.outlinedColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = kilogramsText,
                onValueChange = { newText ->
                    kilogramsText = newText
                    lastEdited = EditedField.Kilograms
                    val value = newText.toDoubleOrNull()
                    poundsText = if (value == null && newText.isNotBlank()) {
                        poundsText // keep previous until valid
                    } else if (value == null) {
                        ""
                    } else {
                        formatDecimal(kilogramsToPounds(value))
                    }
                },
                label = { Text("Kilograms (kg)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = AppTextFieldDefaults.outlinedColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private enum class EditedField { None, Pounds, Kilograms }

private fun poundsToKilograms(pounds: Double): Double = pounds * 0.45359237

private fun kilogramsToPounds(kilograms: Double): Double = kilograms / 0.45359237

private fun formatDecimal(value: Double, maxFractionDigits: Int = 3): String {
    val factor = Math.pow(10.0, maxFractionDigits.toDouble())
    val rounded = kotlin.math.round(value * factor) / factor
    // Avoid trailing .0 when not needed
    return if (rounded % 1.0 == 0.0) {
        rounded.toInt().toString()
    } else {
        rounded.toString()
    }
}


