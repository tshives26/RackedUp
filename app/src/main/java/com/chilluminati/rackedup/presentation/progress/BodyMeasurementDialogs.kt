package com.chilluminati.rackedup.presentation.progress

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chilluminati.rackedup.data.database.entity.BodyMeasurement
import com.chilluminati.rackedup.presentation.components.AppTextFieldDefaults
import java.text.SimpleDateFormat
import java.util.*

/**
 * Dialog for adding a new body measurement
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBodyMeasurementDialog(
    onDismiss: () -> Unit,
    onMeasurementAdded: () -> Unit,
    weightUnit: String,
    distanceUnit: String,
    viewModel: BodyMeasurementViewModel = hiltViewModel()
) {
    var selectedMeasurementType by remember { mutableStateOf("Weight") }
    var value by remember { mutableStateOf("") }
    var selectedBodyPart by remember { mutableStateOf<String?>(null) }
    var selectedMeasurementMethod by remember { mutableStateOf<String?>(null) }
    var notes by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date()) }
    
    var showMeasurementTypeDialog by remember { mutableStateOf(false) }
    var showBodyPartDialog by remember { mutableStateOf(false) }
    var showMeasurementMethodDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    val measurementTypes = listOf(
        "Weight",
        "Body Fat %",
        "Muscle Mass",
        "Circumference"
    )
    
    val bodyParts = listOf(
        "Chest",
        "Waist",
        "Hips",
        "Arms",
        "Forearms",
        "Thighs",
        "Calves",
        "Neck",
        "Shoulders"
    )
    
    val measurementMethods = listOf(
        "Scale",
        "Calipers",
        "DEXA Scan",
        "Tape Measure",
        "Bioelectrical Impedance",
        "Hydrostatic Weighing"
    )
    
    val unit = when (selectedMeasurementType) {
        "Weight" -> weightUnit
        "Circumference" -> distanceUnit
        else -> "%"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Body Measurement",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Measurement Type
                OutlinedTextField(
                    value = selectedMeasurementType,
                    onValueChange = { },
                    label = { Text("Measurement Type") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showMeasurementTypeDialog = true }) {
                            Icon(Icons.Default.ArrowDropDown, "Select type")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = AppTextFieldDefaults.outlinedColors()
                )
                
                // Value
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Value") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    suffix = { Text(unit) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = AppTextFieldDefaults.outlinedColors()
                )
                
                // Body Part (for circumference)
                if (selectedMeasurementType == "Circumference") {
                    OutlinedTextField(
                        value = selectedBodyPart ?: "",
                        onValueChange = { },
                        label = { Text("Body Part") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showBodyPartDialog = true }) {
                                Icon(Icons.Default.ArrowDropDown, "Select body part")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = AppTextFieldDefaults.outlinedColors()
                    )
                }
                
                // Measurement Method
                OutlinedTextField(
                    value = selectedMeasurementMethod ?: "",
                    onValueChange = { },
                    label = { Text("Measurement Method (Optional)") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showMeasurementMethodDialog = true }) {
                            Icon(Icons.Default.ArrowDropDown, "Select method")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = AppTextFieldDefaults.outlinedColors()
                )
                
                // Date
                OutlinedTextField(
                    value = dateFormatter.format(selectedDate),
                    onValueChange = { },
                    label = { Text("Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, "Select date")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = AppTextFieldDefaults.outlinedColors()
                )
                
                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    colors = AppTextFieldDefaults.outlinedColors()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val numericValue = value.toDoubleOrNull()
                    if (numericValue != null) {
                        viewModel.addMeasurement(
                            measurementType = selectedMeasurementType,
                            value = numericValue,
                            unit = unit,
                            bodyPart = selectedBodyPart,
                            measurementMethod = selectedMeasurementMethod,
                            notes = notes.takeIf { it.isNotBlank() },
                            measuredAt = selectedDate
                        )
                        onMeasurementAdded()
                    }
                },
                enabled = value.isNotBlank() && value.toDoubleOrNull() != null &&
                        (selectedMeasurementType != "Circumference" || selectedBodyPart != null)
            ) {
                Text("Add Measurement")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
    
    // Measurement Type Dialog
    if (showMeasurementTypeDialog) {
        AlertDialog(
            onDismissRequest = { showMeasurementTypeDialog = false },
            title = { Text("Select Measurement Type") },
            text = {
                Column {
                    measurementTypes.forEach { type ->
                        TextButton(
                            onClick = {
                                selectedMeasurementType = type
                                selectedBodyPart = null // Reset body part when type changes
                                showMeasurementTypeDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(type)
                        }
                    }
                }
            },
            confirmButton = { }
        )
    }
    
    // Body Part Dialog
    if (showBodyPartDialog) {
        AlertDialog(
            onDismissRequest = { showBodyPartDialog = false },
            title = { Text("Select Body Part") },
            text = {
                Column {
                    bodyParts.forEach { part ->
                        TextButton(
                            onClick = {
                                selectedBodyPart = part
                                showBodyPartDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(part)
                        }
                    }
                }
            },
            confirmButton = { }
        )
    }
    
    // Measurement Method Dialog
    if (showMeasurementMethodDialog) {
        AlertDialog(
            onDismissRequest = { showMeasurementMethodDialog = false },
            title = { Text("Select Measurement Method") },
            text = {
                Column {
                    measurementMethods.forEach { method ->
                        TextButton(
                            onClick = {
                                selectedMeasurementMethod = method
                                showMeasurementMethodDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(method)
                        }
                    }
                }
            },
            confirmButton = { }
        )
    }
    
    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDate.time
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Dialog for editing an existing body measurement
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBodyMeasurementDialog(
    measurement: BodyMeasurement,
    onDismiss: () -> Unit,
    onMeasurementUpdated: () -> Unit,
    weightUnit: String,
    distanceUnit: String,
    viewModel: BodyMeasurementViewModel = hiltViewModel()
) {
    var selectedMeasurementType by remember { mutableStateOf(measurement.measurementType) }
    var value by remember { mutableStateOf(measurement.value.toString()) }
    var selectedBodyPart by remember { mutableStateOf(measurement.bodyPart) }
    var selectedMeasurementMethod by remember { mutableStateOf(measurement.measurementMethod) }
    var notes by remember { mutableStateOf(measurement.notes ?: "") }
    var selectedDate by remember { mutableStateOf(measurement.measuredAt) }
    
    var showMeasurementTypeDialog by remember { mutableStateOf(false) }
    var showBodyPartDialog by remember { mutableStateOf(false) }
    var showMeasurementMethodDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    val measurementTypes = listOf(
        "Weight",
        "Body Fat %",
        "Muscle Mass",
        "Circumference"
    )
    
    val bodyParts = listOf(
        "Chest",
        "Waist",
        "Hips",
        "Arms",
        "Forearms",
        "Thighs",
        "Calves",
        "Neck",
        "Shoulders"
    )
    
    val measurementMethods = listOf(
        "Scale",
        "Calipers",
        "DEXA Scan",
        "Tape Measure",
        "Bioelectrical Impedance",
        "Hydrostatic Weighing"
    )
    
    val unit = when (selectedMeasurementType) {
        "Weight" -> weightUnit
        "Circumference" -> distanceUnit
        else -> "%"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Body Measurement",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Measurement Type
                OutlinedTextField(
                    value = selectedMeasurementType,
                    onValueChange = { },
                    label = { Text("Measurement Type") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showMeasurementTypeDialog = true }) {
                            Icon(Icons.Default.ArrowDropDown, "Select type")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = AppTextFieldDefaults.outlinedColors()
                )
                
                // Value
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Value") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    suffix = { Text(unit) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = AppTextFieldDefaults.outlinedColors()
                )
                
                // Body Part (for circumference)
                if (selectedMeasurementType == "Circumference") {
                    OutlinedTextField(
                        value = selectedBodyPart ?: "",
                        onValueChange = { },
                        label = { Text("Body Part") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showBodyPartDialog = true }) {
                                Icon(Icons.Default.ArrowDropDown, "Select body part")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = AppTextFieldDefaults.outlinedColors()
                    )
                }
                
                // Measurement Method
                OutlinedTextField(
                    value = selectedMeasurementMethod ?: "",
                    onValueChange = { },
                    label = { Text("Measurement Method (Optional)") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showMeasurementMethodDialog = true }) {
                            Icon(Icons.Default.ArrowDropDown, "Select method")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = AppTextFieldDefaults.outlinedColors()
                )
                
                // Date
                OutlinedTextField(
                    value = dateFormatter.format(selectedDate),
                    onValueChange = { },
                    label = { Text("Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, "Select date")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = AppTextFieldDefaults.outlinedColors()
                )
                
                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    colors = AppTextFieldDefaults.outlinedColors()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val numericValue = value.toDoubleOrNull()
                    if (numericValue != null) {
                        val updatedMeasurement = measurement.copy(
                            measurementType = selectedMeasurementType,
                            value = numericValue,
                            unit = unit,
                            bodyPart = selectedBodyPart,
                            measurementMethod = selectedMeasurementMethod,
                            notes = notes.takeIf { it.isNotBlank() },
                            measuredAt = selectedDate
                        )
                        viewModel.updateMeasurement(updatedMeasurement)
                        onMeasurementUpdated()
                    }
                },
                enabled = value.isNotBlank() && value.toDoubleOrNull() != null &&
                        (selectedMeasurementType != "Circumference" || selectedBodyPart != null)
            ) {
                Text("Update Measurement")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
    
    // Measurement Type Dialog
    if (showMeasurementTypeDialog) {
        AlertDialog(
            onDismissRequest = { showMeasurementTypeDialog = false },
            title = { Text("Select Measurement Type") },
            text = {
                Column {
                    measurementTypes.forEach { type ->
                        TextButton(
                            onClick = {
                                selectedMeasurementType = type
                                selectedBodyPart = null // Reset body part when type changes
                                showMeasurementTypeDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(type)
                        }
                    }
                }
            },
            confirmButton = { }
        )
    }
    
    // Body Part Dialog
    if (showBodyPartDialog) {
        AlertDialog(
            onDismissRequest = { showBodyPartDialog = false },
            title = { Text("Select Body Part") },
            text = {
                Column {
                    bodyParts.forEach { part ->
                        TextButton(
                            onClick = {
                                selectedBodyPart = part
                                showBodyPartDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(part)
                        }
                    }
                }
            },
            confirmButton = { }
        )
    }
    
    // Measurement Method Dialog
    if (showMeasurementMethodDialog) {
        AlertDialog(
            onDismissRequest = { showMeasurementMethodDialog = false },
            title = { Text("Select Measurement Method") },
            text = {
                Column {
                    measurementMethods.forEach { method ->
                        TextButton(
                            onClick = {
                                selectedMeasurementMethod = method
                                showMeasurementMethodDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(method)
                        }
                    }
                }
            },
            confirmButton = { }
        )
    }
    
    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDate.time
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
