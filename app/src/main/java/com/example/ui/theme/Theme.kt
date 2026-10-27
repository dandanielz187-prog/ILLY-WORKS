package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val IllyDarkColorScheme = darkColorScheme(
    primary = IllyRed,
    onPrimary = IllyWhite,
    primaryContainer = IllyRedDark,
    onPrimaryContainer = IllyWhite,
    secondary = IllyBlue,
    onSecondary = IllyWhite,
    secondaryContainer = IllyBlueDark,
    onSecondaryContainer = IllyWhite,
    tertiary = IllyCyan,
    onTertiary = IllyWhite,
    background = IllyBgDark,
    onBackground = IllyWhite,
    surface = IllySurfaceDark,
    onSurface = IllyWhite,
    surfaceVariant = IllyCardGlass,
    onSurfaceVariant = IllyTextSecondary,
    outline = IllyBorder,
    outlineVariant = IllyBorderHighlight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // Always enforce ILLY's premium red/blue/black brand identity
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = IllyDarkColorScheme,
        typography = Typography,
        content = content
    )
}
