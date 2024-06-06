package com.app.techzone.ui.theme.purchase

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.browser.customtabs.CustomTabsIntent
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.LocalNavController
import com.app.techzone.LocalSnackbarHostState
import com.app.techzone.data.remote.model.OrderItem
import com.app.techzone.data.remote.model.PaymentRedirect
import com.app.techzone.data.remote.model.User
import com.app.techzone.model.PaymentType
import com.app.techzone.ui.theme.DarkText
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder100
import com.app.techzone.data.remote.model.totalDiscountPrice
import com.app.techzone.data.remote.model.totalPrice
import com.app.techzone.ui.theme.dimension
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.payment_selection.PaymentSelectionViewmodel
import com.app.techzone.ui.theme.payment_selection.PaymentTypes
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.UserViewModel
import com.app.techzone.ui.theme.reusables.OrderComposition
import com.app.techzone.ui.theme.reusables.UserInfoFields
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.ui.theme.server_response.UnauthorizedScreen
import com.app.techzone.utils.formatCommonCase
import com.app.techzone.utils.formatPrice
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PurchaseScreenRoot(
    userViewModel: UserViewModel,
    paymentViewModel: PaymentSelectionViewmodel,
    orderItemIds: List<Int>
) {
    LaunchedEffect(Unit) {
        userViewModel.loadCart()
        userViewModel.loadUser()
    }
    val cartItems by userViewModel.cartItems.collectAsStateWithLifecycle()
    val user by userViewModel.user.collectAsState()

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

        ServerResponse.SUCCESS -> {
            val orderItems = remember(orderItemIds) {
                cartItems.filter { it.id in orderItemIds }
            }
            user?.let {
                PurchaseScreen(
                    user = it,
                    orderItems = orderItems,
                    createOrder = userViewModel::createOrder,
                    paymentViewModel = paymentViewModel,
                    loadCart = userViewModel::loadCart
                )
            } ?: LoadingBox()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun PurchaseScreen(
    user: User,
    orderItems: List<OrderItem>,
    paymentViewModel: PaymentSelectionViewmodel,
    createOrder: suspend (List<Int>, String, Int) -> PaymentRedirect?,
    loadCart: () -> Unit,
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()

    var showOrderComposition by remember { mutableStateOf(false) }

    BackHandler(onBack = navController::popBackStack)
    val (firstName, onFirstNameChange) = remember {
        mutableStateOf(user.firstName ?: "")
    }
    val (lastName, onLastNameChange) = remember { mutableStateOf(user.lastName ?: "") }
    val (phoneNumber, onPhoneNumberChange) = remember {
        mutableStateOf(user.phoneNumber ?: "")
    }

//    val (paymentType, onPaymentTypeChange) = remember { mutableStateOf(defaultPaymentTypes.first()) }
    val totalDiscountPrice = orderItems.totalDiscountPrice()
    val totalPrice = orderItems.totalPrice()
    val profit = totalPrice - totalDiscountPrice

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
                    .padding(
                        start = MaterialTheme.dimension.large,
                        top = MaterialTheme.dimension.extraLarge,
                        bottom = MaterialTheme.dimension.extendedMedium,
                        end = MaterialTheme.dimension.large
                    ),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.extraLarge),
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
                    .padding(
                        horizontal = MaterialTheme.dimension.extendedMedium,
                        vertical = MaterialTheme.dimension.medium
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.extraSmall)
                ) {
                    Text(
                        formatCommonCase(orderItems.size, "товар"),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.scrim
                    )
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
                    modifier = Modifier.padding(end = MaterialTheme.dimension.medium),
                    onClick = { showOrderComposition = true }
                ) {
                    Icon(
                        modifier = Modifier.size(MaterialTheme.dimension.larger),
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                    )
                }
            }

            Column(
                Modifier.padding(
                    vertical = MaterialTheme.dimension.large,
                    horizontal = MaterialTheme.dimension.extendedMedium
                ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.large)
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
                        email = user.email
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.extendedMedium),
                    modifier = Modifier.padding(bottom = MaterialTheme.dimension.extraLarge)
                ) {
                    PurchaseStep(stepIndex = 2, stepTitle = "Способ оплаты")
                    PaymentTypes(
                        selectedType = paymentViewModel.selectedPayment,
                        onPaymentTypeChange = paymentViewModel::onPaymentSelected
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(MaterialTheme.dimension.huge),
            shadowElevation = 12.dp,
            color = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
            border = BorderStroke(width = 1.dp, color = ForStroke),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = MaterialTheme.dimension.extendedMedium,
                        vertical = MaterialTheme.dimension.small
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val tooltipState = rememberTooltipState()
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                    tooltip = {
                        RichTooltip(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = MaterialTheme.dimension.extendedMedium,
                                    end = MaterialTheme.dimension.extendedMedium,
                                    bottom = MaterialTheme.dimension.extraLarge
                                ),
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
                        val paymentMethod = when (paymentViewModel.selectedPayment.second) {
                            PaymentType.CASH -> {
                                PaymentType.CASH.name.lowercase()
                            }

                            PaymentType.CARD -> {
                                PaymentType.CARD.name.lowercase()
                            }

                            else -> return@Button
                        }
                        scope.launch {
                            val orderItemIds = orderItems.map { it.id }
                            val paymentResponse = createOrder(orderItemIds, paymentMethod, totalDiscountPrice)
                            if (paymentResponse == null) {
                                snackbarHostState.showSnackbar(
                                    "Что-то пошло не так\nПроверьте подключение к интернету"
                                )
                                return@launch
                            }
                            if (paymentResponse.url != null) {
                                // open in app browser for payment
                                val intent = CustomTabsIntent.Builder().build()
                                intent.launchUrl(context, Uri.parse(paymentResponse.url))
                                loadCart()
                                navController.navigate(ScreenRoutes.ORDERS)
                            } else {
                                tooltipState.show()
                                // redirect to another page after tooltip disappears
                            }
                        }
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(2000L)
                            navController.navigate(ScreenRoutes.ORDERS)
                            loadCart()
                        }
                    },
                    enabled = (
                        firstName.isNotBlank() &&
                        lastName.isNotBlank() &&
                        phoneNumber.isNotBlank() &&
                        paymentViewModel.selectedPayment.first.isNotBlank()
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
            )
        }
    }
}

@Composable
private fun PurchaseStep(stepIndex: Int, stepTitle: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.extendedMedium)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundBorder100
                )
                .size(MaterialTheme.dimension.larger),
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