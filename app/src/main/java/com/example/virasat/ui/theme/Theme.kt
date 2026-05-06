package com.example.virasat.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = VirasatMaroon,
    onPrimary = Color.White,
    primaryContainer = MaroonContainer,
    onPrimaryContainer = MaroonOnContainer,
    secondary = VirasatGold,
    onSecondary = Color.White,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = GoldOnContainer,
    tertiary = Color(0xFF5B6A44),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFDEF0C1),
    onTertiaryContainer = Color(0xFF1A2E0A),
    background = VirasatCream,
    onBackground = VirasatText,
    surface = VirasatSurface,
    onSurface = VirasatText,
    surfaceVariant = Color(0xFFF0EBE0),
    onSurfaceVariant = Color(0xFF49463F),
    outline = Color(0xFF7B766C),
    outlineVariant = Color(0xFFCCC6BA),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

@Composable
fun VirasatTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = VirasatTypography,
        shapes = VirasatShapes,
        content = content
    )
}