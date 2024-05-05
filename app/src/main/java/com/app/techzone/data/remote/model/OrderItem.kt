package com.app.techzone.data.remote.model

import androidx.compose.runtime.MutableIntState

data class OrderItem(
    val product: BaseProduct,
    // TODO: change to val when backend adds quantity in responses
    var quantity: Int = 1,

    var mutableQuantity: MutableIntState
)
