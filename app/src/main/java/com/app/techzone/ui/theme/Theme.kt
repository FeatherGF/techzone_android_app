package com.app.techzone.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.app.techzone.MainActivity

private val MainColorScheme = lightColorScheme(
    primary = AccentBlue,
    secondary = ActiveBlue,
    tertiary = BackgroundWhite,

    background = BoxBackground,
    secondaryContainer = LightBlue,
    scrim = DarkBlueText,
    onError = ErrorRed,
)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TechZoneTheme(content: @Composable () -> Unit) {
    val activity = (LocalContext.current) as MainActivity
    val window = calculateWindowSizeClass(activity = activity)
    val config = LocalConfiguration.current

    val dimensions = when(window.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            when {
                config.screenWidthDp <= 360 -> compactSmallDimensions
                config.screenWidthDp < 599 -> compactMediumDimensions
                else -> compactDimensions
            }
        }
        WindowWidthSizeClass.Medium -> mediumDimensions
        else -> expandedDimensions
    }
    ProvideAppUtils(dimensions = dimensions) {
        MaterialTheme(
            colorScheme = MainColorScheme,
            typography = Typography,
            content = content
        )
    }
}

val MaterialTheme.dimension
    @Composable
    get() = LocalDimensions.current