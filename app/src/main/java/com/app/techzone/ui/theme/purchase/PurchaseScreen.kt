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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.app.techzone.data.remote.model.validateUserInfo
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder100
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.UnauthorizedScreen
import com.app.techzone.ui.theme.profile.UserInfoFields
import com.app.techzone.ui.theme.profile.auth.UserViewModel
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.utils.formatMaskedCard
import kotlinx.coroutines.launch

@Composable
fun PurchaseScreenRoot(
    userViewModel: UserViewModel,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    PurchaseScreen(userViewModel, snackbarHostState) {
        navController.popBackStack()
    }
    when (userViewModel.state.response) {
        ServerResponse.LOADING -> { LoadingBox() }
        ServerResponse.ERROR -> { ErrorScreen(userViewModel::loadUser) }
        ServerResponse.UNAUTHORIZED -> {
            UnauthorizedScreen {
                navController.navigate(ScreenRoutes.PROFILE_REGISTRATION)
            }
        }
        ServerResponse.SUCCESS -> { }
    }
}

enum class PaymentType{
    CASH,
    CARD,
    MASKED_CARD,
    NOT_SET
}

val defaultPaymentTypes = mutableListOf(
    "Оплата картой" to PaymentType.CARD,
    "Наличный расчет" to PaymentType.CASH
)

@Composable
fun PurchaseScreen(
    userViewModel: UserViewModel,
    snackbarHostState: SnackbarHostState,
    onBackClicked: () -> Unit
) {
    LaunchedEffect(userViewModel) { userViewModel.loadUser() }
    BackHandler(onBack = onBackClicked)
    val scope = rememberCoroutineScope()

    val user by userViewModel.user.collectAsStateWithLifecycle()

    user?.let{ currentUser ->
        // i have no idea why but this three variables just don't want to get the value
        // if it isn't in let block
        val (firstName, onFirstNameChange) = remember { mutableStateOf(currentUser.firstName ?: "") }
        val (lastName, onLastNameChange) = remember { mutableStateOf(currentUser.lastName ?: "") }
        val (phoneNumber, onPhoneNumberChange) = remember { mutableStateOf(currentUser.phoneNumber ?: "") }

        var paymentType by remember { mutableStateOf(PaymentType.NOT_SET) }
        val paymentTypes = defaultPaymentTypes

        /*
            val paymentTypes = if (user?.connectedCards.isNotEmpty()){
                connectedCards.map { currentUser to PaymentType.MASKED_CARD } + listOf("Новая карта" to PaymentType.CARD, defaultPaymentTypes.last())
            } else {
                defaultPaymentTypes
            }
         */
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())){
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.tertiary)
                        .padding(start = 20.dp, top = 40.dp, bottom = 16.dp, end = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(48.dp),
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
                    ){
                        // TODO: get from backend after its release
                        Text("3 товара")
                        Text("Скидка: ...")
                        Text("Итого ...")

                    }
                    IconButton(
                        modifier = Modifier.padding(end = 12.dp),
                        onClick = {
                            // TODO: скорее всего показать наполнение заказа. Уточнить у Ксюши
                        }
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
                            onPhoneUmberChange = onPhoneNumberChange,
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
                                            selected = paymentType == type,
                                            onClick = { paymentType = type },
                                            role = Role.RadioButton
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        modifier = Modifier.padding(start = 34.dp, end = 34.dp),
                                        checked = paymentType == type,
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
                    Button(
                        onClick = {
                            if (firstName != currentUser.firstName || lastName != currentUser.lastName || phoneNumber != currentUser.phoneNumber) {
                                val (isValid, reason) = validateUserInfo(firstName, lastName, phoneNumber)
                                if (!isValid){
                                    scope.launch {
                                        snackbarHostState.showSnackbar(reason)
                                    }
                                    return@Button
                                }
                                userViewModel.updateUser(firstName, lastName, phoneNumber)
                            }

                            if (paymentType == PaymentType.CARD) {
                                // TODO: navigate to enter new card composable
                            }

                            /*TODO userViewModel.createOrder(<list of order item ids>)*/
                        },
                        enabled = (
                            firstName.isNotBlank() &&
                            lastName.isNotBlank() &&
                            phoneNumber.isNotBlank() &&
                            paymentType != PaymentType.NOT_SET
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
        }
    }
}

@Composable
fun PurchaseStep(stepIndex: Int, stepTitle: String) {
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
        ){
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