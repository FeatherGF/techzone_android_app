package com.app.techzone

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.techzone.screens.BaseScreen
import com.app.techzone.screens.CartScreen
import com.app.techzone.screens.CatalogCategoryScreen
import com.app.techzone.screens.FavoriteScreen
import com.app.techzone.screens.MainAppBar
import com.app.techzone.screens.MainScreen
import com.app.techzone.screens.ProfileScreen
import com.app.techzone.screens.CatalogScreen
import com.app.techzone.screens.CatalogViewModel
import com.app.techzone.ui.theme.TechZoneTheme
import dagger.hilt.android.AndroidEntryPoint


@Composable
fun Main() {
    val searchViewModel = viewModel<SearchViewModel>()
    val catalogViewModel = viewModel<CatalogViewModel>()

    val searchSuggestions by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    BaseScreen(
        navController,
        topAppBar = {
            MainAppBar(
                searchWidgetState = searchViewModel.searchWidgetState,
                searchTextState = searchViewModel.searchTextState,
                searchSuggestions = searchSuggestions,
                categoryName = searchViewModel.categoryName,
                onTextChange = {
                    searchViewModel.updateSearchTextState(it)
                },
                onCloseClicked = {
                    searchViewModel.updateSearchTextState("")
                    val searchWidgetState = if (
                        navController.currentBackStackEntry?.destination?.route?.startsWith("catalog_screen/")!!
                    ) SearchWidgetState.CATALOG_OPENED else SearchWidgetState.CLOSED

                    searchViewModel.updateSearchWidgetState(searchWidgetState)
                },
                onSearchClicked = {
                    if (searchViewModel.searchTextState.isNotEmpty()){
                        // TODO: research in case of a much more elegant solution
                        searchViewModel.updateSearchWidgetState(SearchWidgetState.CATALOG_OPENED)
                        searchViewModel.updateCategoryNameState(searchViewModel.searchTextState)
                        navController.navigate(
                            "catalog_screen/${searchViewModel.searchTextState}"
                        )
                        searchViewModel.updateSearchTextState("")
                    }
                },
                onSearchTriggered = {
                    searchViewModel.updateSearchWidgetState(SearchWidgetState.OPENED)
                },
                onBackClicked = { navController.popBackStack() }
            )
        },
    ) {
        NavHost(navController = navController, startDestination = "main_screen") {
            composable("main_screen") {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                MainScreen()
            }
            composable("catalog_screen") {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                CatalogScreen(navController = navController, searchViewModel = searchViewModel)
            }
            composable(
                "catalog_screen/{category}",
                arguments = listOf(navArgument("category") {type = NavType.StringType})
            ){ backStackEntry ->
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CATALOG_OPENED)
                val category = backStackEntry.arguments?.getString("category")!!
                CatalogCategoryScreen(
                    category = category,
                    activeScreenState = catalogViewModel.activeScreenState,
                    onChangeView = searchViewModel::updateSearchWidgetState,
                    onChangeStateView = catalogViewModel::updateActiveState
                )
            }
            composable("cart_screen") {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                CartScreen()
            }
            composable("favorite_screen") {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                FavoriteScreen()
            }
            composable("profile_screen") {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                ProfileScreen()
            }
        }
    }
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // market places don't support landscape orientations
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            TechZoneTheme {
                Main()
            }
        }
    }
}
