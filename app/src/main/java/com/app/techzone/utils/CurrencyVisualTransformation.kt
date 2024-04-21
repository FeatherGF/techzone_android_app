package com.app.techzone.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.text.isDigitsOnly
import java.text.NumberFormat
import java.util.Currency

class CurrencyVisualTransformation(currencyCode: String) : VisualTransformation {
    private val numberFormatter = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance(currencyCode)
        maximumFractionDigits = 0
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.trim()
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        if (originalText.isDigitsOnly().not()) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        val formattedText = numberFormatter.format(originalText.toInt())
        return TransformedText(
            AnnotatedString(formattedText),
            OffsetMapping.Identity
        )
    }
}