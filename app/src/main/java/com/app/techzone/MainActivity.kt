package com.app.techzone

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.techzone.screens.BaseScreen
import com.app.techzone.screens.CartScreen
import com.app.techzone.screens.FavoriteScreen
import com.app.techzone.screens.MainAppBar
import com.app.techzone.screens.MainScreen
import com.app.techzone.screens.ProfileScreen
import com.app.techzone.screens.SearchScreen
import com.app.techzone.ui.theme.TechZoneTheme


@Composable
fun Main(searchViewModel: SearchViewModel = SearchViewModel()) {
    val searchSuggestions by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    BaseScreen(
        navController,
        topAppBar = {
            MainAppBar(
                searchWidgetState = searchViewModel.searchWidgetState,
                searchTextState = searchViewModel.searchTextState,
                searchSuggestions = searchSuggestions,
                onTextChange = {
                    searchViewModel.updateSearchTextState(it)
                },
                onCloseClicked = {
                    searchViewModel.updateSearchTextState("")
                    searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                },
                onSearchClicked = {
                    if (searchViewModel.searchTextState.isNotEmpty()){
                        // TODO: send request to API
                        Log.i("Search", it)
                        searchViewModel.updateSearchTextState("")
                        searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                    }
                },
                onSearchTriggered = {
                    searchViewModel.updateSearchWidgetState(SearchWidgetState.OPENED)
                },
            )
        },
    ) {
        NavHost(navController = navController, startDestination = "main_screen") {
            composable("main_screen") {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                MainScreen()
            }
            composable("search_screen") {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                SearchScreen()
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


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TechZoneTheme {
                Main()
            }
        }
    }
}
