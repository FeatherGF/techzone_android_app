package com.app.techzone.model


enum class SortingOptions {
    POPULAR,
    PRICE_ASC,
    PRICE_DESC,
    DISCOUNT_DESC,
    RATING_DESC;
}

data class Sorting (
    val text: String,
    val queryName: SortingOptions,
)

val sortingOptions = listOf(
    Sorting("По популярности", queryName = SortingOptions.POPULAR),
    Sorting("Сначала недорогие", queryName = SortingOptions.PRICE_ASC),
    Sorting("Сначала дорогие", queryName = SortingOptions.PRICE_DESC),
    Sorting("По размеру скидки", queryName = SortingOptions.DISCOUNT_DESC),
    Sorting("Высокий рейтинг", queryName = SortingOptions.RATING_DESC),
)
