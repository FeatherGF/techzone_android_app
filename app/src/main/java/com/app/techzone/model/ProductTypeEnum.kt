package com.app.techzone.model

import com.app.techzone.data.remote.model.ProductType

enum class ProductTypeEnum {
    ACCESSORY, TABLET, LAPTOP, SMARTPHONE, SMARTWATCH, TELEVISION, PRODUCT;

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

fun getProductType(
    type: ProductType, default: ProductTypeEnum = ProductTypeEnum.PRODUCT
): ProductTypeEnum {
    return try {
        ProductTypeEnum.valueOf(type.type.uppercase())
    } catch (e: IllegalArgumentException) {
        default
    }
}

fun getProductType(
    type: String, default: ProductTypeEnum = ProductTypeEnum.PRODUCT
): ProductTypeEnum {
    return try {
        ProductTypeEnum.valueOf(type.uppercase())
    } catch (e: IllegalArgumentException) {
        ProductTypeEnum.Companion.Localized.localizedValueOf(type) ?: default
    }
}