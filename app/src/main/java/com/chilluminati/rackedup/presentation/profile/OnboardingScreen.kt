package com.chilluminati.rackedup.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.sharp.*
import com.chilluminati.rackedup.presentation.components.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chilluminati.rackedup.R
import java.text.SimpleDateFormat
import java.util.*
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.app.AlarmManager
import android.content.Intent
import android.provider.Settings
import android.net.Uri

@OptIn(
    ExperimentalMaterial3Api::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class
)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val onboardingState by viewModel.onboardingState.collectAsState()
    val settingsUi by settingsViewModel.uiState.collectAsState()

    var step by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        // After notification permission result, proceed to exact alarm
        if (Build.VERSION.SDK_INT >= 31) {
            val am = context.getSystemService(AlarmManager::class.java)
            if (am != null && !am.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
    }

    fun requestAllPermissions() {
        if (Build.VERSION.SDK_INT >= 33) {
            val hasNotifications = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasNotifications) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Request exact alarm if needed
                if (Build.VERSION.SDK_INT >= 31) {
                    val am = context.getSystemService(AlarmManager::class.java)
                    if (am != null && !am.canScheduleExactAlarms()) {
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                            data = Uri.parse("package:${context.packageName}")
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    }
                }
            }
        } else if (Build.VERSION.SDK_INT >= 31) {
            val am = context.getSystemService(AlarmManager::class.java)
            if (am != null && !am.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
    }

    LaunchedEffect(onboardingState.hasCompletedOnboarding) {
        if (onboardingState.hasCompletedOnboarding) {
            onOnboardingComplete()
        }
    }

    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .navigationBarsPadding()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Header varies by step
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rackedup),
                    contentDescription = "RackedUp Logo",
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = if (step == 0) "Welcome to RackedUp" else "Customize your app",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (step == 0) "Let's set up your profile to get started" else "Choose your preferred units and appearance",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            when (step) {
                0 -> OnboardingForm(
                    isLoading = onboardingState.isLoading,
                    error = onboardingState.error,
                    onComplete = { name, birthday, sex ->
                        viewModel.createProfile(name, birthday, sex)
                        step = 1
                    },
                    onErrorClear = { viewModel.clearError() }
                )

                1 -> OnboardingPreferences(
                    ui = settingsUi,
                    onUpdateWeightUnit = settingsViewModel::updateWeightUnit,
                    onUpdateDistanceUnit = settingsViewModel::updateDistanceUnit,
                    onUpdateThemeMode = settingsViewModel::updateThemeMode,
                    onUpdateDynamicColor = settingsViewModel::updateDynamicColor,
                    onUpdateColorTheme = settingsViewModel::updateColorTheme,
                    onUpdateDefaultRest = settingsViewModel::updateDefaultRestSeconds,
                    onRequestAllPermissions = { requestAllPermissions() },
                    onFinish = {
                        viewModel.completeOnboarding()
                    }
                )
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class
)
@Composable
private fun OnboardingForm(
    isLoading: Boolean,
    error: String?,
    onComplete: (String, Date?, String?) -> Unit,
    onErrorClear: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female", "Unspecified")
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    LaunchedEffect(error) {
        if (error != null) {
            onErrorClear()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Name Input
                    var isNameFocused by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                placeholder = { Text("Enter your name") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isNameFocused = focusState.isFocused
                    },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = com.chilluminati.rackedup.presentation.components.AppTextFieldDefaults.outlinedColors()
            )

        // Birthday Selection
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Cake,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Birthday",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedDate?.let { dateFormatter.format(it) } ?: "Select your birthday",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selectedDate != null) 
                            MaterialTheme.colorScheme.onSurface 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Gender Selection
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showGenderDialog = true },
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Wc,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Gender",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedGender ?: "Select your gender",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selectedGender != null) 
                            MaterialTheme.colorScheme.onSurface 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Error Display
        error?.let { errorMessage ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Complete Button
        Button(
            onClick = {
                if (name.isNotBlank()) {
                    onComplete(name, selectedDate, selectedGender)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Complete Setup")
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val defaultCalendar = Calendar.getInstance()
        defaultCalendar.add(Calendar.YEAR, -18) // Default to 18 years ago
        
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.time ?: defaultCalendar.timeInMillis
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Convert the millis to a date using UTC to avoid timezone issues
                        val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        utcCalendar.timeInMillis = millis
                        
                        // Create a new calendar in local timezone and set the date components
                        val localCalendar = Calendar.getInstance()
                        localCalendar.set(Calendar.YEAR, utcCalendar.get(Calendar.YEAR))
                        localCalendar.set(Calendar.MONTH, utcCalendar.get(Calendar.MONTH))
                        localCalendar.set(Calendar.DAY_OF_MONTH, utcCalendar.get(Calendar.DAY_OF_MONTH))
                        localCalendar.set(Calendar.HOUR_OF_DAY, 0)
                        localCalendar.set(Calendar.MINUTE, 0)
                        localCalendar.set(Calendar.SECOND, 0)
                        localCalendar.set(Calendar.MILLISECOND, 0)
                        
                        selectedDate = localCalendar.time
                    }
                    showDatePicker = false
                }) {
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

    // Gender Selection Dialog
    if (showGenderDialog) {
        AlertDialog(
            onDismissRequest = { showGenderDialog = false },
            title = { Text("Select Gender") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    genderOptions.forEach { option ->
                        OutlinedButton(
                            onClick = {
                                selectedGender = option
                                showGenderDialog = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedGender == option) 
                                    MaterialTheme.colorScheme.primaryContainer 
                                else 
                                    MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(
                                text = option,
                                color = if (selectedGender == option) 
                                    MaterialTheme.colorScheme.onPrimaryContainer 
                                else 
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            confirmButton = { },
            dismissButton = {
                TextButton(onClick = { showGenderDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class
)
@Composable
private fun OnboardingPreferences(
    ui: SettingsViewModel.SettingsUiState,
    onUpdateWeightUnit: (String) -> Unit,
    onUpdateDistanceUnit: (String) -> Unit,
    onUpdateThemeMode: (com.chilluminati.rackedup.data.repository.SettingsRepository.ThemeMode) -> Unit,
    onUpdateDynamicColor: (Boolean) -> Unit,
    onUpdateColorTheme: (com.chilluminati.rackedup.data.repository.SettingsRepository.ColorTheme) -> Unit,
    onUpdateDefaultRest: (Int) -> Unit,
    onRequestAllPermissions: () -> Unit,
    onFinish: () -> Unit
) {
    var showWeightUnitDialog by remember { mutableStateOf(false) }
    var showDistanceUnitDialog by remember { mutableStateOf(false) }
    var showColorThemeDialog by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Units section
        Text(
            text = "Units & Measurements",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(vertical = 8.dp)) {
                SettingsClickableItem(
                    title = "Weight Unit",
                    description = ui.weightUnit ?: "lbs",
                    icon = Icons.Default.FitnessCenter,
                    onClick = { showWeightUnitDialog = true }
                )
                SettingsClickableItem(
                    title = "Distance Unit",
                    description = ui.distanceUnit ?: "miles",
                    icon = Icons.Default.Timeline,
                    onClick = { showDistanceUnitDialog = true }
                )
            }
        }

        // Rest timer default
        Text(
            text = "Workout",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(vertical = 8.dp)) {
                var showRestDialog by remember { mutableStateOf(false) }
                SettingsClickableItem(
                    title = "Default Rest Timer",
                    description = formatDuration(ui.defaultRestSeconds),
                    icon = Icons.Default.Timer,
                    onClick = { showRestDialog = true }
                )
                if (showRestDialog) {
                    RestTimeWheelDialog(
                        currentSeconds = ui.defaultRestSeconds,
						onConfirm = { onUpdateDefaultRest(it); showRestDialog = false },
                        onDismiss = { showRestDialog = false }
                    )
                }
            }
        }


        if (showWeightUnitDialog) {
            UnitSelectionDialog(
                title = "Weight Unit",
                options = listOf("kg", "lbs"),
                currentSelection = ui.weightUnit ?: "kg",
                onSelectionChange = { unit ->
                    onUpdateWeightUnit(unit)
                    showWeightUnitDialog = false
                },
                onDismiss = { showWeightUnitDialog = false }
            )
        }

        if (showDistanceUnitDialog) {
            UnitSelectionDialog(
                title = "Distance Unit",
                options = listOf("km", "miles"),
                currentSelection = ui.distanceUnit ?: "km",
                onSelectionChange = { unit ->
                    onUpdateDistanceUnit(unit)
                    showDistanceUnitDialog = false
                },
                onDismiss = { showDistanceUnitDialog = false }
            )
        }

        // Appearance section
        Text(
            text = "Appearance",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(vertical = 8.dp)) {
                SettingsSwitchItem(
                    title = "Dark Mode",
                    description = "Use dark theme for the app",
                    icon = Icons.Default.NightlightRound,
                    checked = ui.themeMode == com.chilluminati.rackedup.data.repository.SettingsRepository.ThemeMode.DARK,
                    onCheckedChange = { isDark ->
                        onUpdateThemeMode(
                            if (isDark) com.chilluminati.rackedup.data.repository.SettingsRepository.ThemeMode.DARK
                            else com.chilluminati.rackedup.data.repository.SettingsRepository.ThemeMode.LIGHT
                        )
                    }
                )
                SettingsSwitchItem(
                    title = "Dynamic Color",
                    description = "Use colors from your wallpaper (Android 12+)",
                    icon = Icons.Default.Brush,
                    checked = ui.dynamicColor,
                    onCheckedChange = onUpdateDynamicColor
                )
                SettingsClickableItem(
                    title = "Color Theme",
                    description = ui.colorTheme.displayName,
                    icon = Icons.Default.Palette,
                    onClick = { showColorThemeDialog = true },
                    enabled = !ui.dynamicColor
                )
            }
        }

        if (showColorThemeDialog) {
            ColorThemeDialog(
                currentTheme = ui.colorTheme,
                onThemeSelected = { theme ->
                    onUpdateColorTheme(theme)
                    showColorThemeDialog = false
                },
                onDismiss = { showColorThemeDialog = false }
            )
        }

        // Footer note and finish
        Text(
            text = "You can change any of these later in App Settings.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Finish")
        }
    }
}
