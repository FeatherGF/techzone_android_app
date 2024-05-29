package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun ProductRating(
    rating: Float? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    isStarFilled: Boolean = false
) {
    rating?.let {
        if (it > 0) {
            Row {
                Text(
                    text = it.toString(),
                    style = textStyle,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (isStarFilled) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .height(14.dp)
                        .width(14.dp)
                )
            }
        }
    }
}