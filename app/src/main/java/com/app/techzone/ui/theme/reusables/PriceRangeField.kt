package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app.techzone.utils.priceMask

@Composable
fun PriceRangeField(
    modifier: Modifier = Modifier,
    placeholderText: String,
    text: String,
    onValueChange: (String) -> Unit,
) = OutlinedTextField(
    value = text,
    onValueChange = onValueChange,
    keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.NumberPassword
    ),
    textStyle = MaterialTheme.typography.bodyLarge,
    shape = RoundedCornerShape(4.dp),
    singleLine = true,
    visualTransformation = priceMask,
    placeholder = {
        Text(
            placeholderText,
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
        )
    },
    modifier = modifier
        .background(MaterialTheme.colorScheme.tertiary)
        .height(56.dp),
)