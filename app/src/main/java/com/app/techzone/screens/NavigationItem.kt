package com.app.techzone.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomItem(
    val title: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector,
    var badgeCount: Int?,
    val route: String
) {

    data object MainScreen :
        BottomItem(
            "Главная",
            Icons.Filled.Home,
            Icons.Outlined.Home,
            null,
            "main_screen"
        )

    data object SearchScreen :
        BottomItem(
            "Каталог",
            Icons.Filled.Search,
            Icons.Outlined.Search,
            null,
            "catalog_screen"
        )

    data object FavoriteScreen :
        BottomItem(
            "Избранное",
            Icons.Filled.Favorite,
            Icons.Outlined.FavoriteBorder,
            1,
            "favorite_screen"
        )

    data object CartScreen :
        BottomItem(
            "Корзина",
            Icons.Filled.ShoppingCart,
            Icons.Outlined.ShoppingCart,
            1,
            "cart_screen"
        )

    data object ProfileScreen :
        BottomItem(
            "Профиль",
            Icons.Filled.Person,
            Icons.Outlined.Person,
            null,
            "profile_screen"
        )
}