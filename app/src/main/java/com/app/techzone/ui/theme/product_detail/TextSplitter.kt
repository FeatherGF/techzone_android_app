package com.app.techzone.ui.theme.product_detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Этот компонент создает точки между двумя элементами текста (как обычно делают в содержаниях отчетов)
 */
@Composable
fun TextSplitter(
    color: Color,
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
) = Canvas(modifier.fillMaxWidth().height(thickness)) {
    drawLine(
        color = color,
        strokeWidth = thickness.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), phase = 0f),
        start = Offset(0f, thickness.toPx() / 2),
        end = Offset(size.width, thickness.toPx() / 2),
    )
}