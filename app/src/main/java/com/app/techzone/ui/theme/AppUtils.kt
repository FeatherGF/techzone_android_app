package com.app.techzone.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

@Composable
fun ProvideAppUtils(
    dimensions: Dimensions,
    content: @Composable () -> Unit,
) {
    val appDimensions = remember { dimensions }
    CompositionLocalProvider(LocalDimensions provides appDimensions) {
        content()
    }
}

val LocalDimensions = compositionLocalOf { compactMediumDimensions }
