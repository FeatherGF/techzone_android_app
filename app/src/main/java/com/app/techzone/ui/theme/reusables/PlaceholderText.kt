package com.app.techzone.ui.theme.reusables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.app.techzone.ui.theme.DarkText

@Composable
fun PlaceholderText(text: String) =
    Text(
        text,
        style = MaterialTheme.typography.bodyLarge,
        color = DarkText
    )