package com.chilluminati.rackedup.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.chilluminati.rackedup.data.repository.SettingsRepository

/**
 * RackedUp Material 3 theme with dynamic color support
 */
@Composable
fun RackedUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    colorTheme: SettingsRepository.ColorTheme = SettingsRepository.ColorTheme.MONOCHROME,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> {
            if (darkTheme) {
                when (colorTheme) {
                    SettingsRepository.ColorTheme.FOREST -> ForestDarkColors
                    SettingsRepository.ColorTheme.OCEAN -> OceanDarkColors
                    SettingsRepository.ColorTheme.SUNSET -> SunsetDarkColors
                    SettingsRepository.ColorTheme.DESERT -> DesertDarkColors
                    SettingsRepository.ColorTheme.GLACIER -> GlacierDarkColors
                    SettingsRepository.ColorTheme.VOLCANO -> VolcanoDarkColors
                    SettingsRepository.ColorTheme.MEADOW -> MeadowDarkColors
                    SettingsRepository.ColorTheme.TWILIGHT -> TwilightDarkColors
                    SettingsRepository.ColorTheme.AUTUMN -> AutumnDarkColors
                    SettingsRepository.ColorTheme.CORAL_REEF -> CoralReefDarkColors
                    SettingsRepository.ColorTheme.STORM -> StormDarkColors
                    SettingsRepository.ColorTheme.AURORA -> AuroraDarkColors
                    SettingsRepository.ColorTheme.CHERRY_BLOSSOM -> CherryBlossomDarkColors
                    SettingsRepository.ColorTheme.MIDNIGHT_GOLD -> MidnightGoldDarkColors
                    SettingsRepository.ColorTheme.ARCTIC_NIGHT -> ArcticNightDarkColors
                    SettingsRepository.ColorTheme.MONOCHROME -> MonochromeDarkColors
                    SettingsRepository.ColorTheme.PINK_PARADISE -> PinkParadiseDarkColors
                }
            } else {
                when (colorTheme) {
                    SettingsRepository.ColorTheme.FOREST -> ForestLightColors
                    SettingsRepository.ColorTheme.OCEAN -> OceanLightColors
                    SettingsRepository.ColorTheme.SUNSET -> SunsetLightColors
                    SettingsRepository.ColorTheme.DESERT -> DesertLightColors
                    SettingsRepository.ColorTheme.GLACIER -> GlacierLightColors
                    SettingsRepository.ColorTheme.VOLCANO -> VolcanoLightColors
                    SettingsRepository.ColorTheme.MEADOW -> MeadowLightColors
                    SettingsRepository.ColorTheme.TWILIGHT -> TwilightLightColors
                    SettingsRepository.ColorTheme.AUTUMN -> AutumnLightColors
                    SettingsRepository.ColorTheme.CORAL_REEF -> CoralReefLightColors
                    SettingsRepository.ColorTheme.STORM -> StormLightColors
                    SettingsRepository.ColorTheme.AURORA -> AuroraLightColors
                    SettingsRepository.ColorTheme.CHERRY_BLOSSOM -> CherryBlossomLightColors
                    SettingsRepository.ColorTheme.MIDNIGHT_GOLD -> MidnightGoldLightColors
                    SettingsRepository.ColorTheme.ARCTIC_NIGHT -> ArcticNightLightColors
                    SettingsRepository.ColorTheme.MONOCHROME -> MonochromeLightColors
                    SettingsRepository.ColorTheme.PINK_PARADISE -> PinkParadiseLightColors
                }
            }
        }
    }

    // Lighten dark mode surfaces for better outdoor visibility
    val finalColorScheme = if (darkTheme) {
        colorScheme.copy(
            background = Color(0xFF1C1C1E),
            surface = Color(0xFF2C2C2E)
        )
    } else {
        colorScheme
    }

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as android.app.Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = finalColorScheme,
        typography = RackedUpTypography,
        shapes = RackedUpShapes,
        content = content
    )
}

/**
 * RackedUp Material 3 theme with settings-based theme mode and color theme
 */
@Composable
fun RackedUpTheme(
    themeMode: SettingsRepository.ThemeMode,
    dynamicColor: Boolean = true,
    colorTheme: SettingsRepository.ColorTheme = SettingsRepository.ColorTheme.MONOCHROME,
    content: @Composable () -> Unit
) {
    val isDarkTheme = when (themeMode) {
        SettingsRepository.ThemeMode.LIGHT -> false
        SettingsRepository.ThemeMode.DARK -> true
    }
    
    RackedUpTheme(
        darkTheme = isDarkTheme,
        dynamicColor = dynamicColor,
        colorTheme = colorTheme,
        content = content
    )
}

/**
 * Preview theme for Compose previews
 */
@Composable
fun RackedUpPreviewTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = RackedUpTypography,
        shapes = RackedUpShapes,
        content = content
    )
}
