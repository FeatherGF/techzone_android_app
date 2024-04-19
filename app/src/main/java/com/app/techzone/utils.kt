package com.app.techzone

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.roundToInt

fun formatPrice(price: Int): String {
    val dec = DecimalFormat("###,###,###,###,### ₽", DecimalFormatSymbols(Locale.ENGLISH))
    return dec.format(price).replace(",", " ")
}

fun calculateDiscount(initialPrice: Int, discountPercentage: Int): Int {
    return if (discountPercentage > 0)
        initialPrice - ((initialPrice * discountPercentage).toFloat() / 100).roundToInt()
    else
        initialPrice
}

fun formatReview(reviewCount: Int): String {
    when (reviewCount.mod(100)) {
        11, 12, 13, 14 -> { return "$reviewCount отзывов"}
    }

    val review: String = when (reviewCount.mod(10)) {
        1 -> {"отзыв"}
        2, 3, 4 -> {"отзыва"}
        else -> {"отзывов"}
    }
    return "$reviewCount $review"
}