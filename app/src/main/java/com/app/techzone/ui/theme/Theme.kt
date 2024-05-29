package com.app.techzone.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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
fun TechZoneTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MainColorScheme,
        typography = Typography,
        content = content
    )
}