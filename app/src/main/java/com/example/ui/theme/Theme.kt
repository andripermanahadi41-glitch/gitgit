package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF133642),
    onPrimaryContainer = NeonCyanBright,
    secondary = ValveOrange,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF3E2207),
    onSecondaryContainer = ValveOrangeBright,
    tertiary = MatrixGreen,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF14382B),
    onTertiaryContainer = MatrixGreenBright,
    background = ChassisBlack,
    onBackground = TextPrimary,
    surface = ChassisDark,
    onSurface = TextPrimary,
    surfaceVariant = ChassisPanel,
    onSurfaceVariant = TextSecondary,
    outline = ChassisBorder,
    outlineVariant = ChassisBorderSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Always preserve our hardware studio dark aesthetic
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
