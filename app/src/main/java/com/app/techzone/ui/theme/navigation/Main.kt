package com.app.techzone.ui.theme.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.techzone.ui.theme.app_bars.MainAppBar
import com.app.techzone.ui.theme.app_bars.SearchViewModel
import com.app.techzone.ui.theme.app_bars.SearchWidgetState
import com.app.techzone.ui.theme.cart.CartScreen
import com.app.techzone.ui.theme.catalog.CatalogCategoryScreen
import com.app.techzone.ui.theme.catalog.CatalogScreen
import com.app.techzone.ui.theme.catalog.CatalogViewModel
import com.app.techzone.ui.theme.favorite.FavoriteScreen
import com.app.techzone.ui.theme.favorite.FavoriteViewModel
import com.app.techzone.ui.theme.main.MainScreen
import com.app.techzone.ui.theme.main.ProductViewModel
import com.app.techzone.ui.theme.product_detail.ProductDetailScreen
import com.app.techzone.ui.theme.profile.ProfileScreen

@Composable
fun Main() {
    val searchViewModel = viewModel<SearchViewModel>()
    val catalogViewModel = viewModel<CatalogViewModel>()
    val favoriteViewModel = viewModel<FavoriteViewModel>()
    val productViewModel = hiltViewModel<ProductViewModel>()

    val searchSuggestions by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()
    val favorites by favoriteViewModel.favorites.collectAsStateWithLifecycle()
//    val bestSellerProducts by productViewModel.bestSellerProducts.collectAsStateWithLifecycle()
//    val newProducts by productViewModel.newProducts.collectAsStateWithLifecycle()
    val allProducts by productViewModel.allProducts.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    fun navigateToDetail(productId: Int) {
        navController.navigate("catalog/$productId")
    }

    BaseScreen(
        navController,
        favorites = favorites,
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
                        navController.currentBackStackEntry?.destination?.route?.startsWith("catalog")!!
                    ) SearchWidgetState.CATALOG_OPENED else SearchWidgetState.CLOSED

                    searchViewModel.updateSearchWidgetState(searchWidgetState)
                },
                onSearchClicked = {
                    if (searchViewModel.searchTextState.isNotEmpty()) {
                        // TODO: research in case of a much more elegant solution
                        searchViewModel.updateSearchWidgetState(SearchWidgetState.CATALOG_OPENED)
                        searchViewModel.updateCategoryNameState(searchViewModel.searchTextState)
                        navController.navigate(
                            "catalog/${searchViewModel.searchTextState}"
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
        NavHost(
            navController = navController,
            startDestination = ScreenRoutes.MAIN,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable(ScreenRoutes.MAIN) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                MainScreen(
                    navigateToDetail = ::navigateToDetail,
                    addToFavorite = favoriteViewModel::addToFavorite,
                    removeFromFavorite = favoriteViewModel::removeFromFavorite,
                    newProducts = allProducts.items,
                    bestSellerProducts = allProducts.items
                )
            }
            composable(ScreenRoutes.CATALOG) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                CatalogScreen(navController = navController, searchViewModel = searchViewModel)
            }
            composable(
                ScreenRoutes.PRODUCT_DETAIL,
                arguments = listOf(navArgument("productId") { type = NavType.IntType})
            ) {backStackEntry ->
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                val productId = backStackEntry.arguments?.getInt("productId")!!
                ProductDetailScreen(
                    productId = productId,
                    navigateToDetail = ::navigateToDetail,
                    onBackClicked = { navController.popBackStack() },
                    addToFavorite = {}
                )
            }
            composable(
                ScreenRoutes.CATALOG_CATEGORY,
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CATALOG_OPENED)
                val category = backStackEntry.arguments?.getString("category")!!
                CatalogCategoryScreen(
                    navigateToDetail = ::navigateToDetail,
                    category = category,
                    activeScreenState = catalogViewModel.activeScreenState,
                    onChangeView = searchViewModel::updateSearchWidgetState,
                    onChangeStateView = catalogViewModel::updateActiveState,
                    addToFavorite = favoriteViewModel::addToFavorite,
                    removeFromFavorite = favoriteViewModel::removeFromFavorite,
                )
            }
            composable(ScreenRoutes.CART) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                CartScreen()
            }
            composable(ScreenRoutes.FAVORITE) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                FavoriteScreen(navController = navController, favorites)
            }
            composable(ScreenRoutes.PROFILE) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                ProfileScreen()
            }
        }
    }
}