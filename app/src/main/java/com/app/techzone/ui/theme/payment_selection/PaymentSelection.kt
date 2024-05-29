package com.app.techzone.ui.theme.payment_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.LocalNavController
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.model.PaymentType
import com.app.techzone.ui.theme.reusables.ConfirmationModalSheet
import com.app.techzone.ui.theme.reusables.PlaceholderText
import com.app.techzone.utils.MaskVisualTransformation
import com.app.techzone.utils.formatMaskedCard

val emptyPayment = Pair("", PaymentType.NOT_SET)
val paymentTypes = listOf(
    "Наличный расчет" to PaymentType.CASH
)

@Composable
fun PaymentSelectionRoot(paymentViewModel: PaymentViewModel) {
    val cards by paymentViewModel.cards.collectAsStateWithLifecycle()
    when (paymentViewModel.state.screen) {
        PaymentScreens.CHOOSE_PAYMENT -> {
            PaymentSelection(
                onEvent = paymentViewModel::onEvent,
                savedCards = cards
            )
        }

        PaymentScreens.ENTER_CARD -> {
            EnterCardInfo(state = paymentViewModel.state, onEvent = paymentViewModel::onEvent)
        }
    }
}

@Composable
fun PaymentSelection(onEvent: (PaymentUiEvent) -> Unit, savedCards: List<Card>) {
    var selectedPaymentMethod by remember {
        mutableStateOf(
            if (savedCards.isNotEmpty()) savedCards.first().cardNumber to PaymentType.MASKED_CARD
            else emptyPayment
        )
    }
    var cardToDelete: String? by remember { mutableStateOf(null) }
    val navController = LocalNavController.current
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBar("Способ оплаты") { navController.popBackStack() }
        Column(
            Modifier
                .padding(horizontal = 16.dp, vertical = 28.dp)
                .border(width = 1.dp, color = ForStroke.copy(alpha = 0.1f))
                .background(MaterialTheme.colorScheme.tertiary)
                .selectableGroup()
        ) {
            savedCards.forEach { card: Card ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(start = 34.dp, end = 24.dp)
                        .selectable(
                            selected = selectedPaymentMethod.first == card.cardNumber,
                            onClick = {
                                selectedPaymentMethod =
                                    if (selectedPaymentMethod.first == card.cardNumber) {
                                        emptyPayment
                                    } else {
                                        card.cardNumber to PaymentType.MASKED_CARD
                                    }
                            },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Checkbox(
                            modifier = Modifier.padding(end = 34.dp),
                            checked = selectedPaymentMethod.first == card.cardNumber,
                            onCheckedChange = null
                        )
                        Text(
                            formatMaskedCard(card.cardNumber),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                        )
                    }
                    IconButton(onClick = { cardToDelete = card.cardNumber }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                    }
                }
                HorizontalDivider(color = ForStroke.copy(alpha = 0.1f))
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = selectedPaymentMethod == paymentTypes[0],
                        onClick = {
                            selectedPaymentMethod = if (selectedPaymentMethod == paymentTypes[0]) {
                                emptyPayment
                            } else paymentTypes[0]
                        },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.padding(start = 34.dp, end = 34.dp),
                    checked = selectedPaymentMethod == paymentTypes[0],
                    onCheckedChange = null
                )
                Text(
                    paymentTypes[0].first,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
            }
            HorizontalDivider(color = ForStroke.copy(alpha = 0.1f))
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable {
                        onEvent(PaymentUiEvent.EnterCard)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCard,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 34.dp, end = 34.dp)
                )
                Text(
                    "Привязать карту",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
            }
        }
        cardToDelete?.let { cardNumber ->
            // make nu
            val shortMaskedCardNumber = cardNumber.takeLast(6).replaceRange(0..1, "**")
            ConfirmationModalSheet(
                confirmationText = "Вы действительно хотите удалить карту $shortMaskedCardNumber из профиля? Отменить это дейстиве будет невозможно.",
                onConfirm = {
                    onEvent(PaymentUiEvent.DeleteCard(cardNumber))
                    cardToDelete = null
                },
                onDismiss = { cardToDelete = null }
            )
        }
    }
}


@Composable
fun EnterCardInfo(
    state: PaymentState,
    onEvent: (PaymentUiEvent) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBar("Ввод карты") { onEvent(PaymentUiEvent.ChoosePayment) }
        Column(
            Modifier
                .padding(horizontal = 16.dp, vertical = 28.dp),
        ) {
            val cardNumberVisualTransformation = MaskVisualTransformation("#### #### #### ####")
            val expirationDateVisualTransformation = MaskVisualTransformation("##/##")
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary),
                value = state.cardNumber,
                placeholder = { PlaceholderText("Номер карты") },
                onValueChange = {
                    if (it.length < 17 && it.isDigitsOnly())
                        onEvent(PaymentUiEvent.CardNumberChanged(it))
                },
                visualTransformation = cardNumberVisualTransformation,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 20.dp)
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val colors = OutlinedTextFieldDefaults.colors(
                    errorContainerColor = MaterialTheme.colorScheme.background,
                    focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.tertiary
                )
                OutlinedTextField(
                    value = state.expirationDate,
                    placeholder = { PlaceholderText("ММ/ГГ") },
                    onValueChange = {
                        if (it.length < 5 && it.isDigitsOnly())
                            onEvent(PaymentUiEvent.ExpirationDateChanged(it))
                    },
                    visualTransformation = expirationDateVisualTransformation,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    isError = state.cardExpiredText != null,
                    supportingText = {
                        state.cardExpiredText?.let {
                            Text(
                                it,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.background(MaterialTheme.colorScheme.background)
                            )
                        }
                    },
                    colors = colors,
                    modifier = Modifier.fillMaxWidth(0.49f)
                )
                OutlinedTextField(
                    value = state.code,
                    placeholder = { PlaceholderText("CVV/CVC") },
                    onValueChange = {
                        if (it.length < 4 && it.isDigitsOnly())
                            onEvent(PaymentUiEvent.CodeChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { onEvent(PaymentUiEvent.CheckCard) }
                    ),
                    supportingText = {
                        Text(
                            "3 цифры на обороте",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        )
                    },
                    colors = colors,
                    modifier = Modifier
                        .fillMaxWidth(0.51f)
                        .weight(1f)
                )
            }
            Button(
                onClick = { onEvent(PaymentUiEvent.CheckCard) },
                enabled = (
                        state.cardNumber.isNotEmpty()
                                && state.expirationDate.isNotEmpty()
                                && state.code.isNotEmpty()
                        ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Привязать карту", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun TopBar(text: String, onBackClicked: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = ForStroke)
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.Black,
            )
        }
        Text(
            text,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = Color.Transparent
        )
    }
}
