package com.app.techzone.ui.theme.payment_selection


sealed class PaymentUiEvent {
    data object ChoosePayment: PaymentUiEvent()
    data object EnterCard: PaymentUiEvent()
    data class CardNumberChanged(val value: String): PaymentUiEvent()
    data class ExpirationDateChanged(val value: String): PaymentUiEvent()
    data class CodeChanged(val value: String): PaymentUiEvent()
    data object CheckCard: PaymentUiEvent()
    data class DeleteCard(val cardNumber: String): PaymentUiEvent()
}