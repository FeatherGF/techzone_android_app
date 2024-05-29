package com.app.techzone.utils

import kotlin.math.roundToInt

fun calculateDiscount(initialPrice: Int, discountPercentage: Int): Int {
    return if (discountPercentage > 0)
        initialPrice - ((initialPrice * discountPercentage).toFloat() / 100).roundToInt()
    else
        initialPrice
}
