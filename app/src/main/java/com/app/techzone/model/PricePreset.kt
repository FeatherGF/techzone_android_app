package com.app.techzone.model

import com.app.techzone.ui.theme.catalog.MAX_PRICE
import com.app.techzone.ui.theme.catalog.MIN_PRICE

data class PricePreset (
    val text: String,
    val minPrice: Int,
    val maxPrice: Int,
)


val pricePresets = listOf(
    PricePreset("Менее 15 000 ₽", MIN_PRICE, 14_999),
    PricePreset("15 000 - 24 999 ₽", 15_000, 24_999),
    PricePreset("25 000 - 39 999 ₽", 25_000, 39_999),
    PricePreset("40 000 - 59 999 ₽", 40_000, 59_999),
    PricePreset("60 000 - 99 999 ₽", 60_000, 99_999),
    PricePreset("Более 99 999 ₽", 100_000, MAX_PRICE),
)
