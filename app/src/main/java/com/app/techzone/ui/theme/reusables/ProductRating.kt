package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.app.techzone.ui.theme.dimension

@Composable
fun ProductRating(
    rating: Float? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    isStarFilled: Boolean = false
) {
    rating?.let {
        if (it > 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = it.toString(),
                    style = textStyle,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (isStarFilled) Icons.Filled.StarRate else Icons.Outlined.StarRate,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(MaterialTheme.dimension.extendedMedium)
                )
            }
        }
    }
}