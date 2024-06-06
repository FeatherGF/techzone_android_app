package com.app.techzone.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val extraSmall: Dp,
    val small: Dp,
    val medium: Dp,
    val extendedMedium: Dp,
    val mediumLarge: Dp,
    val large: Dp,
    val larger: Dp,
    val extraLarge: Dp,
    val huge: Dp,
)

val compactSmallDimensions = Dimensions(
    extraSmall = 2.dp,
    small = 6.dp,
    medium = 8.dp,
    extendedMedium = 12.dp,
    mediumLarge = 16.dp,
    large = 20.dp,
    larger = 28.dp,
    extraLarge = 34.dp,
    huge = 54.dp,
)

val compactMediumDimensions = Dimensions(
    extraSmall = 4.dp,
    small = 8.dp,
    medium = 12.dp,
    extendedMedium = 16.dp,
    mediumLarge = 20.dp,
    large = 24.dp,
    larger = 32.dp,
    extraLarge = 40.dp,
    huge = 60.dp,
)

val compactDimensions = Dimensions(
    extraSmall = 6.dp,
    small = 10.dp,
    medium = 16.dp,
    extendedMedium = 20.dp,
    mediumLarge = 24.dp,
    large = 28.dp,
    larger = 34.dp,
    extraLarge = 42.dp,
    huge = 62.dp,
)

val mediumDimensions = Dimensions(
    extraSmall = 10.dp,
    small = 14.dp,
    medium = 20.dp,
    extendedMedium = 24.dp,
    mediumLarge = 28.dp,
    large = 32.dp,
    larger = 40.dp,
    extraLarge = 48.dp,
    huge = 68.dp
)

val expandedDimensions = Dimensions(
    extraSmall = 14.dp,
    small = 18.dp,
    medium = 24.dp,
    extendedMedium = 28.dp,
    mediumLarge = 32.dp,
    large = 36.dp,
    larger = 44.dp,
    extraLarge = 52.dp,
    huge = 72.dp
)
