package com.chilluminati.rackedup.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chilluminati.rackedup.data.repository.SettingsRepository

@Composable
fun SettingsSwitchItem(
    title: String,
    description: String,
    icon: ImageVector,
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
    icon: ImageVector,
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
fun UnitSelectionDialog(
    title: String,
    options: List<String>,
    currentSelection: String,
    onSelectionChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { option ->
                    OutlinedButton(
                        onClick = { onSelectionChange(option) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (currentSelection == option) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = option,
                            color = if (currentSelection == option) 
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
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ColorThemeDialog(
    currentTheme: SettingsRepository.ColorTheme,
    onThemeSelected: (SettingsRepository.ColorTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Color Theme") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingsRepository.ColorTheme.values().forEach { theme ->
                    ColorThemeOption(
                        theme = theme,
                        isSelected = theme == currentTheme,
                        onClick = { onThemeSelected(theme) }
                    )
                }
            }
        },
        confirmButton = { },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ColorThemeOption(
    theme: SettingsRepository.ColorTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(3) { index ->
                val color = themeColor(theme, index)

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

private fun themeColor(
    theme: SettingsRepository.ColorTheme,
    index: Int
): androidx.compose.ui.graphics.Color {
    val i = when {
        index <= 0 -> 0
        index == 1 -> 1
        else -> 2
    }
    return when (theme) {
        SettingsRepository.ColorTheme.FOREST -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF2E7D32)
            1 -> androidx.compose.ui.graphics.Color(0xFF388E3C)
            else -> androidx.compose.ui.graphics.Color(0xFF81C784)
        }
        SettingsRepository.ColorTheme.OCEAN -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF0277BD)
            1 -> androidx.compose.ui.graphics.Color(0xFF0288D1)
            else -> androidx.compose.ui.graphics.Color(0xFF4FC3F7)
        }
        SettingsRepository.ColorTheme.SUNSET -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFFE64A19)
            1 -> androidx.compose.ui.graphics.Color(0xFFF4511E)
            else -> androidx.compose.ui.graphics.Color(0xFFFF8A65)
        }
        SettingsRepository.ColorTheme.DESERT -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFFD84315)
            1 -> androidx.compose.ui.graphics.Color(0xFFE64A19)
            else -> androidx.compose.ui.graphics.Color(0xFFFFAB91)
        }
        SettingsRepository.ColorTheme.GLACIER -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF00ACC1)
            1 -> androidx.compose.ui.graphics.Color(0xFF00BCD4)
            else -> androidx.compose.ui.graphics.Color(0xFF80DEEA)
        }
        SettingsRepository.ColorTheme.VOLCANO -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFFD72638) // red
            1 -> androidx.compose.ui.graphics.Color(0xFFF57C00) // orange
            else -> androidx.compose.ui.graphics.Color(0xFFFFC107) // yellow
        }
        SettingsRepository.ColorTheme.MEADOW -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF558B2F)
            1 -> androidx.compose.ui.graphics.Color(0xFF689F38)
            else -> androidx.compose.ui.graphics.Color(0xFFAED581)
        }
        SettingsRepository.ColorTheme.TWILIGHT -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF4527A0)
            1 -> androidx.compose.ui.graphics.Color(0xFF512DA8)
            else -> androidx.compose.ui.graphics.Color(0xFF9575CD)
        }
        SettingsRepository.ColorTheme.AUTUMN -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFFEF6C00)
            1 -> androidx.compose.ui.graphics.Color(0xFFF57C00)
            else -> androidx.compose.ui.graphics.Color(0xFFFFB74D)
        }
        SettingsRepository.ColorTheme.CORAL_REEF -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF00897B)
            1 -> androidx.compose.ui.graphics.Color(0xFF009688)
            else -> androidx.compose.ui.graphics.Color(0xFF80CBC4)
        }
        SettingsRepository.ColorTheme.STORM -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF455A64)
            1 -> androidx.compose.ui.graphics.Color(0xFF546E7A)
            else -> androidx.compose.ui.graphics.Color(0xFF90A4AE)
        }
        SettingsRepository.ColorTheme.AURORA -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF00695C)
            1 -> androidx.compose.ui.graphics.Color(0xFF00796B)
            else -> androidx.compose.ui.graphics.Color(0xFF26A69A)
        }
        SettingsRepository.ColorTheme.CHERRY_BLOSSOM -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFFC2185B)
            1 -> androidx.compose.ui.graphics.Color(0xFFD81B60)
            else -> androidx.compose.ui.graphics.Color(0xFFF06292)
        }
        SettingsRepository.ColorTheme.MIDNIGHT_GOLD -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF1A237E)
            1 -> androidx.compose.ui.graphics.Color(0xFF283593)
            else -> androidx.compose.ui.graphics.Color(0xFFFFD700)
        }
        SettingsRepository.ColorTheme.ARCTIC_NIGHT -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF001F3F)
            1 -> androidx.compose.ui.graphics.Color(0xFF7FDBFF)
            else -> androidx.compose.ui.graphics.Color(0xFFF0FFFF)
        }
        SettingsRepository.ColorTheme.MONOCHROME -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFF000000)
            1 -> androidx.compose.ui.graphics.Color(0xFF708090)
            else -> androidx.compose.ui.graphics.Color(0xFFFFFFFF)
        }
        SettingsRepository.ColorTheme.PINK_PARADISE -> when (i) {
            0 -> androidx.compose.ui.graphics.Color(0xFFFF69B4)
            1 -> androidx.compose.ui.graphics.Color(0xFFFF8EC3)
            else -> androidx.compose.ui.graphics.Color(0xFFFFD1E8)
        }
    }
}
