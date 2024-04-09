package com.app.techzone.model

data class Sorting (
    val text: String,
    val queryName: String, // price_desc, price_asc, popular etc
)

// queryName нужно согласовать с беком
val sortingOptions = listOf(
    Sorting("По популярности", queryName = "popular"),
    Sorting("Сначала недорогие", queryName = "price_asc"),
    Sorting("Сначала дорогие", queryName = "price_desc"),
    Sorting("По размеру скидки", queryName = "sale_amount"),
    Sorting("Высокий рейтинг", queryName = "high_rating"),
)
