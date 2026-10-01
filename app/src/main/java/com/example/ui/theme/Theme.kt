package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = MintAccent,
    onPrimary = ForestGreenBg,
    primaryContainer = ForestGreenSurfaceVariant,
    onPrimaryContainer = MintAccentLight,
    secondary = MintAccentLight,
    onSecondary = ForestGreenBg,
    secondaryContainer = ForestGreenSurface,
    onSecondaryContainer = SoftCream,
    tertiary = AdGold,
    onTertiary = ForestGreenBg,
    background = ForestGreenBg,
    onBackground = SoftCream,
    surface = ForestGreenSurface,
    onSurface = SoftCream,
    surfaceVariant = ForestGreenSurfaceVariant,
    onSurfaceVariant = SoftCreamMuted,
    outline = ForestGreenBorder,
)

private val LightColorScheme = lightColorScheme(
    primary = MintAccentGlow,
    onPrimary = SoftCream,
    primaryContainer = LightSurfaceVariant,
    onPrimaryContainer = LightText,
    secondary = MintAccent,
    onSecondary = SoftCream,
    secondaryContainer = LightSurface,
    onSecondaryContainer = LightText,
    tertiary = AdGold,
    onTertiary = SoftCream,
    background = LightBg,
    onBackground = LightText,
    surface = LightSurface,
    onSurface = LightText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextMuted,
    outline = LightBorder,
)

@Composable
fun NexChatTheme(
    darkTheme: Boolean = true, // Dark mode is primary by default for NexChat
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
