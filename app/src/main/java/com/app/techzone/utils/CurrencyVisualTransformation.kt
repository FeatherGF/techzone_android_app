package com.app.techzone.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.text.isDigitsOnly
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency

class CurrencyVisualTransformation(currencyCode: String) : VisualTransformation {
    private val numberFormatter = (NumberFormat.getCurrencyInstance() as DecimalFormat).apply {
        currency = Currency.getInstance(currencyCode)
        maximumFractionDigits = 0
        isGroupingUsed = true
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.trim()
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        if (originalText.isDigitsOnly().not()) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        val parsedNumber =
            originalText.toLongOrNull() ?: return TransformedText(text, OffsetMapping.Identity)
        val formattedText = numberFormatter.format(parsedNumber)
        val output = AnnotatedString(formattedText)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val beforeCursor = originalText.substring(0, offset)
                val beforeCursorFormatted = numberFormatter.format(beforeCursor.toLongOrNull() ?: 0)
                return beforeCursorFormatted.length - 2
            }

            override fun transformedToOriginal(offset: Int): Int {
                var nonDigitCount = 0
                var digitsEncountered = 0

                for (i in formattedText.indices) {
                    if (!formattedText[i].isDigit()) {
                        nonDigitCount++
                    }
                    if (digitsEncountered + nonDigitCount == offset) {
                        return digitsEncountered
                    }
                    if (formattedText[i].isDigit()) {
                        digitsEncountered++
                    }
                }

                return digitsEncountered
            }
        }

        return TransformedText(
            output,
            offsetMapping
        )
    }
}
