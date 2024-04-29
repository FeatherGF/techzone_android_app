package com.app.techzone.ui.theme.navigation

import androidx.compose.material3.SnackbarHostState
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
import com.app.techzone.ui.theme.favorite.FavoriteViewModel
import com.app.techzone.ui.theme.main.MainScreen
import com.app.techzone.ui.theme.main.ProductViewModel
import com.app.techzone.ui.theme.product_detail.ProductDetailScreen
import com.app.techzone.ui.theme.profile.auth.UserViewModel
import com.app.techzone.ui.theme.profile.ProfileScreen
import com.app.techzone.ui.theme.profile.Authorization
import com.app.techzone.ui.theme.profile.EditUserProfile
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse

@Composable
fun Main() {
    val searchViewModel = viewModel<SearchViewModel>()
    val catalogViewModel = viewModel<CatalogViewModel>()
    val favoriteViewModel = viewModel<FavoriteViewModel>()
    val productViewModel = hiltViewModel<ProductViewModel>()
    val userViewModel = hiltViewModel<UserViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }

    val searchSuggestions by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()
    val favorites by favoriteViewModel.favorites.collectAsStateWithLifecycle()
    val allProducts by productViewModel.allProducts.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val authResultState by userViewModel.authResults.collectAsState(userViewModel.initialState)

    fun navigateToDetail(productId: Int) {
        navController.navigate("catalog/$productId"){
            popUpTo(ScreenRoutes.PRODUCT_DETAIL)
        }
    }

    BaseScreen(
        navController,
        favorites = favorites,
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
                when (productViewModel.state.response){
                    ServerResponse.LOADING -> { LoadingBox() }
                    ServerResponse.ERROR -> {
                        ErrorScreen(onRefreshApiCall = productViewModel::loadMainProducts)
                    }
                    ServerResponse.SUCCESS -> {
                        MainScreen(
                            navigateToDetail = ::navigateToDetail,
                            addToFavorite = favoriteViewModel::addToFavorite,
                            removeFromFavorite = favoriteViewModel::removeFromFavorite,
                            newProducts = allProducts.items,
                            bestSellerProducts = allProducts.items
                        )
                    }
                }
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