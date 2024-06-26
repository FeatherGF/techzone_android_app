package com.app.techzone.ui.theme.navigation

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
            ScreenRoutes.MAIN
        )

    data object SearchScreen :
        BottomItem(
            "Каталог",
            Icons.Filled.Search,
            Icons.Outlined.Search,
            null,
            ScreenRoutes.CATALOG
        )

    data object FavoriteScreen :
        BottomItem(
            "Избранное",
            Icons.Filled.Favorite,
            Icons.Outlined.FavoriteBorder,
            null,
            ScreenRoutes.FAVORITE
        ) {
            fun updateFavoriteCount(newValue: Int): BottomItem {
                badgeCount = if (newValue == 0) null else newValue
                return this
            }
        }

    data object CartScreen :
        BottomItem(
            "Корзина",
            Icons.Filled.ShoppingCart,
            Icons.Outlined.ShoppingCart,
            null,
            ScreenRoutes.CART
        ) {
            fun updateCartCount(newValue: Int): BottomItem {
                badgeCount = if (newValue == 0) null else newValue
                return this
            }
        }

    data object ProfileScreen :
        BottomItem(
            "Профиль",
            Icons.Filled.Person,
            Icons.Outlined.Person,
            null,
            ScreenRoutes.PROFILE
        )
}