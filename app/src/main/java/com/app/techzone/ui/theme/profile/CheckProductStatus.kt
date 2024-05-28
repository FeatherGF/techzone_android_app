package com.app.techzone.ui.theme.profile

sealed class CheckProductStatus {
    data class CheckProductIsFavorite(val productId: Int) : CheckProductStatus()
    data class CheckProductInCart(val productId: Int) : CheckProductStatus()
}
