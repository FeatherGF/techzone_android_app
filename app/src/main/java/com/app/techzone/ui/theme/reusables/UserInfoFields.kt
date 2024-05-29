package com.app.techzone.ui.theme.reusables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app.techzone.utils.MaskVisualTransformation

@Composable
fun UserInfoFields(
    // TODO: rewrite to AuthUIEvent
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    email: String,
    phoneFieldActions: KeyboardActions = KeyboardActions.Default
) {
    val phoneNumberVisualTransformation = MaskVisualTransformation("+# (###) ###-##-##")

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp)
            .background(MaterialTheme.colorScheme.tertiary),
        value = firstName,
        onValueChange = { onFirstNameChange(it) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        placeholder = {
            Text("Имя", color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f))
        },
        textStyle = MaterialTheme.typography.bodyLarge
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .background(MaterialTheme.colorScheme.tertiary),
        value = lastName,
        onValueChange = { onLastNameChange(it) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
        placeholder = {
            Text("Фамилия", color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f))
        },
        textStyle = MaterialTheme.typography.bodyLarge
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .background(MaterialTheme.colorScheme.tertiary),
        value = phoneNumber,
        onValueChange = { textPhone ->
            if (textPhone.length < 12) {
                onPhoneNumberChange(
                    textPhone.replaceFirstChar { if (it == '8') '7' else it }
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = if (phoneFieldActions != KeyboardActions.Default) ImeAction.Send else ImeAction.Next
        ),
        keyboardActions = phoneFieldActions,
        visualTransformation = phoneNumberVisualTransformation,
        placeholder = {
            Text("Телефон", color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f))
        },
        textStyle = MaterialTheme.typography.bodyLarge
    )
    OutlinedTextField(
        value = email,
        onValueChange = {},
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .background(MaterialTheme.colorScheme.tertiary),
    )
}