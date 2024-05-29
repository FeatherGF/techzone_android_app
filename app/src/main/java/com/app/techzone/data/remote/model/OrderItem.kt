package com.app.techzone.data.remote.model

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import com.app.techzone.utils.calculateDiscount
import com.google.gson.annotations.SerializedName

interface IOrderItemProduct : IBaseProduct {
    val quantity: Int
    val reviewId: Int?
}

data class OrderItemProduct(
    override val id: Int,
    override val name: String,
    override val price: Int,
    override val photos: List<Photo>?,
    override val quantity: Int,
    @SerializedName("discount") override val discountPercentage: Int,
    @SerializedName("reviews_count") override val reviewsCount: Int,
    @SerializedName("average_rating") override val rating: Float?,
    @SerializedName("id_review") override val reviewId: Int?,
) : IOrderItemProduct

data class OrderItem(
    val id: Int, val product: OrderItemProduct, val quantity: Int,

    var mutableQuantity: MutableIntState = mutableIntStateOf(quantity)
)

fun List<OrderItem>.totalPrice(): Int {
    if (isEmpty()) return 0
    return this.map { it.product.price * it.mutableQuantity.intValue }.reduce { acc, i -> acc + i }
}

fun List<OrderItem>.totalDiscountPrice(): Int {
    if (isEmpty()) return 0
    return this
        .associate {
            (it.product.price to it.product.discountPercentage) to it.mutableQuantity.intValue
        }.map { (pair, quantity) ->
            calculateDiscount(
                initialPrice = pair.first, discountPercentage = pair.second
            ) * quantity
        }.reduce { acc, i -> acc + i }
}