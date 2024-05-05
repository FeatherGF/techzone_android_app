package com.app.techzone.ui.theme.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.ui.theme.app_bars.MainAppBar
import com.app.techzone.ui.theme.app_bars.SearchViewModel
import com.app.techzone.ui.theme.app_bars.SearchWidgetState
import com.app.techzone.ui.theme.cart.CartScreen
import com.app.techzone.ui.theme.catalog.CatalogCategoryScreen
import com.app.techzone.ui.theme.catalog.CatalogScreen
import com.app.techzone.ui.theme.catalog.CatalogViewModel
import com.app.techzone.ui.theme.favorite.FavoriteScreen
import com.app.techzone.ui.theme.main.MainScreen
import com.app.techzone.ui.theme.main.ProductViewModel
import com.app.techzone.ui.theme.orders.OrderScreenRoot
import com.app.techzone.ui.theme.product_detail.ProductDetailScreen
import com.app.techzone.ui.theme.profile.auth.UserViewModel
import com.app.techzone.ui.theme.profile.ProfileScreen
import com.app.techzone.ui.theme.profile.Authorization
import com.app.techzone.ui.theme.profile.EditUserProfile
import com.app.techzone.ui.theme.purchase.PurchaseScreenRoot

@Composable
fun Main() {
    val searchViewModel = viewModel<SearchViewModel>()
    val catalogViewModel = hiltViewModel<CatalogViewModel>()
    val productViewModel = hiltViewModel<ProductViewModel>()
    val userViewModel = hiltViewModel<UserViewModel>()

    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    val searchSuggestions by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()
    val authResultState by userViewModel.authResults.collectAsState(userViewModel.initialState)
    val favorites by userViewModel.favorites.collectAsState()

//    val orders by userViewModel.orders.collectAsState()

    fun navigateToDetail(productId: Int) {
        navController.navigate("catalog/$productId")
    }

    val navigateToFavorite = { navController.navigate(ScreenRoutes.FAVORITE) }

    fun addToFavorite(productId: Int) = userViewModel.addToFavorite(
        productId, snackbarHostState, navigateToFavorite
    )
    fun removeFromFavorite(productId: Int) = userViewModel.removeFromFavorite(
        productId, snackbarHostState, navigateToFavorite
    )

    BaseScreen(
        navController,
        favorites = favorites,
        cartItems = emptyList(), // preparation for future
        snackbarHostState = snackbarHostState,
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
        NavHost(navController = navController, startDestination = ScreenRoutes.MAIN) {
            composable(ScreenRoutes.MAIN) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                MainScreen(
                    navigateToDetail = ::navigateToDetail,
                    addToFavorite = ::addToFavorite,
                    removeFromFavorite = ::removeFromFavorite,
                    productViewModel = productViewModel,
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
                    addToFavorite = ::addToFavorite,
                    removeFromFavorite = ::removeFromFavorite,
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
                    onChangeView = searchViewModel::updateSearchWidgetState,
                    catalogViewModel = catalogViewModel,
                    addToFavorite = ::addToFavorite,
                    removeFromFavorite = ::removeFromFavorite,
                )
            }
            composable(ScreenRoutes.CART) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                CartScreen(
                    navController = navController,
                    userViewModel = userViewModel,
                    addToFavorite = ::addToFavorite,
                    removeFromFavorite = ::removeFromFavorite,
                )
            }
            composable(ScreenRoutes.PURCHASE) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                PurchaseScreenRoot(
                    userViewModel = userViewModel,
                    navController = navController,
                    snackbarHostState = snackbarHostState
                )
            }
            composable(ScreenRoutes.ORDERS) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)

                OrderScreenRoot(
                    userViewModel = userViewModel,
                    navController = navController,
//                    orders = orders.items
                )
            }
            composable(ScreenRoutes.PAY_METHOD){
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                Text("pay method")
            }
            composable(ScreenRoutes.FAVORITE) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                FavoriteScreen(
                    navController = navController,
                    favorites = favorites,
                    favoriteState = userViewModel.state.response,
                    loadFavorites = userViewModel::loadFavorites,
                    addToFavorite = ::addToFavorite,
                    removeFromFavorite = ::removeFromFavorite
                )
            }
            composable(ScreenRoutes.PROFILE) {
                searchViewModel.updateSearchWidgetState(
                    if (authResultState is AuthResult.Authorized) SearchWidgetState.HIDDEN
                    else SearchWidgetState.CLOSED
                )
                ProfileScreen(
                    authResultState = authResultState,
                    userViewModel = userViewModel,
                    navController = navController,
                )
            }
            composable(ScreenRoutes.EDIT_PROFILE){
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                EditUserProfile(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    userViewModel = userViewModel,
                    onBackClicked = { navController.popBackStack() },
                )
            }
            composable(ScreenRoutes.PROFILE_REGISTRATION){
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                Authorization(
                    userViewModel = userViewModel,
                    authResultState = authResultState,
                    onBackClicked = { navController.popBackStack() },
                    navigateToProfile = {
                        navController.navigate(ScreenRoutes.PROFILE) {
                            popUpTo(ScreenRoutes.PROFILE)
                        }
                    }
                )
            }
        }
    }
}