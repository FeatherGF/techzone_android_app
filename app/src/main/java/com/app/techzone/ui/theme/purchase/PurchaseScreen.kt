package com.app.techzone.ui.theme.purchase

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.LocalNavController
import com.app.techzone.model.PaymentType
import com.app.techzone.ui.theme.DarkText
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder100
import com.app.techzone.data.remote.model.totalDiscountPrice
import com.app.techzone.data.remote.model.totalPrice
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.payment_selection.Card
import com.app.techzone.ui.theme.payment_selection.PaymentViewModel
import com.app.techzone.ui.theme.payment_selection.emptyPayment
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.UserViewModel
import com.app.techzone.ui.theme.reusables.OrderComposition
import com.app.techzone.ui.theme.reusables.UserInfoFields
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.ui.theme.server_response.UnauthorizedScreen
import com.app.techzone.utils.formatCommonCase
import com.app.techzone.utils.formatMaskedCard
import com.app.techzone.utils.formatPrice
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val defaultPaymentTypes = mutableListOf(
    "Оплата картой" to PaymentType.CARD,
    "Наличный расчет" to PaymentType.CASH
)

@Composable
fun PurchaseScreenRoot(
    userViewModel: UserViewModel,
    paymentViewModel: PaymentViewModel,
    orderItemIds: List<Int>
) {
    val cards by paymentViewModel.cards.collectAsStateWithLifecycle()
    PurchaseScreen(userViewModel, orderItemIds, storedCards = cards)
    when (userViewModel.state.response) {
        ServerResponse.LOADING -> {
            LoadingBox()
        }

        ServerResponse.ERROR -> {
            ErrorScreen(userViewModel::loadUser)
        }

        ServerResponse.UNAUTHORIZED -> {
            UnauthorizedScreen()
        }

        ServerResponse.SUCCESS -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun PurchaseScreen(
    userViewModel: UserViewModel,
    orderItemIds: List<Int>,
    storedCards: List<Card>
) {
    LaunchedEffect(userViewModel) {
        userViewModel.loadCart()
        userViewModel.loadUser()
    }
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val user by userViewModel.user.collectAsStateWithLifecycle()
    val cartItems by userViewModel.cartItems.collectAsStateWithLifecycle()
    val orderItems = cartItems
        .filter { it.id in orderItemIds }
        .map {
            it.apply {
                mutableQuantity = remember { mutableIntStateOf(it.quantity) }
            }
        }

    var showOrderComposition by remember { mutableStateOf(false) }

    BackHandler(onBack = navController::popBackStack)
    user?.let { currentUser ->
        // i have no idea why but this three variables just don't want to get the value
        // if it isn't in let block
        val (firstName, onFirstNameChange) = remember {
            mutableStateOf(
                currentUser.firstName ?: ""
            )
        }
        val (lastName, onLastNameChange) = remember { mutableStateOf(currentUser.lastName ?: "") }
        val (phoneNumber, onPhoneNumberChange) = remember {
            mutableStateOf(
                currentUser.phoneNumber ?: ""
            )
        }

        val paymentTypes = if (storedCards.isNotEmpty()) {
            storedCards.map { it.cardNumber to PaymentType.MASKED_CARD } + listOf(
                "Новая карта" to PaymentType.CARD,
                defaultPaymentTypes.last()
            )
        } else {
            defaultPaymentTypes
        }
        var paymentType: Pair<String, PaymentType> by remember {
            mutableStateOf(
                if (storedCards.isNotEmpty())
                    storedCards.first().cardNumber to PaymentType.MASKED_CARD
                else emptyPayment
            )
        }
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = ForStroke)
                        .background(color = MaterialTheme.colorScheme.tertiary)
                        .padding(start = 20.dp, top = 40.dp, bottom = 16.dp, end = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.Black,
                        )
                    }
                    Text(
                        "Оформление заказа",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                    )
                }

                // Строка с информацией о заказе
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.tertiary)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            formatCommonCase(orderItems.size, "товар"),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.scrim
                        )
                        val totalDiscountPrice = orderItems.totalDiscountPrice()
                        val totalPrice = orderItems.totalPrice()
                        val profit = totalPrice - totalDiscountPrice
                        if (profit > 0) {
                            Text(
                                "Скидка: ${formatPrice(profit)}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.scrim
                            )
                        }
                        Text(
                            "Итого: ${formatPrice(totalDiscountPrice)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                        )
                    }
                    IconButton(
                        modifier = Modifier.padding(end = 12.dp),
                        onClick = { showOrderComposition = true }
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = null,
                        )
                    }
                }

                Column(
                    Modifier.padding(vertical = 28.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    Column {
                        PurchaseStep(stepIndex = 1, stepTitle = "Данные покупателя")
                        UserInfoFields(
                            firstName = firstName,
                            onFirstNameChange = onFirstNameChange,
                            lastName = lastName,
                            onLastNameChange = onLastNameChange,
                            phoneNumber = phoneNumber,
                            onPhoneNumberChange = onPhoneNumberChange,
                            email = currentUser.email
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(bottom = 52.dp)
                    ) {
                        PurchaseStep(stepIndex = 2, stepTitle = "Способ оплаты")
                        Column(
                            Modifier
                                .selectableGroup()
                                .background(MaterialTheme.colorScheme.tertiary)
                                .border(
                                    width = 1.dp,
                                    color = ForStroke.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        ) {
                            paymentTypes.forEachIndexed { index, paymentTypePair ->
                                val (name, type) = paymentTypePair
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .selectable(
                                            selected = paymentType == paymentTypePair,
                                            onClick = { paymentType = paymentTypePair },
                                            role = Role.RadioButton
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        modifier = Modifier.padding(start = 34.dp, end = 34.dp),
                                        checked = paymentType == paymentTypePair,
                                        onCheckedChange = null
                                    )
                                    val text = if (type == PaymentType.MASKED_CARD) {
                                        formatMaskedCard(name)
                                    } else {
                                        name
                                    }
                                    Text(
                                        text,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                    )
                                }
                                // don't render divider after last, because border will do it
                                if (index != paymentTypes.size - 1) {
                                    HorizontalDivider(color = ForStroke.copy(alpha = 0.1f))
                                }
                            }
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(56.dp),
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                border = BorderStroke(width = 1.dp, color = ForStroke),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val tooltipState = rememberTooltipState()
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                        tooltip = {
                            RichTooltip(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 42.dp),
                                colors = TooltipDefaults.richTooltipColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    titleContentColor = DarkText,
                                    contentColor = DarkText
                                ),
                                title = {
                                    Text(
                                        "Заказ создан",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                },
                            ) {
                                Text(
                                    "Ваша заказ успешно создан \nи собирается на складе.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        state = tooltipState,
                        content = {}
                    )
                    Button(
                        onClick = {
                            val paymentMethod = when (paymentType.second) {
                                PaymentType.CASH -> {
                                    PaymentType.CASH.name.lowercase()
                                }

                                PaymentType.CARD -> {
                                    navController.navigate(ScreenRoutes.PAY_METHOD)
                                    return@Button
                                }

                                PaymentType.MASKED_CARD -> {
                                    PaymentType.CARD.name.lowercase()
                                }

                                PaymentType.NOT_SET -> {
                                    return@Button
                                }
                            }
                            scope.launch {
                                if (userViewModel.createOrder(orderItemIds, paymentMethod)) {
                                    tooltipState.show()
                                }
                            }

                            // FIXME: do it more elegant way than this hack
                            // redirect to another page after tooltip disappears
                            GlobalScope.launch(Dispatchers.Main) {
                                delay(2000L)
                                navController.navigate(ScreenRoutes.ORDERS)
                                userViewModel.loadCart()
                            }
                        },
                        enabled = (
                                firstName.isNotBlank() &&
                                        lastName.isNotBlank() &&
                                        phoneNumber.isNotBlank() &&
                                        paymentType.first.isNotBlank()
                                ),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary,
                            disabledContentColor = Color(29, 27, 32, 38)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Оформить заказ",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
            if (showOrderComposition) {
                OrderComposition(
                    orderItems = orderItems,
                    onDismiss = { showOrderComposition = false },
                    onProductCheckStatus = userViewModel::onCheckProduct,
                    onProductAction = userViewModel::onProductAction
                )
            }
        }
    }
}

@Composable
private fun PurchaseStep(stepIndex: Int, stepTitle: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundBorder100
                )
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                stepIndex.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            stepTitle,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
        )
    }
}