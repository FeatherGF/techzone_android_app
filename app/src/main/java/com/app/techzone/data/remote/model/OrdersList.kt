package com.app.techzone.data.remote.model

import com.google.gson.annotations.SerializedName


data class Order(
    val id: Int,
    val description: String? = null,
    val status: String,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("order_item")
    val orderItem: List<OrderItem>
)


data class OrdersList(
    val items: List<Order>
)
