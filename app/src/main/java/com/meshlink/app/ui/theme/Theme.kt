package com.meshlink.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4FC3F7),
    primaryContainer = Color(0xFF006064),
    secondary = Color(0xFF81D4FA),
    secondaryContainer = Color(0xFF01579B),
    tertiary = Color(0xFF4DD0E1),
    tertiaryContainer = Color(0xFF006064),
    surface = Color(0xFF121212),
    surfaceVariant = Color(0xFF1E1E1E),
    background = Color(0xFF000000),
    onPrimary = Color(0xFF000000),
    onPrimaryContainer = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onSecondaryContainer = Color(0xFFFFFFFF),
    onTertiary = Color(0xFF000000),
    onTertiaryContainer = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onSurfaceVariant = Color(0xFFCCCCCC),
    onBackground = Color(0xFFFFFFFF),
    outline = Color(0xFF757575),
    outlineVariant = Color(0xFF424242),
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = Color(0xFF212121),
    inversePrimary = Color(0xFF009688)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF009688),
    primaryContainer = Color(0xFFB2DFDB),
    secondary = Color(0xFF00ACC1),
    secondaryContainer = Color(0xFFB2EBF2),
    tertiary = Color(0xFF00BCD4),
    tertiaryContainer = Color(0xFFB2EBF2),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF5F5F5),
    background = Color(0xFFFAFAFA),
    onPrimary = Color(0xFFFFFFFF),
    onPrimaryContainer = Color(0xFF000000),
    onSecondary = Color(0xFFFFFFFF),
    onSecondaryContainer = Color(0xFF000000),
    onTertiary = Color(0xFFFFFFFF),
    onTertiaryContainer = Color(0xFF000000),
    onSurface = Color(0xFF212121),
    onSurfaceVariant = Color(0xFF616161),
    onBackground = Color(0xFF212121),
    outline = Color(0xFFBDBDBD),
    outlineVariant = Color(0xFFE0E0E0),
    inverseSurface = Color(0xFF212121),
    inverseOnSurface = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFF4FC3F7)
)

@Composable
fun MeshLinkTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}