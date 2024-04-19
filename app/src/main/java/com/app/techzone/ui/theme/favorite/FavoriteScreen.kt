package com.app.techzone.ui.theme.favorite

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.app.techzone.model.ProductCard


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