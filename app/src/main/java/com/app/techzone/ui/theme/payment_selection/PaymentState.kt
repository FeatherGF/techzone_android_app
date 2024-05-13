package com.app.techzone.ui.theme.payment_selection

import android.icu.text.SimpleDateFormat
import java.util.Locale

enum class PaymentScreens {
    CHOOSE_PAYMENT,
    ENTER_CARD;
}

data class Card(
    val cardNumber: String,
    val expirationDate: String,  // month and year
    val code: String,
)

data class PaymentState(
    val screen: PaymentScreens = PaymentScreens.CHOOSE_PAYMENT,
    val cardNumber: String = "",
    val expirationDate: String = "",  // month and year
    val cardExpiredText: String? = null,
    val code: String = "",
)
