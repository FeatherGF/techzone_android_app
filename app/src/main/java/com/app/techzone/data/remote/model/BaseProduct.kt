package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName


enum class ProductTypeEnum {
    ACCESSORY,
    TABLET,
    LAPTOP,
    SMARTPHONE,
    SMARTWATCH,
    TELEVISION;
}

fun getProductType(type: ProductType): ProductTypeEnum {
    return ProductTypeEnum.valueOf(type.type.uppercase())
}

fun getProductType(type: String): ProductTypeEnum{
    return ProductTypeEnum.valueOf(type.uppercase())
}

interface IBaseProduct{
    val id: Int
    val name: String
    val price: Int
    val photos: List<Photo>
    val discountPercentage: Int
    val reviewsCount: Int
    val rating: Float?
    val isFavorite: Boolean
    val isInCart: Boolean
}

interface IDetailedProduct: IBaseProduct{
    val type: String
    val reviews: List<Review>
    val colorMain: String
    val colorVariations: List<ColorVariation>
    val memoryVariations: List<Int>?
    val memory: Int?
}

data class ColorVariation(
    @SerializedName("color")
    val colorName: String,
    @SerializedName("hex")
    val colorHex: String
)

data class BaseProduct(
    override val id: Int,
    override val name: String,
    override val price: Int,
    override val photos: List<Photo>,
    @SerializedName("discount")
    override val discountPercentage: Int,
    @SerializedName("reviews_count")
    override val reviewsCount: Int,
    @SerializedName("average_rating")
    override val rating: Float?,
    @SerializedName("is_favourite")
    override val isFavorite: Boolean,
    @SerializedName("is_in_cart")
    override val isInCart: Boolean
): IBaseProduct


data class ProductType(
    val id: Int,
    val type: String
)