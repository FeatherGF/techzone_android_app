package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import com.app.techzone.ui.theme.dimension
import com.app.techzone.utils.formatPrice

@Composable
fun ProductCrossedPrice(price: Int, discountPercentage: Int, large: Boolean = false) {
    if (discountPercentage > 0) {
        val style = if (large) MaterialTheme.typography.labelLarge else
            MaterialTheme.typography.labelSmall
        Text(
            text = formatPrice(price),
            style = style,
            color = MaterialTheme.colorScheme.scrim,
            textDecoration = TextDecoration.LineThrough,
            modifier = Modifier.padding(bottom = MaterialTheme.dimension.extraSmall)
        )
    }
}

@Composable
fun ProductCrossedPrice(price: Int, large: Boolean = false) {
    val style = if (large) MaterialTheme.typography.labelLarge else
        MaterialTheme.typography.labelSmall
    Text(
        text = formatPrice(price),
        style = style,
        color = MaterialTheme.colorScheme.scrim,
        textDecoration = TextDecoration.LineThrough,
        modifier = Modifier.padding(bottom = MaterialTheme.dimension.extraSmall)
    )
}