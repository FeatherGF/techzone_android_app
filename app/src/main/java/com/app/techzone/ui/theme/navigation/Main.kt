package com.app.techzone.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.app.techzone.ui.theme.payment_selection.PaymentSelectionRoot
import com.app.techzone.ui.theme.payment_selection.PaymentViewModel
import com.app.techzone.ui.theme.privacy_policy.PrivacyPolicy
import com.app.techzone.ui.theme.product_detail.ProductDetailScreen
import com.app.techzone.ui.theme.profile.UserViewModel
import com.app.techzone.ui.theme.profile.ProfileScreen
import com.app.techzone.ui.theme.profile.Authorization
import com.app.techzone.ui.theme.profile.EditUserProfile
import com.app.techzone.ui.theme.purchase.PurchaseScreenRoot
import com.app.techzone.ui.theme.reviews.AddReviewScreenRoot

@Composable
fun Main(navController: NavHostController) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val searchViewModel = viewModel<SearchViewModel>()
    val paymentViewModel = hiltViewModel<PaymentViewModel>()
    val catalogViewModel = hiltViewModel<CatalogViewModel>()
    val productViewModel = hiltViewModel<ProductViewModel>()

    val searchSuggestions by searchViewModel.searchSuggestions.collectAsStateWithLifecycle()
    val authResultState by userViewModel.authResults.collectAsState(userViewModel.initialState)

    // load user data when app launches
    LaunchedEffect(Unit) {
        val response = userViewModel.authenticate()
        if (response is AuthResult.Authorized<Unit>){
            userViewModel.loadCart()
            userViewModel.loadFavorites()
        }
    }
    val cartItems by userViewModel.cartItems.collectAsState()
    val favorites by userViewModel.favorites.collectAsState()

    BaseScreen(
        navController,
        favorites = favorites,
        cartItems = cartItems,
        topAppBar = {
            MainAppBar(
                // TODO: rewrite using MVI
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
                            "${ScreenRoutes.CATALOG}/${searchViewModel.searchTextState}"
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
            composable(ScreenRoutes.PRIVACY_POLICY) {
                PrivacyPolicy()
            }
            composable(ScreenRoutes.MAIN) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                MainScreen(
                    productViewModel = productViewModel,
                    onProductAction = userViewModel::onProductAction,
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
                    onProductAction = userViewModel::onProductAction
                )
            }
            composable(
                ScreenRoutes.CATALOG_CATEGORY,
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CATALOG_OPENED)
                val category = backStackEntry.arguments?.getString("category")!!
                CatalogCategoryScreen(
                    category = category,
                    onChangeView = searchViewModel::updateSearchWidgetState,
                    catalogViewModel = catalogViewModel,
                    onProductAction = userViewModel::onProductAction
                )
            }
            composable(ScreenRoutes.CART) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                CartScreen(
                    cartItems = cartItems,
                    state = userViewModel.state,
                    loadCart = userViewModel::loadCart,
                    onProductAction = userViewModel::onProductAction
                )
            }
            composable(
                ScreenRoutes.PURCHASE + "?orderItem={orderItem}",
                arguments = listOf(navArgument("orderItem") {type = NavType.IntArrayType})
            ) { backStackEntry ->
                val orderItemIds = backStackEntry.arguments?.getIntArray("orderItem")
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                PurchaseScreenRoot(
                    userViewModel = userViewModel,
                    paymentViewModel = paymentViewModel,
                    orderItemIds = orderItemIds!!.toList()
                )
            }
            composable(ScreenRoutes.ORDERS) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                OrderScreenRoot(
                    userViewModel = userViewModel,
                )
            }
            composable(
                ScreenRoutes.ADD_REVIEW + "?orderId={orderId}&productId={productId}",
                arguments = listOf(
                    navArgument("orderId") {type = NavType.IntType},
                    navArgument("productId") {type = NavType.IntType},
                )
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getInt("orderId")
                val productId = backStackEntry.arguments?.getInt("productId")
                if (orderId != null && productId != null) {
                    AddReviewScreenRoot(
                        userViewModel, orderId, productId
                    )
                } else {
                    navController.popBackStack()
                }
            }
            composable(ScreenRoutes.PAY_METHOD){
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                PaymentSelectionRoot(paymentViewModel)
            }
            composable(ScreenRoutes.FAVORITE) {
                searchViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                FavoriteScreen(
                    favorites = favorites,
                    favoriteState = userViewModel.state.response,
                    loadFavorites = userViewModel::loadFavorites,
                    onProductAction = userViewModel::onProductAction
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
                )
            }
            composable(ScreenRoutes.EDIT_PROFILE){
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                EditUserProfile(userViewModel = userViewModel)
            }
            composable(ScreenRoutes.PROFILE_REGISTRATION){
                searchViewModel.updateSearchWidgetState(SearchWidgetState.HIDDEN)
                Authorization(
                    userViewModel = userViewModel,
                    authResultState = authResultState,
                )
            }
        }
    }
}