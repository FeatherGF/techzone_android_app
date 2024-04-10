package com.app.techzone.ui.theme.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.techzone.model.ProductCard
import kotlinx.coroutines.flow.StateFlow


@Composable
fun FavoriteScreen(
    navController: NavController,
    favorites: List<ProductCard>
) {
    if (favorites.isEmpty()) {
        EmptyFavoriteScreen()
    } else {
        FavoriteList()
    }
}


@Composable
fun EmptyFavoriteScreen() {
    
}


@Composable
fun FavoriteList() {

}