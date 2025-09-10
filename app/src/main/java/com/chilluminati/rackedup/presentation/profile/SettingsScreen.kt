package com.chilluminati.rackedup.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
 
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chilluminati.rackedup.R
import com.chilluminati.rackedup.data.repository.SettingsRepository
 

/**
 * Settings screen for app configuration and preferences
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { Text(stringResource(R.string.settings)) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Unit Preferences
            item {
                SettingsSection(title = "Units & Measurements") {
                    var showWeightUnitDialog by remember { mutableStateOf(false) }
                    var showDistanceUnitDialog by remember { mutableStateOf(false) }
                    var showMeasurementsUnitDialog by remember { mutableStateOf(false) }
                    
                    SettingsClickableItem(
                        title = "Weight Unit",
                        description = uiState.weightUnit ?: "kg",
                        icon = Icons.Default.Scale,
                        onClick = { showWeightUnitDialog = true }
                    )
                    
                    SettingsClickableItem(
                        title = "Distance Unit",
                        description = uiState.distanceUnit ?: "km",
                        icon = Icons.Default.Straighten,
                        onClick = { showDistanceUnitDialog = true }
                    )
                    
                    SettingsClickableItem(
                        title = "Measurements Unit",
                        description = uiState.measurementsUnit ?: "in",
                        icon = Icons.Default.Straighten,
                        onClick = { showMeasurementsUnitDialog = true }
                    )
                    
                    if (showWeightUnitDialog) {
                        UnitSelectionDialog(
                            title = "Weight Unit",
                            options = listOf("kg", "lbs"),
                            currentSelection = uiState.weightUnit ?: "kg",
                             leadingIcon = Icons.Default.Scale,
                             subtitle = "Choose how weights are displayed",
                            onSelectionChange = { unit ->
                                viewModel.updateWeightUnit(unit)
                                showWeightUnitDialog = false
                            },
                            onDismiss = { showWeightUnitDialog = false }
                        )
                    }
                    
                    if (showDistanceUnitDialog) {
                        UnitSelectionDialog(
                            title = "Distance Unit", 
                            options = listOf("km", "miles"),
                            currentSelection = uiState.distanceUnit ?: "km",
                             leadingIcon = Icons.Default.Straighten,
                             subtitle = "Choose how distances are displayed",
                            onSelectionChange = { unit ->
                                viewModel.updateDistanceUnit(unit)
                                showDistanceUnitDialog = false
                            },
                            onDismiss = { showDistanceUnitDialog = false }
                        )
                    }
                    
                    if (showMeasurementsUnitDialog) {
                        UnitSelectionDialog(
                            title = "Measurements Unit",
                            options = listOf("in", "cm"),
                            currentSelection = uiState.measurementsUnit ?: "in",
                            leadingIcon = Icons.Default.Straighten,
                            subtitle = "Choose how body measurements are displayed",
                            onSelectionChange = { unit ->
                                viewModel.updateMeasurementsUnit(unit)
                                showMeasurementsUnitDialog = false
                            },
                            onDismiss = { showMeasurementsUnitDialog = false }
                        )
                    }
                }
            }
            
            // Theme Settings
            item {
                SettingsSection(title = "Appearance") {
                    SettingsSwitchItem(
                        title = "Dark Mode",
                        description = "Use dark theme for the app",
                        icon = Icons.Default.DarkMode,
                        checked = uiState.themeMode == SettingsRepository.ThemeMode.DARK,
                        onCheckedChange = { isDarkMode ->
                            val newThemeMode = if (isDarkMode) {
                                SettingsRepository.ThemeMode.DARK
                            } else {
                                SettingsRepository.ThemeMode.LIGHT
                            }
                            viewModel.updateThemeMode(newThemeMode)
                        }
                    )
                    
                    SettingsSwitchItem(
                        title = "Dynamic Color",
                        description = "Use colors from your wallpaper (Android 12+)",
                        icon = Icons.Default.Palette,
                        checked = uiState.dynamicColor,
                        onCheckedChange = { viewModel.updateDynamicColor(it) }
                    )
                    
                    var showColorThemeDialog by remember { mutableStateOf(false) }
                    
                    SettingsClickableItem(
                        title = "Color Theme",
                        description = uiState.colorTheme.displayName,
                        icon = Icons.Default.ColorLens,
                        onClick = { showColorThemeDialog = true },
                        enabled = !uiState.dynamicColor
                    )
                    
                    if (showColorThemeDialog) {
                        ColorThemeDialog(
                            currentTheme = uiState.colorTheme,
                            onThemeSelected = { theme ->
                                viewModel.updateColorTheme(theme)
                                showColorThemeDialog = false
                            },
                            onDismiss = { showColorThemeDialog = false }
                        )
                    }
                }
            }

            // Workout Settings (includes Notifications)
            item {
                SettingsSection(title = "Workout") {
            // Permissions and workout reminder removed
                    var showRestTimeDialog by remember { mutableStateOf(false) }

                    SettingsClickableItem(
                        title = "Default Rest Timer",
                        description = formatDuration(uiState.defaultRestSeconds),
                        icon = Icons.Default.Timer,
                        onClick = { showRestTimeDialog = true }
                    )

                    // Provide quick access to the same weight unit dialog here
                    SettingsSwitchItem(
                        title = "Timer Sound",
                        description = "Play sound when rest timer ends",
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        checked = uiState.restTimerSound,
                        onCheckedChange = { viewModel.updateRestTimerSound(it) }
                    )
                    
                    SettingsSwitchItem(
                        title = "Vibration",
                        description = "Vibrate for timer alerts",
                        icon = Icons.Default.Vibration,
                        checked = uiState.vibration,
                        onCheckedChange = { viewModel.updateVibration(it) }
                    )

                    if (showRestTimeDialog) {
                        RestTimeWheelDialog(
                            currentSeconds = uiState.defaultRestSeconds,
                            onConfirm = { seconds ->
                                viewModel.updateDefaultRestSeconds(seconds)
                                showRestTimeDialog = false
                            },
                            onDismiss = { showRestTimeDialog = false }
                        )
                    }

            // Workout reminder UI removed

                    // Debug buttons removed
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingsSwitchItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Composable
fun SettingsClickableItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun ColorThemeDialog(
    currentTheme: SettingsRepository.ColorTheme,
    onThemeSelected: (SettingsRepository.ColorTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Color Theme",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            val listState = androidx.compose.foundation.lazy.rememberLazyListState()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(end = 16.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = "Choose a color theme for your app",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(SettingsRepository.ColorTheme.values()) { theme ->
                        ColorThemeOption(
                            theme = theme,
                            isSelected = theme == currentTheme,
                            onSelected = { onThemeSelected(theme) }
                        )
                    }
                }

                // Themed scrollbar hint drawn in the padding gutter so it doesn't overlap content
                val layoutInfo = listState.layoutInfo
                val totalItems = layoutInfo.totalItemsCount
                val visibleItems = layoutInfo.visibleItemsInfo.size
                if (totalItems > 0 && visibleItems in 1 until totalItems) {
                    val firstIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0
                    val visibleFraction = (visibleItems.toFloat() / totalItems.toFloat()).coerceIn(0.15f, 1f)
                    val travelSpace = 1f - visibleFraction
                    val scrollFraction = if (totalItems - visibleItems <= 0) 0f else firstIndex.toFloat() / (totalItems - visibleItems).toFloat()
                    val topWeight = (scrollFraction * travelSpace).coerceIn(0f, 1f)
                    val bottomWeight = (1f - visibleFraction - topWeight).coerceIn(0f, 1f)

                    // Ensure weights are strictly greater than zero to avoid crashes
                    val epsilon = 0.0001f
                    val topW = topWeight.coerceAtLeast(epsilon)
                    val thumbW = visibleFraction.coerceAtLeast(epsilon)
                    val bottomW = bottomWeight.coerceAtLeast(epsilon)

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .width(6.dp)
                            .padding(vertical = 8.dp)
                            .offset(x = 8.dp) // push into the end padding gutter
                    ) {
                        Spacer(modifier = Modifier.weight(topW))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(thumbW)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(50)
                                )
                        )
                        Spacer(modifier = Modifier.weight(bottomW))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}

@Composable
private fun ColorThemeOption(
    theme: SettingsRepository.ColorTheme,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color preview circles
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(3) { index ->
                val color = when (theme) {
                    SettingsRepository.ColorTheme.FOREST -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFF014421)
                        1 -> androidx.compose.ui.graphics.Color(0xFF6B8E23)
                        else -> androidx.compose.ui.graphics.Color(0xFF8B5A2B)
                    }
                    SettingsRepository.ColorTheme.OCEAN -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFF004F6E)
                        1 -> androidx.compose.ui.graphics.Color(0xFF00CED1)
                        else -> androidx.compose.ui.graphics.Color(0xFFF5DEB3)
                    }
                    SettingsRepository.ColorTheme.SUNSET -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFFFF6F3C)
                        1 -> androidx.compose.ui.graphics.Color(0xFFFF3366)
                        else -> androidx.compose.ui.graphics.Color(0xFFFFD93D)
                    }
                    SettingsRepository.ColorTheme.DESERT -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFFD2B48C)
                        1 -> androidx.compose.ui.graphics.Color(0xFFC1440E)
                        else -> androidx.compose.ui.graphics.Color(0xFFC9A66B)
                    }
                    SettingsRepository.ColorTheme.GLACIER -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFFA7D8F0)
                        1 -> androidx.compose.ui.graphics.Color(0xFF0A2342)
                        else -> androidx.compose.ui.graphics.Color(0xFFF0F8FF)
                    }
                    SettingsRepository.ColorTheme.VOLCANO -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFFD72638)
                        1 -> androidx.compose.ui.graphics.Color(0xFFF57C00)
                        else -> androidx.compose.ui.graphics.Color(0xFFFFC107)
                    }
                    SettingsRepository.ColorTheme.MEADOW -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
                        1 -> androidx.compose.ui.graphics.Color(0xFFFFD700)
                        else -> androidx.compose.ui.graphics.Color(0xFF87CEEB)
                    }
                    SettingsRepository.ColorTheme.TWILIGHT -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFF4B0082)
                        1 -> androidx.compose.ui.graphics.Color(0xFF663399)
                        else -> androidx.compose.ui.graphics.Color(0xFF191970)
                    }
                    SettingsRepository.ColorTheme.AUTUMN -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFFFF7518)
                        1 -> androidx.compose.ui.graphics.Color(0xFF800020)
                        else -> androidx.compose.ui.graphics.Color(0xFFD4A017)
                    }
                    SettingsRepository.ColorTheme.CORAL_REEF -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFFFF7F50)
                        1 -> androidx.compose.ui.graphics.Color(0xFF40E0D0)
                        else -> androidx.compose.ui.graphics.Color(0xFFF4A460)
                    }
                    SettingsRepository.ColorTheme.STORM -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFF4682B4)
                        1 -> androidx.compose.ui.graphics.Color(0xFF555555)
                        else -> androidx.compose.ui.graphics.Color(0xFFC0C0C0)
                    }
                    SettingsRepository.ColorTheme.AURORA -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFF39FF14)
                        1 -> androidx.compose.ui.graphics.Color(0xFFBF00FF)
                        else -> androidx.compose.ui.graphics.Color(0xFF00FFFF)
                    }
                    SettingsRepository.ColorTheme.CHERRY_BLOSSOM -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFFFFC0CB)
                        1 -> androidx.compose.ui.graphics.Color(0xFFFFF0F5)
                        else -> androidx.compose.ui.graphics.Color(0xFF8B4513)
                    }
                    SettingsRepository.ColorTheme.MIDNIGHT_GOLD -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFF000000)
                        1 -> androidx.compose.ui.graphics.Color(0xFFFFD700)
                        else -> androidx.compose.ui.graphics.Color(0xFFCD7F32)
                    }
                    SettingsRepository.ColorTheme.ARCTIC_NIGHT -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFF001F3F)
                        1 -> androidx.compose.ui.graphics.Color(0xFF7FDBFF)
                        else -> androidx.compose.ui.graphics.Color(0xFFF0FFFF)
                    }
                    SettingsRepository.ColorTheme.MONOCHROME -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFF000000)
                        1 -> androidx.compose.ui.graphics.Color(0xFF708090)
                        else -> androidx.compose.ui.graphics.Color(0xFFFFFFFF)
                    }
                    SettingsRepository.ColorTheme.PINK_PARADISE -> when (index) {
                        0 -> androidx.compose.ui.graphics.Color(0xFFFF69B4)
                        1 -> androidx.compose.ui.graphics.Color(0xFFFF8EC3)
                        else -> androidx.compose.ui.graphics.Color(0xFFFFD1E8)
                    }
                }
                
                Card(
                    modifier = Modifier.size(16.dp),
                    colors = CardDefaults.cardColors(containerColor = color),
                    shape = androidx.compose.foundation.shape.CircleShape
                ) {}
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = theme.displayName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun UnitSelectionDialog(
    title: String,
    options: List<String>,
    currentSelection: String,
    onSelectionChange: (String) -> Unit,
    onDismiss: () -> Unit,
    leadingIcon: ImageVector? = null,
    subtitle: String? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (!subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        shape = RoundedCornerShape(28.dp),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                options.forEach { option ->
                    UnitSelectionRow(
                        label = option,
                        selected = currentSelection == option,
                        onClick = { onSelectionChange(option) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun UnitSelectionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val border = if (selected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else BorderStroke(
        1.dp,
        MaterialTheme.colorScheme.outline
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        onClick = onClick,
        tonalElevation = if (selected) 2.dp else 0.dp,
        shadowElevation = 0.dp,
        border = border
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = selected, onClick = onClick)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun RestTimeWheelDialog(
    currentSeconds: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var minutes by remember { mutableStateOf(currentSeconds / 60) }
    var seconds by remember { mutableStateOf(currentSeconds % 60) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Default Rest Time", style = MaterialTheme.typography.titleLarge) },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    WheelIntPicker(value = minutes, range = 0..30, onSelected = { minutes = it })
                    Text("min")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    WheelIntPicker(value = seconds, range = 0..59, onSelected = { seconds = it })
                    Text("sec")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(minutes * 60 + seconds) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// Android NumberPicker removed in favor of compose WheelIntPicker for consistent theming

@Composable
private fun ReminderTimePickerDialog(
    initialMinutes: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val initHour24 = (initialMinutes / 60) % 24
    val initMinute = initialMinutes % 60
    var hour12 by remember { mutableStateOf(((initHour24 % 12).let { if (it == 0) 12 else it })) }
    var minute by remember { mutableStateOf(initMinute) }
    var isAm by remember { mutableStateOf(initHour24 < 12) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Reminder Time", style = MaterialTheme.typography.titleLarge) },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    WheelIntPicker(value = hour12, range = 1..12, onSelected = { hour12 = it })
                    Text("hour")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    WheelIntPicker(value = minute, range = 0..59, onSelected = { minute = it })
                    Text("min")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    WheelStringPicker(
                        value = if (isAm) "AM" else "PM",
                        options = listOf("AM", "PM"),
                        onSelected = { isAm = it == "AM" }
                    )
                    Text("AM/PM")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val hour24 = ((hour12 % 12) + if (isAm) 0 else 12) % 24
                onConfirm(hour24 * 60 + minute)
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun WheelIntPicker(value: Int, range: IntRange, onSelected: (Int) -> Unit) {
    val itemHeight = 38.dp
    val visibleCount = 5
    val listHeight = itemHeight * visibleCount
    val state = rememberLazyListState(initialFirstVisibleItemIndex = (value - range.first).coerceAtLeast(0))
    val haptics = LocalHapticFeedback.current
    val itemHeightPx: Float = with(LocalDensity.current) { itemHeight.toPx() }

    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress) {
            val offsetPx = state.firstVisibleItemScrollOffset
            val additional = kotlin.math.round(offsetPx.toFloat() / itemHeightPx).toInt()
            val idx = (state.firstVisibleItemIndex + additional).coerceIn(0, range.last - range.first)
            state.animateScrollToItem(idx)
            val selected = range.first + idx
            if (selected != value) {
                onSelected(selected)
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
        }
    }

    Box(modifier = Modifier.height(listHeight).width(80.dp)) {
        val snapFling = rememberSnapFlingBehavior(lazyListState = state)
        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = (listHeight - itemHeight) / 2),
            flingBehavior = snapFling
        ) {
            items(range.count()) { i ->
                val num = range.first + i
                val isSelected = num == value
                Text(
                    text = if (range.last >= 10) num.toString().padStart(2, '0') else num.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isSelected) 1f else 0.5f),
                    modifier = Modifier.height(itemHeight).wrapContentHeight()
                )
            }
        }
        // Selection indicators
        val lineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        val stroke = 1.dp
        Box(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .height(itemHeight)) {
            Box(modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().height(stroke).background(lineColor))
            Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(stroke).background(lineColor))
        }
    }
}

@Composable
private fun WheelStringPicker(value: String, options: List<String>, onSelected: (String) -> Unit) {
    val itemHeight = 38.dp
    val visibleCount = 5
    val listHeight = itemHeight * visibleCount
    val startIndex = options.indexOf(value).coerceAtLeast(0)
    val state = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)
    val haptics = LocalHapticFeedback.current
    val itemHeightPx: Float = with(LocalDensity.current) { itemHeight.toPx() }

    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress) {
            val offsetPx = state.firstVisibleItemScrollOffset
            val additional = kotlin.math.round(offsetPx.toFloat() / itemHeightPx).toInt()
            val idx = (state.firstVisibleItemIndex + additional).coerceIn(0, options.lastIndex)
            state.animateScrollToItem(idx)
            val selected = options[idx]
            if (selected != value) {
                onSelected(selected)
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
        }
    }

    Box(modifier = Modifier.height(listHeight).width(80.dp)) {
        val snapFling = rememberSnapFlingBehavior(lazyListState = state)
        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = (listHeight - itemHeight) / 2),
            flingBehavior = snapFling
        ) {
            items(options.size) { i ->
                val option = options[i]
                val isSelected = option == value
                Text(
                    text = option,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isSelected) 1f else 0.5f),
                    modifier = Modifier.height(itemHeight).wrapContentHeight()
                )
            }
        }
        val lineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        val stroke = 1.dp
        Box(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .height(itemHeight)) {
            Box(modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().height(stroke).background(lineColor))
            Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(stroke).background(lineColor))
        }
    }
}

// applyPickerTextColor no longer needed

private fun formatMinutesAsTime(minutesFromMidnight: Int): String {
    val hours = (minutesFromMidnight / 60) % 24
    val minutes = minutesFromMidnight % 60
    val am = hours < 12
    val displayHour = when (hours % 12) { 0 -> 12; else -> hours % 12 }
    val displayMin = minutes.toString().padStart(2, '0')
    val suffix = if (am) "AM" else "PM"
    return "$displayHour:$displayMin $suffix"
}

fun formatDuration(totalSeconds: Int): String {
    val min = totalSeconds / 60
    val sec = totalSeconds % 60
    return if (sec == 0) {
        "$min minute${if (min == 1) "" else "s"}"
    } else {
        val minPart = if (min > 0) "$min minute${if (min == 1) "" else "s"} " else ""
        "$minPart$sec second${if (sec == 1) "" else "s"}"
    }
}

