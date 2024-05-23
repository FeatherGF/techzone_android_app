package com.app.techzone.data.remote.model

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import com.google.gson.annotations.SerializedName

interface IReviewableProduct: IBaseProduct {
    val reviewId: Int?
}

data class ReviewableProduct(
    override val id: Int,
    override val name: String,
    override val price: Int,
    override val photos: List<Photo>?,
    @SerializedName("discount")
    override val discountPercentage: Int,
    @SerializedName("reviews_count")
    override val reviewsCount: Int,
    @SerializedName("average_rating")
    override val rating: Float?,
    @SerializedName("is_favourite")
    override val isFavorite: Boolean,
    @SerializedName("is_in_cart")
    override val isInCart: Boolean,
    @SerializedName("id_review")
    override val reviewId: Int?,
): IReviewableProduct

data class OrderItem(
    val id: Int,
    val product: ReviewableProduct,
    val quantity: Int,

    var mutableQuantity: MutableIntState = mutableIntStateOf(quantity)
)
