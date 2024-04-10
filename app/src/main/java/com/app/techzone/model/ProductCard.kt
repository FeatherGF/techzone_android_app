package com.app.techzone.model

import com.app.techzone.R

data class ProductCard(
    val imageId: Int,
    val title: String,
    val price: Int,
    val crossedPrice: Int? = null,
    val reviewCount: Int? = null,
    val rating: Float? = null,
    val isInCart: Boolean = false,
    val isFavorite: Boolean = false,
)


val newProducts = listOf(
    ProductCard(
        R.drawable.ic_iphone,
        "Смартфон Apple iPhone 15 Pro 256GB Blue Titanium",
        123999,
        154999,
        191,
        4.75f,
    ),
    ProductCard(
        R.drawable.ic_tv,
        "Телевизор Samsung Ultra HD (4K) LED 55",
        77399,
        85999,
        25,
        5.0f,
    ),
    ProductCard(
        R.drawable.ic_macbook,
        "Ноутбук Apple MacBook Air 13 M1/8/256GB Silver (MGN93)",
        95999,
        reviewCount = 114,
        rating = 4.8f,
    ),
    ProductCard(
        R.drawable.ic_gaming_laptop,
        "Ноутбук игровой MSI Katana 17 B11UCX-882XRU",
        89999,
        reviewCount = 22,
        rating = 5.0f
    ),
)

val bestSellerProducts = listOf(
    ProductCard(
        R.drawable.ic_sale,
        "Телевизор Haier 55 Smart TV AX Pro",
        price = 64_999,
        rating = 4.9f,
        reviewCount = 13,
    ),
    ProductCard(
        R.drawable.ic_sale_2,
        "Планшет Apple iPad 10.2 Wi-Fi 64GB Space Grey (MK2K3)",
        price = 37_999,
        crossedPrice = 39_999,
        rating = 4.9f,
        reviewCount = 156,
    ),
    ProductCard(
        R.drawable.ic_sale_3,
        "Смартфон Samsung Galaxy A54 128GB Awesome Graphite",
        price = 34_999,
        crossedPrice = 45_999,
        rating = 4.8f,
        reviewCount = 73,
    ),
    ProductCard(
        R.drawable.ic_sale_4,
        "Смарт-часы Xiaomi M2216W1 Ivory",
        price = 7_999,
        crossedPrice = 9_999,
        rating = 4.75f,
        reviewCount = 25,
    ),
)
