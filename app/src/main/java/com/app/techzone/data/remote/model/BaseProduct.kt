package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName


enum class ProductTypeEnum {
    ACCESSORY,
    TABLET,
    LAPTOP,
    SMARTPHONE,
    SMARTWATCH,
    TELEVISION,
    PRODUCT;

    companion object {
        object Localized {
            private val ACCESSORY = "аксессуар" to ProductTypeEnum.ACCESSORY
            private val TABLET = "планшет" to ProductTypeEnum.TABLET
            private val LAPTOP = "ноутбук" to ProductTypeEnum.LAPTOP
            private val SMARTPHONE = "смартфон" to ProductTypeEnum.SMARTPHONE
            private val SMARTWATCH = "часы" to ProductTypeEnum.SMARTWATCH
            private val TELEVISION = "телевизор" to ProductTypeEnum.TELEVISION

            private val localizedEntries = listOf(
                ACCESSORY,
                TABLET,
                LAPTOP,
                SMARTPHONE,
                SMARTWATCH,
                TELEVISION,
            )

            fun localizedValueOf(value: String): ProductTypeEnum? {
                localizedEntries.forEach { pair ->
                    val (name, type) = pair
                    if (name == value) return type
                }
                return null
            }
        }
    }
}

fun getProductType(type: ProductType, default: ProductTypeEnum = ProductTypeEnum.PRODUCT): ProductTypeEnum {
    return try {
        ProductTypeEnum.valueOf(type.type.uppercase())
    } catch (e: IllegalArgumentException) {
        default
    }
}

fun getProductType(type: String, default: ProductTypeEnum = ProductTypeEnum.PRODUCT): ProductTypeEnum {
    return try {
        ProductTypeEnum.valueOf(type.uppercase())
    } catch (e: IllegalArgumentException) {
        ProductTypeEnum.Companion.Localized.localizedValueOf(type) ?: default
    }
}

interface IBaseProduct{
    val id: Int
    val name: String
    val price: Int
    val photos: List<Photo>?
    val discountPercentage: Int
    val reviewsCount: Int
    val rating: Float?
    val isFavorite: Boolean
    val isInCart: Boolean
}

private typealias MemoryCapacity = String
private typealias ProductId = Int
typealias MemoryVariations = Map<MemoryCapacity, ProductId>?

interface IDetailedProduct: IBaseProduct{
    val type: String
    val reviews: List<Review>
    val colorMain: String
    val colorVariations: List<ColorVariation>
    val memoryVariations: MemoryVariations
    val memory: Int?
}

data class ColorVariation(
    @SerializedName("color")
    val colorName: String,
    @SerializedName("hex")
    val colorHex: String,
    @SerializedName("id_product")
    val productId: Int,
)

data class BaseProduct(
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
    override val isInCart: Boolean
): IBaseProduct


data class ProductType(
    val id: Int,
    val type: String
)