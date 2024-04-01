package com.app.techzone.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FavoriteScreen() {
    Column {
        Text(text = "Favorite Screen", modifier = Modifier.padding(24.dp))
    }
}
