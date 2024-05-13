package com.app.techzone.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MainColorScheme = lightColorScheme(
    primary = AccentBlue,
    secondary = ActiveBlue,
    tertiary = BackgroundWhite,

    background = BoxBackground,
    secondaryContainer = LightBlue,
    scrim = DarkBlueText,
    onError = ErrorRed,
)

@Composable
fun TechZoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = MainColorScheme.primary.toArgb()
            window.navigationBarColor = MainColorScheme.tertiary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = MainColorScheme,
        typography = Typography,
        content = content
    )
}