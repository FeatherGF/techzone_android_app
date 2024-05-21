package com.app.techzone.data.remote.model

import androidx.compose.runtime.MutableIntState

data class OrderItem(
    val id: Int,
    val product: BaseProduct,
    val quantity: Int = 1,

    var mutableQuantity: MutableIntState
)
