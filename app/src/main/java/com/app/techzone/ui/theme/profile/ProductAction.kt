package com.app.techzone.ui.theme.profile

import androidx.compose.material3.SnackbarHostState

sealed class ProductAction {
    data class AddToCart(
        val productId: Int, val snackbarHostState: SnackbarHostState
    ): ProductAction()

    data class RemoveFromCart(
        val productId: Int, val snackbarHostState: SnackbarHostState
    ): ProductAction()

    data class ChangeQuantityInCart(val productId: Int, val quantity: Int): ProductAction()

    data class ClearCart(val snackbarHostState: SnackbarHostState): ProductAction()

    data class AddToFavorites(
        val productId: Int,
        val snackbarHostState: SnackbarHostState,
        val navigateToFavorites: () -> Unit,
    ) : ProductAction()

    data class RemoveFromFavorites(
        val productId: Int,
        val snackbarHostState: SnackbarHostState,
        val navigateToFavorites: () -> Unit,
    ): ProductAction()

}