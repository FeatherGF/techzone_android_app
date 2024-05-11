package com.app.techzone.data.remote.model

import com.google.gson.annotations.SerializedName

data class Cart(
    val items: List<OrderItem>
)

data class AddToCartRequest(
    @SerializedName("id_product")
    val productId: Int,
)

data class ProductInCartResponse(
    @SerializedName("id_product")
    val productId: Int,
    val quantity: Int
)

data class ChangeQuantityRequest(
    val quantity: Int
)