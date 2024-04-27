package com.app.techzone.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatPrice(price: Int): String {
    val dec = DecimalFormat("###,###,###,###,### ₽", DecimalFormatSymbols(Locale.ENGLISH))
    return dec.format(price).replace(",", " ")
}

/**
 * @param phoneNumber phone number containing something like `79221110033`
 * @return string representation of a phone number in specified format `+7 (922) 111-00-33`
 */
fun formatPhoneNumber(phoneNumber: String): String {
    return "+${phoneNumber.first()} (${phoneNumber.substring(1, 4)}) ${
        phoneNumber.substring(4, 7)
    }-${phoneNumber.substring(7, 9)}-${phoneNumber.substring(9, 11)}"
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

/**
 * Конвертация булевого значения в человекочитабельный текст ("Есть" и "Нет")
 */
fun boolToString(value: Boolean): String {
    return if (value) "Есть" else "Нет"
}