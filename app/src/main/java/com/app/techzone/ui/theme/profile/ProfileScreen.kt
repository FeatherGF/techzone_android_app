package com.app.techzone.ui.theme.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.data.remote.model.validateUserInfo
import com.app.techzone.utils.formatPhoneNumber
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.auth.UserViewModel
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.utils.MaskVisualTransformation
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(
    authResultState: AuthResult<Unit>,
    navController: NavController,
    userViewModel: UserViewModel
) {
    when (authResultState) {
        is AuthResult.Authorized -> {
            UserProfile(userViewModel, navController = navController)
        }
        is AuthResult.Unauthorized -> {
            UnauthorizedScreen {
                navController.navigate(ScreenRoutes.PROFILE_REGISTRATION) {
                    popUpTo(ScreenRoutes.PROFILE_REGISTRATION)
                }
            }
        }
        is AuthResult.UnknownError -> { ErrorScreen(userViewModel::loadUser) }
        else -> {}
    }
}


@Composable
fun EditUserProfile(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    userViewModel: UserViewModel,
    onBackClicked: () -> Unit,
) {
    LaunchedEffect(userViewModel) { userViewModel.loadUser() }
    BackHandler(onBack = onBackClicked)

    val user by userViewModel.user.collectAsStateWithLifecycle()

    val (firstName, onFirstNameChange) = remember { mutableStateOf(user?.firstName ?: "") }
    val (lastName, onLastNameChange) = remember { mutableStateOf(user?.lastName ?: "") }
    val (phoneNumber, onPhoneNumberChange) = remember { mutableStateOf(user?.phoneNumber ?: "") }

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    fun saveUserChanges() {
        val (isValid, reason) = validateUserInfo(firstName, lastName, phoneNumber)
        if (!isValid){
            coroutineScope.launch {
                snackbarHostState.showSnackbar(reason)
            }
            return
        }
        userViewModel.updateUser(
            firstName = firstName.takeIf { it.isNotBlank() } ?: "",
            lastName = lastName.takeIf { it.isNotBlank() } ?: "",
            phoneNumber = phoneNumber.takeIf { it.isNotBlank() } ?: ""
        )
        navController.navigate(ScreenRoutes.PROFILE) {
            popUpTo(ScreenRoutes.PROFILE)
        }
    }

    // separate column needed in order to apply .weight(1f) and place `save` button at the bottom
    Column {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .weight(1f)
        ) {
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
                    "Изменить профиль",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 28.dp, end = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = Color(0xFF272727).copy(alpha = 0.3f)
                    )
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }

                UserInfoFields(
                    firstName = firstName,
                    onFirstNameChange = onFirstNameChange,
                    lastName = lastName,
                    onLastNameChange = onLastNameChange,
                    phoneNumber = phoneNumber,
                    onPhoneUmberChange = onPhoneNumberChange,
                    email = user?.email!!,
                    phoneFieldActions = KeyboardActions(
                        onSend = {
                            saveUserChanges()
                            // if text fields contained errors, hide keyboard to make snackbar visible
                            keyboardController?.hide()
                        }
                    )
                )

                var isBottomSheetDeleteUserShown by remember { mutableStateOf(false) }
                OutlinedButton(
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = { isBottomSheetDeleteUserShown = true },
                    border = null,
                    contentPadding = PaddingValues(start = 12.dp, end = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onError,
                            modifier = Modifier
                                .size(26.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            "Удалить профиль",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                }
                if (isBottomSheetDeleteUserShown) {
                    ConfirmationModalSheet(
                        confirmationText = "Вы действительно хотите удалить свой профиль? " +
                                "Отменить это действие будет невозможно.",
                        onConfirm = { userViewModel.deleteUser() },
                        onDismiss = { isBottomSheetDeleteUserShown = false}
                    )
                }
            }
        }
        Surface(
            Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = ForStroke.copy(alpha = 0.1f))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentColor = MaterialTheme.colorScheme.tertiary,
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { saveUserChanges() }
            ) {
                Text(
                    "Сохранить изменения",
                    color = Color.Companion.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun UserInfoFields(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneUmberChange: (String) -> Unit,
    email: String,
    phoneFieldActions: KeyboardActions = KeyboardActions.Default
) {
    val phoneNumberVisualTransformation = MaskVisualTransformation("+# (###) ###-##-##")

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp),
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
            .padding(top = 12.dp),
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
            .padding(top = 12.dp),
        value = phoneNumber,
        onValueChange = { textPhone ->
            if (textPhone.length < 12)
                onPhoneUmberChange(textPhone.filter { it.isDigit() })
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
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
            .padding(top = 12.dp),
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationModalSheet(
    confirmationText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 23.dp, end = 23.dp, bottom = 23.dp),
        ) {
            Text(
                confirmationText,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp)
                    .height(40.dp),
                onClick = onConfirm
            ) {
                Text(
                    "Удалить",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    "Отменить",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserProfile(
    userViewModel: UserViewModel,
    navController: NavController,
) {
    LaunchedEffect(userViewModel) { userViewModel.loadUser() }
    val user by userViewModel.user.collectAsStateWithLifecycle()
    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.tertiary)
                .padding(top = 40.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Профиль",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
            )
        }
        val verticalSpacing =
            if (user?.firstName == null && user?.lastName == null && user?.phoneNumber == null)
                12.dp
            else 24.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = MaterialTheme.colorScheme.tertiary)
                .border(width = 1.dp, color = ForStroke.copy(alpha = 0.1f))
                .padding(horizontal = 16.dp, vertical = verticalSpacing),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = null,
                    tint = ForStroke.copy(alpha = 0.1f)
                )
                FlowColumn(
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    user?.let {
                        it.firstName?.let { firstName ->
                            Row {
                                Text(
                                    firstName,
                                    modifier = Modifier.height(28.dp),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                )
                                it.lastName?.let { lastName ->
                                    Text(
                                        " $lastName",
                                        modifier = Modifier.height(28.dp),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                    )
                                }
                            }
                        }
                        Text(
                            it.email,
                            modifier = Modifier.height(24.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        it.phoneNumber?.let { phoneNumber ->
                            Text(
                                formatPhoneNumber(phoneNumber),
                                modifier = Modifier.height(20.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                            )
                        }
                    }
                }
            }
            IconButton(
                modifier = Modifier.padding(end = 12.dp),
                onClick = {
                    navController.navigate(ScreenRoutes.EDIT_PROFILE)
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                )
            }
        }
        val profileItems = listOf(
            "Мои заказы" to ScreenRoutes.ORDERS,
            "Избранное" to ScreenRoutes.FAVORITE,
            "Способ оплаты" to ScreenRoutes.PAY_METHOD
        )
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 28.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Column(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = ForStroke.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                )
            ) {
                profileItems.forEachIndexed { index, profileItemsPair ->
                    val (text, route) = profileItemsPair
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                        )
                        IconButton(
                            modifier = Modifier.padding(end = 36.dp),
                            onClick = { navController.navigate(route) }
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.Outlined.ChevronRight,
                                contentDescription = null,
                            )
                        }
                    }
                    if (index != profileItems.size - 1) {
                        HorizontalDivider(color = ForStroke.copy(alpha = 0.1f))
                    }
                }
            }
            val magazineInfo = mapOf(
                Icons.Outlined.Place to "Ростов-на-Дону, Пушкина, 1",
                Icons.Outlined.Phone to "8 (800) 500-26-12",
                Icons.Outlined.Email to "hello@techzone.ru"
            )
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "О магазине",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
                Column(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = ForStroke.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
                ) {
                    magazineInfo.forEach { (icon, text) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(start = 28.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 28.dp)
                            )
                            Text(
                                text,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                            )
                        }
                        if (text != magazineInfo.values.last()) {
                            HorizontalDivider(color = ForStroke.copy(alpha = 0.1f))
                        }
                    }
                }
            }
        }
        OutlinedButton(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            onClick = { userViewModel.logoutUser() },
            border = null,
            contentPadding = PaddingValues(start = 12.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(26.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    "Выйти из профиля",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Composable
fun LoginText(paddingTop: Dp = 12.dp) {
    Text(
        modifier = Modifier.padding(top = paddingTop),
        textAlign = TextAlign.Center,
        text = "Войдите\n или зарегистрируйтесь",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
    )
}

@Composable
fun UnauthorizedScreen(navigateToAuth: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 100.dp, end = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(80.dp),
            imageVector = Icons.Outlined.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        LoginText()
        Text(
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
            text = "Чтобы сохранять товары\n и совершать покупки",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.scrim
        )
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = navigateToAuth
        ) {
            Text(
                text = "Войти или зарегистрироваться",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}