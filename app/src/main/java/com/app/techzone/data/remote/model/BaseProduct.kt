package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName


interface IBaseProduct {
    val id: Int
    val name: String
    val price: Int
    val photos: List<Photo>?
    val discountPercentage: Int
    val reviewsCount: Int
    val rating: Float?
    val isActive: Boolean
}

data class MemoryVariations(
    val memory: Int,
    @SerializedName("id_product") val productId: Int,
)

interface IDetailedProduct : IBaseProduct {
    val type: String
    val reviews: List<Review>
    val colorMain: String
    val colorVariations: List<ColorVariation>
    val memoryVariations: List<MemoryVariations>?
    val memory: Int?
}

data class ColorVariation(
    @SerializedName("color_main") val colorName: String,
    @SerializedName("color_hex") val colorHex: String,
    @SerializedName("id_product") val productId: Int,
)

data class BaseProduct(
    override val id: Int,
    override val name: String,
    override val price: Int,
    override val photos: List<Photo>?,
    @SerializedName("is_active") override val isActive: Boolean = true,
    @SerializedName("discount") override val discountPercentage: Int,
    @SerializedName("reviews_count") override val reviewsCount: Int,
    @SerializedName("average_rating") override val rating: Float?,
) : IBaseProduct


data class PagingBaseProduct(
    val pk: Int,  // unique identifier for LazyRow key
    override val id: Int,
    override val name: String,
    override val price: Int,
    override val photos: List<Photo>?,
    @SerializedName("is_active") override val isActive: Boolean = true,
    @SerializedName("discount") override val discountPercentage: Int,
    @SerializedName("reviews_count") override val reviewsCount: Int,
    @SerializedName("average_rating") override val rating: Float?,
): IBaseProduct

data class ProductType(
    val id: Int, val type: String
)