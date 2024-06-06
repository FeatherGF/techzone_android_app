package com.app.techzone.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.util.Date
import java.util.Locale

fun formatPrice(price: Int): String {
    val dec = DecimalFormat("###,###,###,###,### ₽", DecimalFormatSymbols(Locale.ENGLISH))
    return dec.format(price).replace(',', ' ')
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

/**
 * @param quantity количество для которого нужно поставить слово в падеж
 * @param word слово, которое нужно поставить в падеж
 * @return строка с количеством и словом в нужном падеже
 * @sample formatCommonCaseSample
 */
fun formatCommonCase(quantity: Int, word: String): String {
    when (quantity.mod(100)) {
        11, 12, 13, 14 -> {
            return "$quantity ${word}ов"
        }
    }
    val endCase: String = when (quantity.mod(10)) {
        1 -> { "" }
        2, 3, 4 -> { "а" }
        else -> { "ов" }
    }
    return "$quantity $word$endCase"
}

private fun formatCommonCaseSample() {
    println(formatCommonCase(120, "отзыв"))  // "120 отзывов"
    println(formatCommonCase(243, "отзыв"))  // "243 отзыва"
    println(formatCommonCase(111, "отзыв"))  // "111 отзывов"
    println(formatCommonCase(401, "отзыв"))  // "401 отзыв"
}

fun formatDateShort(s: String): String {
    val date = getDateObject(s) ?: return ""
    val dateFormatter = java.text.SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    return dateFormatter.format(date)
}

fun getDateObject(s: String): Date? {
    val dateParser = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ru"))
    return try {
        dateParser.parse(s)
    } catch (e: ParseException){
        null
    }
}

fun formatDateLong(s: String): String {
    val date = getDateObject(s) ?: return ""
    val dateFormatter = java.text.SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    return dateFormatter.format(date)
}

/**
 * Конвертация булевого значения в человекочитабельный текст ("Есть" и "Нет")
 */
fun boolToString(value: Boolean): String {
    return if (value) "Есть" else "Нет"
}