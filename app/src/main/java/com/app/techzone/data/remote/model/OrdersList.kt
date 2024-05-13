package com.app.techzone.data.remote.model

import com.google.gson.annotations.SerializedName


enum class OrderStatus {
    CART,
    ASSEMBLY,
    READY,
    GOT;
}

data class Order(
    val id: Int,
    val status: String,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("order_items")
    val orderItems: List<OrderItem>
)


data class CreateOrderRequest(
    @SerializedName("ids_order_items")
    val orderItemIds: List<Int>,
    @SerializedName("payment_method")
    val paymentMethod: String,
)

data class OrderCreated(
    val id: Int,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("date_created")
    val createdDate: String,
    val status: String,
    @SerializedName("is_paid")
    val isPaid: Boolean,
)


data class OrdersList(
    val items: List<Order>
)
