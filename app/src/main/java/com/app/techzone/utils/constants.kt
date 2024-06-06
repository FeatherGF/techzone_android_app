package com.app.techzone.utils

import com.app.techzone.model.PaymentType

const val DEFAULT_MAX_PRICE = 250_000
const val DEFAULT_MIN_PRICE = 5_000
val emptyPayment = Pair("", PaymentType.NOT_SET)
val defaultPaymentTypes = mutableListOf(
    "Наличный расчет" to PaymentType.CASH,
    "Оплата картой" to PaymentType.CARD,
)