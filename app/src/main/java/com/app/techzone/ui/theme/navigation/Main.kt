package com.app.techzone.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.ui.theme.app_bars.MainAppBar
import com.app.techzone.ui.theme.app_bars.SearchViewModel
import com.app.techzone.ui.theme.app_bars.SearchTopBarState
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
import com.app.techzone.ui.theme.reviews.ReviewScreenRoot

@Composable
fun Main(navController: NavHostController) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val searchViewModel = hiltViewModel<SearchViewModel>()
    val paymentViewModel = hiltViewModel<PaymentViewModel>()
    val catalogViewModel = hiltViewModel<CatalogViewModel>()
    val productViewModel = hiltViewModel<ProductViewModel>()

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
        topAppBar = { MainAppBar(searchViewModel) },
    ) {
        NavHost(navController = navController, startDestination = ScreenRoutes.MAIN) {
            composable(ScreenRoutes.PRIVACY_POLICY) {
                PrivacyPolicy()
            }
            composable(ScreenRoutes.MAIN) {
                searchViewModel.updateSearchTopBarState(SearchTopBarState.CLOSED)
                MainScreen(
                    productViewModel = productViewModel,
                    onProductAction = userViewModel::onProductAction,
                )
            }
            composable(ScreenRoutes.CATALOG) {
                searchViewModel.updateSearchTopBarState(SearchTopBarState.CLOSED)
                CatalogScreen(onEvent = searchViewModel::onEvent)
            }
            composable(
                ScreenRoutes.PRODUCT_DETAIL + "/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType})
            ) {backStackEntry ->
                searchViewModel.updateSearchTopBarState(SearchTopBarState.HIDDEN)
                val productId = backStackEntry.arguments?.getInt("productId")!!
                ProductDetailScreen(
                    productId = productId,
                    onProductAction = userViewModel::onProductAction
                )
            }
            composable(
                ScreenRoutes.CATALOG_CATEGORY,
                arguments = listOf(navArgument("searchText") { type = NavType.StringType })
            ) { backStackEntry ->
                val searchText = backStackEntry.arguments?.getString("searchText")!!
                CatalogCategoryScreen(
                    searchText = searchText,
                    catalogViewModel = catalogViewModel,
                    onChangeView = searchViewModel::updateSearchTopBarState,
                    onProductAction = userViewModel::onProductAction
                )
            }
            composable(ScreenRoutes.CART) {
                searchViewModel.updateSearchTopBarState(SearchTopBarState.CLOSED)
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
                searchViewModel.updateSearchTopBarState(SearchTopBarState.HIDDEN)
                PurchaseScreenRoot(
                    userViewModel = userViewModel,
                    paymentViewModel = paymentViewModel,
                    orderItemIds = orderItemIds!!.toList()
                )
            }
            composable(ScreenRoutes.ORDERS) {
                searchViewModel.updateSearchTopBarState(SearchTopBarState.HIDDEN)
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
                searchViewModel.updateSearchTopBarState(SearchTopBarState.HIDDEN)
                val orderId = backStackEntry.arguments?.getInt("orderId")
                val productId = backStackEntry.arguments?.getInt("productId")
                if (orderId != null && productId != null) {
                    ReviewScreenRoot(
                        userViewModel, orderId, productId
                    )
                } else {
                    navController.popBackStack()
                }
            }
            composable(ScreenRoutes.PAY_METHOD){
                searchViewModel.updateSearchTopBarState(SearchTopBarState.HIDDEN)
                PaymentSelectionRoot(paymentViewModel)
            }
            composable(ScreenRoutes.FAVORITE) {
                searchViewModel.updateSearchTopBarState(SearchTopBarState.CLOSED)
                FavoriteScreen(
                    favorites = favorites,
                    favoriteState = userViewModel.state.response,
                    loadFavorites = userViewModel::loadFavorites,
                    onProductAction = userViewModel::onProductAction
                )
            }
            composable(ScreenRoutes.PROFILE) {
                searchViewModel.updateSearchTopBarState(
                    if (authResultState is AuthResult.Authorized) SearchTopBarState.HIDDEN
                    else SearchTopBarState.CLOSED
                )
                ProfileScreen(
                    authResultState = authResultState,
                    userViewModel = userViewModel,
                )
            }
            composable(ScreenRoutes.EDIT_PROFILE){
                searchViewModel.updateSearchTopBarState(SearchTopBarState.HIDDEN)
                EditUserProfile(userViewModel = userViewModel)
            }
            composable(ScreenRoutes.PROFILE_REGISTRATION){
                searchViewModel.updateSearchTopBarState(SearchTopBarState.HIDDEN)
                Authorization(
                    userViewModel = userViewModel,
                    authResultState = authResultState,
                )
            }
        }
    }
}