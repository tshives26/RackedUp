package com.chilluminati.rackedup.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.presentation.components.AccentSectionHeader
import com.chilluminati.rackedup.presentation.components.AppTextFieldDefaults
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen for creating a new user profile
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
    onNavigateBack: () -> Unit,
    onProfileCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var selectedSex by remember { mutableStateOf<String?>(null) }
    var heightCm by remember { mutableStateOf("") }
    var weightKg by remember { mutableStateOf("") }
    var selectedActivityLevel by remember { mutableStateOf<String?>(null) }
    var selectedFitnessGoal by remember { mutableStateOf<String?>(null) }
    var selectedExperienceLevel by remember { mutableStateOf<String?>(null) }
    var makeActive by remember { mutableStateOf(true) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showSexDialog by remember { mutableStateOf(false) }
    var showActivityLevelDialog by remember { mutableStateOf(false) }
    var showFitnessGoalDialog by remember { mutableStateOf(false) }
    var showExperienceLevelDialog by remember { mutableStateOf(false) }

    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    val sexOptions = listOf("Male", "Female", "Other", "Prefer not to say")
    val activityLevels = listOf("Sedentary", "Lightly Active", "Moderately Active", "Very Active")
    val fitnessGoals = listOf("Lose Weight", "Gain Muscle", "Maintain", "Strength", "Endurance", "General Health")
    val experienceLevels = listOf("Beginner", "Intermediate", "Advanced")

    // Handle profile creation success
    LaunchedEffect(uiState.isProfileCreated) {
        if (uiState.isProfileCreated) {
            onProfileCreated()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.createProfile(
                                name = name.trim(),
                                email = email.trim().takeIf { it.isNotEmpty() },
                                birthday = selectedDate,
                                sex = selectedSex,
                                heightCm = heightCm.toDoubleOrNull(),
                                weightKg = weightKg.toDoubleOrNull(),
                                activityLevel = selectedActivityLevel,
                                fitnessGoal = selectedFitnessGoal,
                                experienceLevel = selectedExperienceLevel,
                                makeActive = makeActive
                            )
                        },
                        enabled = name.trim().isNotEmpty() && !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Create")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Basic Information Section
            AccentSectionHeader(title = "Basic Information")

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name *") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = name.trim().isEmpty() && uiState.hasTriedToSubmit,
                colors = AppTextFieldDefaults.colors()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = AppTextFieldDefaults.colors()
            )

            // Birthday Field
            OutlinedTextField(
                value = selectedDate?.let { dateFormatter.format(it) } ?: "",
                onValueChange = { },
                label = { Text("Birthday") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Cake, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Select date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                readOnly = true,
                enabled = false,
                colors = AppTextFieldDefaults.colors()
            )

            // Sex Selection
            OutlinedTextField(
                value = selectedSex ?: "",
                onValueChange = { },
                label = { Text("Sex") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Wc, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { showSexDialog = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSexDialog = true },
                readOnly = true,
                enabled = false,
                colors = AppTextFieldDefaults.colors()
            )

            // Physical Information Section
            AccentSectionHeader(title = "Physical Information")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = heightCm,
                    onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) heightCm = it },
                    label = { Text("Height (cm)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = AppTextFieldDefaults.colors()
                )

                OutlinedTextField(
                    value = weightKg,
                    onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) weightKg = it },
                    label = { Text("Weight (kg)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = AppTextFieldDefaults.colors()
                )
            }

            // Fitness Information Section
            AccentSectionHeader(title = "Fitness Information")

            // Activity Level
            OutlinedTextField(
                value = selectedActivityLevel ?: "",
                onValueChange = { },
                label = { Text("Activity Level") },
                leadingIcon = {
                    Icon(imageVector = Icons.AutoMirrored.Filled.DirectionsRun, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { showActivityLevelDialog = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showActivityLevelDialog = true },
                readOnly = true,
                enabled = false,
                colors = AppTextFieldDefaults.colors()
            )

            // Fitness Goal
            OutlinedTextField(
                value = selectedFitnessGoal ?: "",
                onValueChange = { },
                label = { Text("Fitness Goal") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { showFitnessGoalDialog = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showFitnessGoalDialog = true },
                readOnly = true,
                enabled = false,
                colors = AppTextFieldDefaults.colors()
            )

            // Experience Level
            OutlinedTextField(
                value = selectedExperienceLevel ?: "",
                onValueChange = { },
                label = { Text("Experience Level") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { showExperienceLevelDialog = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showExperienceLevelDialog = true },
                readOnly = true,
                enabled = false,
                colors = AppTextFieldDefaults.colors()
            )

            // Make Active Profile Toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Set as Active Profile",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Make this the current active profile after creation",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = makeActive,
                        onCheckedChange = { makeActive = it }
                    )
                }
            }

            // Error message
            uiState.message?.let { message ->
                if (uiState.isError) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = message,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = Date(millis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Selection Dialogs
    if (showSexDialog) {
        SelectionDialog(
            title = "Select Sex",
            options = sexOptions,
            selectedOption = selectedSex,
            onSelectionChange = { selectedSex = it },
            onDismiss = { showSexDialog = false }
        )
    }

    if (showActivityLevelDialog) {
        SelectionDialog(
            title = "Select Activity Level",
            options = activityLevels,
            selectedOption = selectedActivityLevel,
            onSelectionChange = { selectedActivityLevel = it },
            onDismiss = { showActivityLevelDialog = false }
        )
    }

    if (showFitnessGoalDialog) {
        SelectionDialog(
            title = "Select Fitness Goal",
            options = fitnessGoals,
            selectedOption = selectedFitnessGoal,
            onSelectionChange = { selectedFitnessGoal = it },
            onDismiss = { showFitnessGoalDialog = false }
        )
    }

    if (showExperienceLevelDialog) {
        SelectionDialog(
            title = "Select Experience Level",
            options = experienceLevels,
            selectedOption = selectedExperienceLevel,
            onSelectionChange = { selectedExperienceLevel = it },
            onDismiss = { showExperienceLevelDialog = false }
        )
    }
}

@Composable
private fun SelectionDialog(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onSelectionChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectionChange(option)
                                onDismiss()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = {
                                onSelectionChange(option)
                                onDismiss()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
