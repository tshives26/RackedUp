package com.chilluminati.rackedup.presentation.theme

import androidx.compose.ui.graphics.Color

// Primary brand colors (vibrant triad)
val RackedUpPrimary = Color(0xFF7C4DFF)
val RackedUpPrimaryDark = Color(0xFF5E35B1)
val RackedUpSecondary = Color(0xFF536DFE)
val RackedUpTertiary = Color(0xFFFF4081)

// Surface colors
val RackedUpSurface = Color(0xFFFFFBFE)
val RackedUpSurfaceDark = Color(0xFF121212)
val RackedUpSurfaceVariant = Color(0xFFE7E0EC)
val RackedUpSurfaceVariantDark = Color(0xFF49454F)

// Workout-specific colors
val ExerciseStrength = Color(0xFF1976D2)     // Blue for strength training
val ExerciseCardio = Color(0xFFD32F2F)       // Red for cardio
val ExerciseIsometric = Color(0xFF388E3C)    // Green for isometric
val ExerciseStretching = Color(0xFFE64A19)   // Orange for stretching

// Progress colors
val ProgressGreen = Color(0xFF4CAF50)
val ProgressYellow = Color(0xFFFF9800)
val ProgressRed = Color(0xFFF44336)
val ProgressBlue = Color(0xFF2196F3)

// Chart colors (aligned to brand triad + accents)
val ChartPrimary = RackedUpPrimary
val ChartSecondary = RackedUpSecondary
val ChartTertiary = RackedUpTertiary
val ChartQuaternary = Color(0xFF43A047)
val ChartQuinary = Color(0xFFFFC107)

// Status colors
val StatusActive = Color(0xFF4CAF50)
val StatusInactive = Color(0xFF9E9E9E)
val StatusWarning = Color(0xFFFF9800)
val StatusError = Color(0xFFF44336)

// Background gradients
val GradientStart = Color(0xFF667eea)
val GradientEnd = Color(0xFF764ba2)

// Color Theme Definitions
// Purple Theme (Default)
val PurpleLightColors = androidx.compose.material3.lightColorScheme(
    primary = RackedUpPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = RackedUpSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = RackedUpTertiary,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = RackedUpSurface,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = RackedUpSurfaceVariant,
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
)

val PurpleDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFB388FF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFF8C9EFF),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),
    tertiary = Color(0xFFFF79A7),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF10131A),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF10131A),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
)

// Ocean Theme (Deep Sea / Aqua / Sand)
val OceanLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF004F6E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB3D7E6),
    onPrimaryContainer = Color(0xFF00141D),
    secondary = Color(0xFF00CED1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB2F1F2),
    onSecondaryContainer = Color(0xFF003637),
    tertiary = Color(0xFFF5DEB3),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFEFD0),
    onTertiaryContainer = Color(0xFF2D2204),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFDFCFF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFFDFCFF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDFE2EB),
    onSurfaceVariant = Color(0xFF43474E),
    outline = Color(0xFF73777F),
    outlineVariant = Color(0xFFC3C7CF),
)

val OceanDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF69B3CF),
    onPrimary = Color(0xFF001E29),
    primaryContainer = Color(0xFF003B51),
    onPrimaryContainer = Color(0xFFB3D7E6),
    secondary = Color(0xFF4ADADB),
    onSecondary = Color(0xFF002A2B),
    secondaryContainer = Color(0xFF005C5D),
    onSecondaryContainer = Color(0xFFB2F1F2),
    tertiary = Color(0xFFE8CFA1),
    onTertiary = Color(0xFF2A1F03),
    tertiaryContainer = Color(0xFF5E4E1E),
    onTertiaryContainer = Color(0xFFFFEFD0),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF111416),
    onBackground = Color(0xFFE2E2E5),
    surface = Color(0xFF111416),
    onSurface = Color(0xFFE2E2E5),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C7CF),
    outline = Color(0xFF8D9199),
    outlineVariant = Color(0xFF43474E),
)

// Forest Theme (Deep Evergreen / Moss / Earth)
val ForestLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF014421),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF9DD9B8),
    onPrimaryContainer = Color(0xFF00150A),
    secondary = Color(0xFF6B8E23),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD8E7A7),
    onSecondaryContainer = Color(0xFF1B2400),
    tertiary = Color(0xFF8B5A2B),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE0C2A0),
    onTertiaryContainer = Color(0xFF2F1904),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFCFDF7),
    onBackground = Color(0xFF1A1C19),
    surface = Color(0xFFFCFDF7),
    onSurface = Color(0xFF1A1C19),
    surfaceVariant = Color(0xFFDFE4D7),
    onSurfaceVariant = Color(0xFF43483E),
    outline = Color(0xFF73796D),
    outlineVariant = Color(0xFFC3C8BB),
)

val ForestDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF57B27A),
    onPrimary = Color(0xFF001E0E),
    primaryContainer = Color(0xFF0B3A23),
    onPrimaryContainer = Color(0xFF9DD9B8),
    secondary = Color(0xFFAFC771),
    onSecondary = Color(0xFF222800),
    secondaryContainer = Color(0xFF39420E),
    onSecondaryContainer = Color(0xFFD8E7A7),
    tertiary = Color(0xFFCFAB7E),
    onTertiary = Color(0xFF2A1603),
    tertiaryContainer = Color(0xFF573914),
    onTertiaryContainer = Color(0xFFE0C2A0),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF111411),
    onBackground = Color(0xFFE2E3DD),
    surface = Color(0xFF111411),
    onSurface = Color(0xFFE2E3DD),
    surfaceVariant = Color(0xFF43483E),
    onSurfaceVariant = Color(0xFFC3C8BB),
    outline = Color(0xFF8D9286),
    outlineVariant = Color(0xFF43483E),
)

// Sunset Theme (Warm Orange / Magenta / Golden)
val SunsetLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFFF6F3C),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD1BF),
    onPrimaryContainer = Color(0xFF2F0A00),
    secondary = Color(0xFFFF3366),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFB0C2),
    onSecondaryContainer = Color(0xFF330011),
    tertiary = Color(0xFFFFD93D),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFEE9B),
    onTertiaryContainer = Color(0xFF271E00),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF201A17),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF201A17),
    surfaceVariant = Color(0xFFF5DDD6),
    onSurfaceVariant = Color(0xFF53433E),
    outline = Color(0xFF85736D),
    outlineVariant = Color(0xFFD8C2BA),
)

val SunsetDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFFFA477),
    onPrimary = Color(0xFF411400),
    primaryContainer = Color(0xFF7A2C0F),
    onPrimaryContainer = Color(0xFFFFD1BF),
    secondary = Color(0xFFFF7AA2),
    onSecondary = Color(0xFF370015),
    secondaryContainer = Color(0xFF7A1335),
    onSecondaryContainer = Color(0xFFFFB0C2),
    tertiary = Color(0xFFF8D76D),
    onTertiary = Color(0xFF2B2100),
    tertiaryContainer = Color(0xFF635400),
    onTertiaryContainer = Color(0xFFFFEE9B),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF17120F),
    onBackground = Color(0xFFEDE0DB),
    surface = Color(0xFF17120F),
    onSurface = Color(0xFFEDE0DB),
    surfaceVariant = Color(0xFF53433E),
    onSurfaceVariant = Color(0xFFD8C2BA),
    outline = Color(0xFFA08D85),
    outlineVariant = Color(0xFF53433E),
)

// Rose Theme (Pink/Magenta)
val RoseLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFE91E63),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFCDD7),
    onPrimaryContainer = Color(0xFF3A0015),
    secondary = Color(0xFF9C27B0),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFD9DD),
    onSecondaryContainer = Color(0xFF2C1516),
    tertiary = Color(0xFFFF4081),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDCBE),
    onTertiaryContainer = Color(0xFF2E1500),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1F1A1B),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1F1A1B),
    surfaceVariant = Color(0xFFF2DDE1),
    onSurfaceVariant = Color(0xFF514143),
    outline = Color(0xFF837174),
    outlineVariant = Color(0xFFD5C1C5),
)

val RoseDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFF48FB1),
    onPrimary = Color(0xFF5E0026),
    primaryContainer = Color(0xFF83003A),
    onPrimaryContainer = Color(0xFFFFCDD7),
    secondary = Color(0xFFCE93D8),
    onSecondary = Color(0xFF432A2B),
    secondaryContainer = Color(0xFF594041),
    onSecondaryContainer = Color(0xFFFFD9DD),
    tertiary = Color(0xFFFF80AB),
    onTertiary = Color(0xFF462A06),
    tertiaryContainer = Color(0xFF60401B),
    onTertiaryContainer = Color(0xFFFFDCBE),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF161012),
    onBackground = Color(0xFFEDE0E1),
    surface = Color(0xFF161012),
    onSurface = Color(0xFFEDE0E1),
    surfaceVariant = Color(0xFF514143),
    onSurfaceVariant = Color(0xFFD5C1C5),
    outline = Color(0xFF9E8B8F),
    outlineVariant = Color(0xFF514143),
)

// Lavender Theme (Light Purple/Periwinkle)
val LavenderLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF7E57C2),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF3E5F5),
    onPrimaryContainer = Color(0xFF38003C),
    secondary = Color(0xFF5C6BC0),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF0DBF6),
    onSecondaryContainer = Color(0xFF241529),
    tertiary = Color(0xFFBA68C8),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD9DF),
    onTertiaryContainer = Color(0xFF33111A),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFEFBFF),
    onBackground = Color(0xFF1D1B1E),
    surface = Color(0xFFFEFBFF),
    onSurface = Color(0xFF1D1B1E),
    surfaceVariant = Color(0xFFEADFE6),
    onSurfaceVariant = Color(0xFF4B444A),
    outline = Color(0xFF7C747B),
    outlineVariant = Color(0xFFCDC3CA),
)

val LavenderDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFB39DDB),
    onPrimary = Color(0xFF5E0962),
    primaryContainer = Color(0xFF7B1984),
    onPrimaryContainer = Color(0xFFF3E5F5),
    secondary = Color(0xFF9FA8DA),
    onSecondary = Color(0xFF3A2B3F),
    secondaryContainer = Color(0xFF524057),
    onSecondaryContainer = Color(0xFFF0DBF6),
    tertiary = Color(0xFFCE93D8),
    onTertiary = Color(0xFF4A262F),
    tertiaryContainer = Color(0xFF653C45),
    onTertiaryContainer = Color(0xFFFFD9DF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF141316),
    onBackground = Color(0xFFE7E1E5),
    surface = Color(0xFF141316),
    onSurface = Color(0xFFE7E1E5),
    surfaceVariant = Color(0xFF4B444A),
    onSurfaceVariant = Color(0xFFCDC3CA),
    outline = Color(0xFF968D94),
    outlineVariant = Color(0xFF4B444A),
)

// Mint Theme (Light Green/Teal)
val MintLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF00BCD4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB2EBF2),
    onPrimaryContainer = Color(0xFF002A2F),
    secondary = Color(0xFF26A69A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1E7EB),
    onSecondaryContainer = Color(0xFF0B1F22),
    tertiary = Color(0xFF4DD0E1),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFDAE2FF),
    onTertiaryContainer = Color(0xFF0E1B37),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFAFDFD),
    onBackground = Color(0xFF191C1D),
    surface = Color(0xFFFAFDFD),
    onSurface = Color(0xFF191C1D),
    surfaceVariant = Color(0xFFDBE4E6),
    onSurfaceVariant = Color(0xFF3F484A),
    outline = Color(0xFF70787A),
    outlineVariant = Color(0xFFBFC8CA),
)

val MintDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF80DEEA),
    onPrimary = Color(0xFF003F47),
    primaryContainer = Color(0xFF00596B),
    onPrimaryContainer = Color(0xFFB2EBF2),
    secondary = Color(0xFF80CBC4),
    onSecondary = Color(0xFF203437),
    secondaryContainer = Color(0xFF374A4D),
    onSecondaryContainer = Color(0xFFD1E7EB),
    tertiary = Color(0xFF4DD0E1),
    onTertiary = Color(0xFF24304D),
    tertiaryContainer = Color(0xFF3B4664),
    onTertiaryContainer = Color(0xFFDAE2FF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0F1415),
    onBackground = Color(0xFFE0E3E3),
    surface = Color(0xFF0F1415),
    onSurface = Color(0xFFE0E3E3),
    surfaceVariant = Color(0xFF3F484A),
    onSurfaceVariant = Color(0xFFBFC8CA),
    outline = Color(0xFF899294),
    outlineVariant = Color(0xFF3F484A),
)

// Amber Theme (Yellow/Gold)
val AmberLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFFFC107),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE082),
    onPrimaryContainer = Color(0xFF261900),
    secondary = Color(0xFFFF8F00),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF9DEBC),
    onSecondaryContainer = Color(0xFF271904),
    tertiary = Color(0xFFFF7043),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD3EABC),
    onTertiaryContainer = Color(0xFF102004),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBF7),
    onBackground = Color(0xFF1F1B16),
    surface = Color(0xFFFFFBF7),
    onSurface = Color(0xFF1F1B16),
    surfaceVariant = Color(0xFFF0E0CF),
    onSurfaceVariant = Color(0xFF4F4539),
    outline = Color(0xFF817567),
    outlineVariant = Color(0xFFD3C4B3),
)

val AmberDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFFFD54F),
    onPrimary = Color(0xFF3E2723),
    primaryContainer = Color(0xFF8D4E00),
    onPrimaryContainer = Color(0xFFFFE082),
    secondary = Color(0xFFFFB74D),
    onSecondary = Color(0xFF3E2E16),
    secondaryContainer = Color(0xFF56442A),
    onSecondaryContainer = Color(0xFFF9DEBC),
    tertiary = Color(0xFFFFAB91),
    onTertiary = Color(0xFF253516),
    tertiaryContainer = Color(0xFF3A4C2A),
    onTertiaryContainer = Color(0xFFD3EABC),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF16130E),
    onBackground = Color(0xFFEAE1D9),
    surface = Color(0xFF16130E),
    onSurface = Color(0xFFEAE1D9),
    surfaceVariant = Color(0xFF4F4539),
    onSurfaceVariant = Color(0xFFD3C4B3),
    outline = Color(0xFF9C8F7F),
    outlineVariant = Color(0xFF4F4539),
)

// Midnight Theme (Dark Blue/Navy)
val MidnightLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF283593),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDE1FF),
    onPrimaryContainer = Color(0xFF000C5C),
    secondary = Color(0xFF00897B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDFE1F9),
    onSecondaryContainer = Color(0xFF18192B),
    tertiary = Color(0xFF8E24AA),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD6F4),
    onTertiaryContainer = Color(0xFF2D1127),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFEFBFF),
    onBackground = Color(0xFF1B1B21),
    surface = Color(0xFFFEFBFF),
    onSurface = Color(0xFF1B1B21),
    surfaceVariant = Color(0xFFE3E1EC),
    onSurfaceVariant = Color(0xFF46464F),
    outline = Color(0xFF767680),
    outlineVariant = Color(0xFFC7C5D0),
)

val MidnightDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF9FA8DA),
    onPrimary = Color(0xFF001871),
    primaryContainer = Color(0xFF002C99),
    onPrimaryContainer = Color(0xFFDDE1FF),
    secondary = Color(0xFF80CBC4),
    onSecondary = Color(0xFF2D2F42),
    secondaryContainer = Color(0xFF434659),
    onSecondaryContainer = Color(0xFFDFE1F9),
    tertiary = Color(0xFFCE93D8),
    onTertiary = Color(0xFF44263D),
    tertiaryContainer = Color(0xFF5D3C54),
    onTertiaryContainer = Color(0xFFFFD6F4),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF131318),
    onBackground = Color(0xFFE5E1E9),
    surface = Color(0xFF131318),
    onSurface = Color(0xFFE5E1E9),
    surfaceVariant = Color(0xFF46464F),
    onSurfaceVariant = Color(0xFFC7C5D0),
    outline = Color(0xFF909099),
    outlineVariant = Color(0xFF46464F),
)

// Cherry Theme (Deep Red/Burgundy)
val CherryLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFC62828),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD4),
    onPrimaryContainer = Color(0xFF410000),
    secondary = Color(0xFF8E24AA),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDAD4),
    onSecondaryContainer = Color(0xFF2C1512),
    tertiary = Color(0xFFFF6F00),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFBDFA6),
    onTertiaryContainer = Color(0xFF251A00),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF201A19),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF201A19),
    surfaceVariant = Color(0xFFF5DDD9),
    onSurfaceVariant = Color(0xFF534341),
    outline = Color(0xFF857370),
    outlineVariant = Color(0xFFD8C2BD),
)

val CherryDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFEF9A9A),
    onPrimary = Color(0xFF680100),
    primaryContainer = Color(0xFF930100),
    onPrimaryContainer = Color(0xFFFFDAD4),
    secondary = Color(0xFFCE93D8),
    onSecondary = Color(0xFF442925),
    secondaryContainer = Color(0xFF5D3F3B),
    onSecondaryContainer = Color(0xFFFFDAD4),
    tertiary = Color(0xFFFFCC80),
    onTertiary = Color(0xFF3C2F05),
    tertiaryContainer = Color(0xFF554519),
    onTertiaryContainer = Color(0xFFFBDFA6),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF181210),
    onBackground = Color(0xFFF1DFD9),
    surface = Color(0xFF181210),
    onSurface = Color(0xFFF1DFD9),
    surfaceVariant = Color(0xFF534341),
    onSurfaceVariant = Color(0xFFD8C2BD),
    outline = Color(0xFFA08C89),
    outlineVariant = Color(0xFF534341),
)

// Desert Theme (Sandy / Clay / Dusty Gold)
val DesertLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFD2B48C),
    onPrimary = Color(0xFF222222),
    primaryContainer = Color(0xFFF0E0C8),
    onPrimaryContainer = Color(0xFF2A1E00),
    secondary = Color(0xFFC1440E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF0B69B),
    onSecondaryContainer = Color(0xFF3A0E00),
    tertiary = Color(0xFFC9A66B),
    onTertiary = Color(0xFF231800),
    tertiaryContainer = Color(0xFFE7D7B8),
    onTertiaryContainer = Color(0xFF2C1F03),
    background = Color(0xFFFFFBF5),
    onBackground = Color(0xFF201E19),
    surface = Color(0xFFFFFBF5),
    onSurface = Color(0xFF201E19),
    surfaceVariant = Color(0xFFEDE2D3),
    onSurfaceVariant = Color(0xFF51463A),
    outline = Color(0xFF817567),
    outlineVariant = Color(0xFFD8CCBD)
)

val DesertDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFE2C79C),
    onPrimary = Color(0xFF211600),
    primaryContainer = Color(0xFF6B5738),
    onPrimaryContainer = Color(0xFFF0E0C8),
    secondary = Color(0xFFE48C6B),
    onSecondary = Color(0xFF3A0E00),
    secondaryContainer = Color(0xFF6B2B12),
    onSecondaryContainer = Color(0xFFF0B69B),
    tertiary = Color(0xFFD8BF8C),
    onTertiary = Color(0xFF231800),
    tertiaryContainer = Color(0xFF5A4622),
    onTertiaryContainer = Color(0xFFE7D7B8),
    background = Color(0xFF15130F),
    onBackground = Color(0xFFE7E0D7),
    surface = Color(0xFF15130F),
    onSurface = Color(0xFFE7E0D7),
    surfaceVariant = Color(0xFF51463A),
    onSurfaceVariant = Color(0xFFD8CCBD),
    outline = Color(0xFF9C8F7F),
    outlineVariant = Color(0xFF51463A)
)

// Glacier Theme (Ice Blue / Deep Navy / Frost)
val GlacierLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFA7D8F0),
    onPrimary = Color(0xFF0A2342),
    primaryContainer = Color(0xFFD9F1FB),
    onPrimaryContainer = Color(0xFF09131B),
    secondary = Color(0xFF0A2342),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB7C4D9),
    onSecondaryContainer = Color(0xFF000A16),
    tertiary = Color(0xFFF0F8FF),
    onTertiary = Color(0xFF0B1A24),
    tertiaryContainer = Color(0xFFFFFFFF),
    onTertiaryContainer = Color(0xFF0B1A24),
    background = Color(0xFFFBFDFF),
    onBackground = Color(0xFF131C22),
    surface = Color(0xFFFBFDFF),
    onSurface = Color(0xFF131C22),
    surfaceVariant = Color(0xFFE0E7EF),
    onSurfaceVariant = Color(0xFF3F4A52),
    outline = Color(0xFF72808B),
    outlineVariant = Color(0xFFC8D2DB)
)

val GlacierDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFC7E8F7),
    onPrimary = Color(0xFF09131B),
    primaryContainer = Color(0xFF123249),
    onPrimaryContainer = Color(0xFFD9F1FB),
    secondary = Color(0xFF93A3BC),
    onSecondary = Color(0xFF0A1220),
    secondaryContainer = Color(0xFF0F2649),
    onSecondaryContainer = Color(0xFFB7C4D9),
    tertiary = Color(0xFFE7F4FF),
    onTertiary = Color(0xFF0A1720),
    tertiaryContainer = Color(0xFF2A4054),
    onTertiaryContainer = Color(0xFFF0F8FF),
    background = Color(0xFF0E141A),
    onBackground = Color(0xFFDDE5EB),
    surface = Color(0xFF0E141A),
    onSurface = Color(0xFFDDE5EB),
    surfaceVariant = Color(0xFF3F4A52),
    onSurfaceVariant = Color(0xFFC8D2DB),
    outline = Color(0xFF8B98A3),
    outlineVariant = Color(0xFF3F4A52)
)

// Volcano Theme (Lava / Charcoal / Ash)
val VolcanoLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFD72638),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFC0C6),
    onPrimaryContainer = Color(0xFF3F0006),
    // Fire-themed accents
    secondary = Color(0xFFF57C00),
    onSecondary = Color(0xFF2A1100),
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFF2A1100),
    tertiary = Color(0xFFFFC107),
    onTertiary = Color(0xFF231B00),
    tertiaryContainer = Color(0xFFFFECB3),
    onTertiaryContainer = Color(0xFF241A00),
    background = Color(0xFFFFFBFB),
    onBackground = Color(0xFF1D1B1B),
    surface = Color(0xFFFFFBFB),
    onSurface = Color(0xFF1D1B1B),
    surfaceVariant = Color(0xFFE7E0E0),
    onSurfaceVariant = Color(0xFF4C4646),
    outline = Color(0xFF807979),
    outlineVariant = Color(0xFFCDC5C5)
)

val VolcanoDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFFF8A94),
    onPrimary = Color(0xFF47000A),
    primaryContainer = Color(0xFF7D0E1D),
    onPrimaryContainer = Color(0xFFFFC0C6),
    // Fire-themed accents
    secondary = Color(0xFFFFB74D),
    onSecondary = Color(0xFF3E1300),
    secondaryContainer = Color(0xFF5A2A00),
    onSecondaryContainer = Color(0xFFFFE0B2),
    tertiary = Color(0xFFFFE066),
    onTertiary = Color(0xFF261E00),
    tertiaryContainer = Color(0xFF5A4A00),
    onTertiaryContainer = Color(0xFFFFECB3),
    background = Color(0xFF131111),
    onBackground = Color(0xFFE7E0E0),
    surface = Color(0xFF131111),
    onSurface = Color(0xFFE7E0E0),
    surfaceVariant = Color(0xFF4C4646),
    onSurfaceVariant = Color(0xFFCDC5C5),
    outline = Color(0xFF9A9292),
    outlineVariant = Color(0xFF4C4646)
)

// Meadow Theme (Grass / Dandelion / Sky)
val MeadowLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF08210B),
    secondary = Color(0xFFFFD700),
    onSecondary = Color(0xFF2A2200),
    secondaryContainer = Color(0xFFFFECB3),
    onSecondaryContainer = Color(0xFF241A00),
    tertiary = Color(0xFF87CEEB),
    onTertiary = Color(0xFF07171E),
    tertiaryContainer = Color(0xFFD6F0FB),
    onTertiaryContainer = Color(0xFF051219),
    background = Color(0xFFFAFFFA),
    onBackground = Color(0xFF19201A),
    surface = Color(0xFFFAFFFA),
    onSurface = Color(0xFF19201A),
    surfaceVariant = Color(0xFFE1EAD6),
    onSurfaceVariant = Color(0xFF44513B),
    outline = Color(0xFF74836D),
    outlineVariant = Color(0xFFC9D4BF)
)

val MeadowDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF93D59A),
    onPrimary = Color(0xFF06210B),
    primaryContainer = Color(0xFF1E5E24),
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFFFFE066),
    onSecondary = Color(0xFF2C2400),
    secondaryContainer = Color(0xFF594C00),
    onSecondaryContainer = Color(0xFFFFECB3),
    tertiary = Color(0xFFB6E6F7),
    onTertiary = Color(0xFF051219),
    tertiaryContainer = Color(0xFF1E4C5D),
    onTertiaryContainer = Color(0xFFD6F0FB),
    background = Color(0xFF101511),
    onBackground = Color(0xFFE2E7E3),
    surface = Color(0xFF101511),
    onSurface = Color(0xFFE2E7E3),
    surfaceVariant = Color(0xFF44513B),
    onSurfaceVariant = Color(0xFFC9D4BF),
    outline = Color(0xFF8E9B86),
    outlineVariant = Color(0xFF44513B)
)

// Twilight Theme (Indigo / Deep Purple / Midnight)
val TwilightLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF4B0082),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD3B3F2),
    onPrimaryContainer = Color(0xFF1A0034),
    secondary = Color(0xFF663399),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDEC1FF),
    onSecondaryContainer = Color(0xFF230046),
    tertiary = Color(0xFF191970),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFAEB3FF),
    onTertiaryContainer = Color(0xFF06083A)
)

val TwilightDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFB98AE9),
    onPrimary = Color(0xFF2A004F),
    primaryContainer = Color(0xFF3A006E),
    onPrimaryContainer = Color(0xFFD3B3F2),
    secondary = Color(0xFFC7A2FF),
    onSecondary = Color(0xFF300061),
    secondaryContainer = Color(0xFF47008F),
    onSecondaryContainer = Color(0xFFDEC1FF),
    tertiary = Color(0xFF9EA6FF),
    onTertiary = Color(0xFF0B0E4A),
    tertiaryContainer = Color(0xFF1D2366),
    onTertiaryContainer = Color(0xFFAEB3FF)
)

// Autumn Theme (Pumpkin / Burgundy / Harvest Gold)
val AutumnLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFFF7518),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD0B3),
    onPrimaryContainer = Color(0xFF2F0C00),
    secondary = Color(0xFF800020),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDA9AAC),
    onSecondaryContainer = Color(0xFF2C0010),
    tertiary = Color(0xFFD4A017),
    onTertiary = Color(0xFF231B00),
    tertiaryContainer = Color(0xFFF2D28A),
    onTertiaryContainer = Color(0xFF2A2100)
)

val AutumnDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFFFA66A),
    onPrimary = Color(0xFF3E1300),
    primaryContainer = Color(0xFF7A2F0F),
    onPrimaryContainer = Color(0xFFFFD0B3),
    secondary = Color(0xFFFF7697),
    onSecondary = Color(0xFF370015),
    secondaryContainer = Color(0xFF5B0F27),
    onSecondaryContainer = Color(0xFFDA9AAC),
    tertiary = Color(0xFFE6C35B),
    onTertiary = Color(0xFF261E00),
    tertiaryContainer = Color(0xFF5A4A00),
    onTertiaryContainer = Color(0xFFF2D28A)
)

// Coral Reef Theme (Coral / Turquoise / Sand)
val CoralReefLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFFF7F50),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD0C2),
    onPrimaryContainer = Color(0xFF300C03),
    secondary = Color(0xFF40E0D0),
    onSecondary = Color(0xFF00332E),
    secondaryContainer = Color(0xFFB9FFF2),
    onSecondaryContainer = Color(0xFF00201C),
    tertiary = Color(0xFFF4A460),
    onTertiary = Color(0xFF2A1700),
    tertiaryContainer = Color(0xFFFFD8AC),
    onTertiaryContainer = Color(0xFF301C00)
)

// Pink Paradise Theme (Three shades of pink; playful and girly)
val PinkParadiseLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFFF69B4), // Hot Pink
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFC1DE),
    onPrimaryContainer = Color(0xFF3A0A23),
    secondary = Color(0xFFFF8EC3), // Pink
    onSecondary = Color(0xFF330018),
    secondaryContainer = Color(0xFFFFD1E8),
    onSecondaryContainer = Color(0xFF2C0014),
    tertiary = Color(0xFFFFD1E8), // Light Pink
    onTertiary = Color(0xFF2E0A1A),
    tertiaryContainer = Color(0xFFFFE6F3),
    onTertiaryContainer = Color(0xFF290815),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

val PinkParadiseDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFFF9CCC),
    onPrimary = Color(0xFF3A0A23),
    primaryContainer = Color(0xFF65163A),
    onPrimaryContainer = Color(0xFFFFC1DE),
    secondary = Color(0xFFFFB3D7),
    onSecondary = Color(0xFF3A0A23),
    secondaryContainer = Color(0xFF5A1E3A),
    onSecondaryContainer = Color(0xFFFFD1E8),
    tertiary = Color(0xFFFFE1F1),
    onTertiary = Color(0xFF330018),
    tertiaryContainer = Color(0xFF4A1830),
    onTertiaryContainer = Color(0xFFFFE6F3)
)

val CoralReefDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFFFB199),
    onPrimary = Color(0xFF401507),
    primaryContainer = Color(0xFF7E2E17),
    onPrimaryContainer = Color(0xFFFFD0C2),
    secondary = Color(0xFF79F6E6),
    onSecondary = Color(0xFF003733),
    secondaryContainer = Color(0xFF00524B),
    onSecondaryContainer = Color(0xFFB9FFF2),
    tertiary = Color(0xFFF8C891),
    onTertiary = Color(0xFF2D1A01),
    tertiaryContainer = Color(0xFF5F3F12),
    onTertiaryContainer = Color(0xFFFFD8AC)
)

// Storm Theme (Steel / Dark Gray / Silver)
val StormLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF4682B4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBFD4E6),
    onPrimaryContainer = Color(0xFF0A1F2F),
    secondary = Color(0xFF555555),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC8C8C8),
    onSecondaryContainer = Color(0xFF151515),
    tertiary = Color(0xFFC0C0C0),
    onTertiary = Color(0xFF1A1A1A),
    tertiaryContainer = Color(0xFFE6E6E6),
    onTertiaryContainer = Color(0xFF1F1F1F)
)

val StormDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF9BB7D2),
    onPrimary = Color(0xFF0D2234),
    primaryContainer = Color(0xFF27455D),
    onPrimaryContainer = Color(0xFFBFD4E6),
    secondary = Color(0xFFB0B0B0),
    onSecondary = Color(0xFF151515),
    secondaryContainer = Color(0xFF393939),
    onSecondaryContainer = Color(0xFFC8C8C8),
    tertiary = Color(0xFFD8D8D8),
    onTertiary = Color(0xFF202020),
    tertiaryContainer = Color(0xFF3E3E3E),
    onTertiaryContainer = Color(0xFFE6E6E6)
)

// Aurora Theme (Neon Green / Electric Purple / Cyan)
val AuroraLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF39FF14),
    onPrimary = Color(0xFF002200),
    primaryContainer = Color(0xFFB9FFAD),
    onPrimaryContainer = Color(0xFF001700),
    secondary = Color(0xFFBF00FF),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE5B0FF),
    onSecondaryContainer = Color(0xFF2B0040),
    tertiary = Color(0xFF00FFFF),
    onTertiary = Color(0xFF002022),
    tertiaryContainer = Color(0xFFB3FFFF),
    onTertiaryContainer = Color(0xFF001416)
)

val AuroraDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF97FF82),
    onPrimary = Color(0xFF002200),
    primaryContainer = Color(0xFF0E3D0E),
    onPrimaryContainer = Color(0xFFB9FFAD),
    secondary = Color(0xFFD39BFF),
    onSecondary = Color(0xFF2B0040),
    secondaryContainer = Color(0xFF520082),
    onSecondaryContainer = Color(0xFFE5B0FF),
    tertiary = Color(0xFF8BFFFF),
    onTertiary = Color(0xFF001416),
    tertiaryContainer = Color(0xFF005A5F),
    onTertiaryContainer = Color(0xFFB3FFFF)
)

// Cherry Blossom Theme (Soft Pink / Blossom White / Warm Brown)
val CherryBlossomLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFFFFC0CB),
    onPrimary = Color(0xFF3B1118),
    primaryContainer = Color(0xFFFFE4EA),
    onPrimaryContainer = Color(0xFF2A0C12),
    secondary = Color(0xFFFFF0F5),
    onSecondary = Color(0xFF2B0D14),
    secondaryContainer = Color(0xFFFFFFFF),
    onSecondaryContainer = Color(0xFF2B0D14),
    tertiary = Color(0xFF8B4513),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFDEB887),
    onTertiaryContainer = Color(0xFF271403)
)

val CherryBlossomDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFFFD1DA),
    onPrimary = Color(0xFF2A0C12),
    primaryContainer = Color(0xFF6A2F3C),
    onPrimaryContainer = Color(0xFFFFE4EA),
    secondary = Color(0xFFFFF4F8),
    onSecondary = Color(0xFF2B0D14),
    secondaryContainer = Color(0xFF4C2D36),
    onSecondaryContainer = Color(0xFFFFFFFF),
    tertiary = Color(0xFFB37A45),
    onTertiary = Color(0xFF291705),
    tertiaryContainer = Color(0xFF5E3B19),
    onTertiaryContainer = Color(0xFFDEB887)
)

// Midnight Gold Theme (Black / Gold / Bronze)
val MidnightGoldLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF000000),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF424242),
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFFFFD700),
    onSecondary = Color(0xFF241E00),
    secondaryContainer = Color(0xFFFFECB3),
    onSecondaryContainer = Color(0xFF221B00),
    tertiary = Color(0xFFCD7F32),
    onTertiary = Color(0xFF2B1604),
    tertiaryContainer = Color(0xFFE9B07A),
    onTertiaryContainer = Color(0xFF2B1604)
)

val MidnightGoldDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFDEDEDE),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF1E1E1E),
    onPrimaryContainer = Color(0xFFE0E0E0),
    secondary = Color(0xFFFFE066),
    onSecondary = Color(0xFF241E00),
    secondaryContainer = Color(0xFF594C00),
    onSecondaryContainer = Color(0xFFFFECB3),
    tertiary = Color(0xFFE2A267),
    onTertiary = Color(0xFF2E1906),
    tertiaryContainer = Color(0xFF5A3A16),
    onTertiaryContainer = Color(0xFFE9B07A)
)

// Arctic Night Theme (Deep Navy / Glacier Blue / Ice White)
val ArcticNightLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF001F3F),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB3C8E0),
    onPrimaryContainer = Color(0xFF000A15),
    secondary = Color(0xFF7FDBFF),
    onSecondary = Color(0xFF002733),
    secondaryContainer = Color(0xFFD3F3FF),
    onSecondaryContainer = Color(0xFF00161E),
    tertiary = Color(0xFFF0FFFF),
    onTertiary = Color(0xFF0D1B20),
    tertiaryContainer = Color(0xFFFFFFFF),
    onTertiaryContainer = Color(0xFF0D1B20)
)

val ArcticNightDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF9DB3CC),
    onPrimary = Color(0xFF061421),
    primaryContainer = Color(0xFF0B2A49),
    onPrimaryContainer = Color(0xFFB3C8E0),
    secondary = Color(0xFFB4EDFF),
    onSecondary = Color(0xFF001A23),
    secondaryContainer = Color(0xFF00495E),
    onSecondaryContainer = Color(0xFFD3F3FF),
    tertiary = Color(0xFFE7FEFF),
    onTertiary = Color(0xFF0C1A20),
    tertiaryContainer = Color(0xFF2A3F46),
    onTertiaryContainer = Color(0xFFFFFFFF)
)

// Monochrome Theme (Black / Slate / White)
val MonochromeLightColors = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF000000),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE0E0E0),
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFF708090),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCAD4DC),
    onSecondaryContainer = Color(0xFF11171C),
    tertiary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFF151515),
    tertiaryContainer = Color(0xFFF5F5F5),
    onTertiaryContainer = Color(0xFF151515)
)

val MonochromeDarkColors = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFEBEBEB),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF1A1A1A),
    onPrimaryContainer = Color(0xFFE0E0E0),
    secondary = Color(0xFFA7B3BC),
    onSecondary = Color(0xFF0F1418),
    secondaryContainer = Color(0xFF2F3A43),
    onSecondaryContainer = Color(0xFFCAD4DC),
    tertiary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFF101010),
    tertiaryContainer = Color(0xFF3A3A3A),
    onTertiaryContainer = Color(0xFFF5F5F5)
)

// Psychedelic theme removed

// Legacy aliases for backward compatibility
val LightColors = PurpleLightColors
val DarkColors = PurpleDarkColors
