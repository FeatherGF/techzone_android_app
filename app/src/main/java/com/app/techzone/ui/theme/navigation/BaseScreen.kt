package com.app.techzone.ui.theme.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.techzone.LocalSnackbarHostState

@Composable
fun BaseScreen(
    navController: NavController,
    favoritesCount: Int,
    cartItemsCount: Int,
    topAppBar: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val screens = listOf(
        BottomItem.MainScreen,
        BottomItem.SearchScreen,
        BottomItem.CartScreen.updateCartCount(cartItemsCount),
        BottomItem.FavoriteScreen.updateFavoriteCount(favoritesCount),
        BottomItem.ProfileScreen,
    )
    val snackbarHostState = LocalSnackbarHostState.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: ""

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.imePadding()
            ){ data ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    action = {
                        data.visuals.actionLabel?.let{
                            TextButton(
                                onClick = {  data.performAction() },
                            ) { Text(it) }
                        }
                    }
                ) {
                    Text(
                        data.visuals.message,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                    )
                }
            }
        },
        content = {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background),
                content = content
            )
        },
        topBar = topAppBar,
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.tertiary,
            ) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.tertiary) {
                    screens.forEach { screen ->
                        val isSelected = currentRoute.startsWith(screen.route)
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { navController.navigate(screen.route) },
                            icon = {
                                BadgedBox(badge = {
                                    screen.badgeCount?.let {
                                        Badge {
                                            Text(
                                                text = it.toString(),
                                                color = MaterialTheme.colorScheme.tertiary
                                            )
                                        }
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