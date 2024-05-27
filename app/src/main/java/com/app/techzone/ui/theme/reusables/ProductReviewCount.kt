package com.app.techzone.ui.theme.reusables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.app.techzone.utils.formatCommonCase

@Composable
fun ProductReviewCount(
    reviewsCount: Int,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    if (reviewsCount > 0) {
        Text(
            text = formatCommonCase(reviewsCount, "отзыв"),
            style = textStyle,
            color = Color.Black.copy(alpha = 0.5f),
        )
    }
}