package com.app.techzone.ui.theme.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.techzone.model.ProductCard

@Composable
fun BaseScreen(
    navController: NavController,
    favorites: List<ProductCard>,
    topAppBar: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val screens = listOf(
        BottomItem.MainScreen,
        BottomItem.SearchScreen,
        BottomItem.CartScreen,
        BottomItem.FavoriteScreen.updateFavoriteCount(favorites.size),
        BottomItem.ProfileScreen,
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ""

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        content = {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background),
                content = content
            )
        },
        topBar = { topAppBar() },
        bottomBar = {
            BottomAppBar(containerColor = MaterialTheme.colorScheme.tertiary) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.tertiary) {
                    screens.forEach { screen ->
                        val isSelected = currentRoute.startsWith(screen.route)
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { navController.navigate(screen.route) },
                            icon = {
                                BadgedBox(badge = {
                                    screen.badgeCount?.let {
                                        Badge { Text(text = it.toString()) }
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (isSelected) screen.iconFilled else screen.iconOutlined,
                                        contentDescription = "Иконка навигации для раздела ${screen.title}",
                                    )
                                }
                            },
                            label = { Text(text = screen.title) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            )
                        )
                    }
                }
            }
        }
    )
}