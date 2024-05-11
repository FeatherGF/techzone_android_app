package com.app.techzone.data.remote.model

import androidx.compose.runtime.MutableIntState

data class OrderItem(
    var id: Int,
    val product: BaseProduct,
    val quantity: Int = 1,

    var mutableQuantity: MutableIntState
)
