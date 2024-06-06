package com.app.techzone.ui.theme.payment_selection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.app.techzone.model.PaymentType
import com.app.techzone.utils.defaultPaymentTypes
import com.app.techzone.utils.emptyPayment


class PaymentSelectionViewmodel: ViewModel() {
    var selectedPayment by mutableStateOf(defaultPaymentTypes.first())
        private set

    fun onPaymentSelected(payment: Pair<String, PaymentType>) {
        selectedPayment = if (payment == selectedPayment) emptyPayment else payment
    }
}