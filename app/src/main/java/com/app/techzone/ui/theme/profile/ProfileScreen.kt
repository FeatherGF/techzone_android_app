package com.app.techzone.ui.theme.profile

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.LocalNavController
import com.app.techzone.LocalSnackbarHostState
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.data.remote.model.validateUserInfo
import com.app.techzone.data.remote.repository.ContentUriRequestBody
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder100
import com.app.techzone.ui.theme.dimension
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.reusables.ConfirmationModalSheet
import com.app.techzone.ui.theme.reusables.ProfilePicture
import com.app.techzone.ui.theme.reusables.UserInfoFields
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.UnauthorizedScreen
import com.app.techzone.utils.formatPhoneNumber
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(
    authResultState: AuthResult<Unit>,
    userViewModel: UserViewModel
) {
    when (authResultState) {
        is AuthResult.Authorized -> {
            UserProfile(userViewModel)
        }

        is AuthResult.Unauthorized -> {
            UnauthorizedScreen()
        }

        is AuthResult.UnknownError -> {
            ErrorScreen(userViewModel::loadUser)
        }

        else -> {}
    }
}


@Composable
fun EditUserProfile(userViewModel: UserViewModel) {
    LaunchedEffect(userViewModel) { userViewModel.loadUser() }

    val navController = LocalNavController.current
    val snackbarHostState = LocalSnackbarHostState.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    val user by userViewModel.user.collectAsStateWithLifecycle()

    BackHandler(onBack = navController::popBackStack)
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    var userPhotoUrl by remember { mutableStateOf(user?.photoUrl) }
    val (firstName, onFirstNameChange) = remember { mutableStateOf(user?.firstName ?: "") }
    val (lastName, onLastNameChange) = remember { mutableStateOf(user?.lastName ?: "") }
    val (phoneNumber, onPhoneNumberChange) = remember { mutableStateOf(user?.phoneNumber ?: "") }
    var showPhotoActionModal by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    suspend fun saveUserChanges() {
        val (isValid, reason) = validateUserInfo(firstName, lastName, phoneNumber)
        if (!isValid) {
            snackbarHostState.showSnackbar(reason)
            return
        }
        userViewModel.updateUser(
            imageFile = imageUri?.let { ContentUriRequestBody(context.contentResolver, it) },
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
        )
        navController.navigate(ScreenRoutes.PROFILE)
    }

    // separate column needed in order to apply .weight(1f) and place `save` button at the bottom
    Column {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.tertiary)
                    .padding(
                        start = MaterialTheme.dimension.mediumLarge,
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
                    "Изменить профиль",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                )
            }
            Column(
                modifier = Modifier
                    .padding(
                        start = MaterialTheme.dimension.extendedMedium,
                        top = MaterialTheme.dimension.large,
                        end = MaterialTheme.dimension.extendedMedium
                    )
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ProfilePicture(
                        Modifier
                            .size(MaterialTheme.dimension.mediumLarge * 5)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { showPhotoActionModal = true }
                            ),
                        userPhotoUrl = userPhotoUrl,
                        imageUri = imageUri
                    )
                    if (isPressed || isHovered) {
                        val size =
                            if (userPhotoUrl != null || imageUri != null)
                                MaterialTheme.dimension.mediumLarge * 5
                            else MaterialTheme.dimension.extraLarge * 2
                        Box(
                            Modifier
                                .size(size)
                                .clip(RoundBorder100)
                                .background(ForStroke.copy(alpha = 0.4f))
                        )
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }

                UserInfoFields(
                    firstName = firstName,
                    onFirstNameChange = onFirstNameChange,
                    lastName = lastName,
                    onLastNameChange = onLastNameChange,
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = onPhoneNumberChange,
                    email = user?.email!!,
                    phoneFieldActions = KeyboardActions(
                        onSend = {
                            coroutineScope.launch {
                                saveUserChanges()
                            }
                            // if text fields contained errors, hide keyboard to make snackbar visible
                            keyboardController?.hide()
                        }
                    )
                )

                var isBottomSheetDeleteUserShown by remember { mutableStateOf(false) }
                OutlinedButton(
                    modifier = Modifier.padding(top = MaterialTheme.dimension.extendedMedium),
                    onClick = { isBottomSheetDeleteUserShown = true },
                    border = null,
                    contentPadding = PaddingValues(
                        start = MaterialTheme.dimension.medium,
                        end = MaterialTheme.dimension.extendedMedium
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onError,
                            modifier = Modifier
                                .size(MaterialTheme.dimension.large)
                                .padding(end = MaterialTheme.dimension.small)
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
                        onDismiss = { isBottomSheetDeleteUserShown = false }
                    )
                }
            }
        }
        if (showPhotoActionModal) {
            userPhotoUrl?.let {
                ConfirmationModalSheet(
                    confirmationText = "",
                    confirmOptionText = "Выбрать фото",
                    onConfirm = {
                        launcher.launch("image/*")
                        showPhotoActionModal = false
                    },
                    thirdActionOptionText = "Удалить фото",
                    onThirdAction = {
                        coroutineScope.launch {
                            if (userViewModel.deleteUserPhoto()) {
                                userPhotoUrl = null
                                snackbarHostState.showSnackbar(
                                    "Фотография успешно удалена"
                                )
                            } else {
                                snackbarHostState.showSnackbar(
                                    "Что-то пошло не так." +
                                            "\nПроверьте подключение к интернету и попробуйте снова"
                                )
                            }
                        }
                        showPhotoActionModal = false
                    },
                    onDismiss = { showPhotoActionModal = false }
                )
            } ?: ConfirmationModalSheet(
                confirmationText = "",
                confirmOptionText = "Выбрать фото",
                onConfirm = {
                    launcher.launch("image/*")
                    showPhotoActionModal = false
                },
                onDismiss = { showPhotoActionModal = false }
            )
        }
        Surface(
            Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = ForStroke.copy(alpha = 0.1f))
                .padding(
                    horizontal = MaterialTheme.dimension.extendedMedium,
                    vertical = MaterialTheme.dimension.small
                ),
            contentColor = MaterialTheme.colorScheme.tertiary,
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    coroutineScope.launch {
                        saveUserChanges()
                    }
                }
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UserProfile(userViewModel: UserViewModel) {
    LaunchedEffect(userViewModel.user) { userViewModel.loadUser() }
    val navController = LocalNavController.current
    val user by userViewModel.user.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.tertiary)
                .padding(
                    top = MaterialTheme.dimension.extraLarge,
                    bottom = MaterialTheme.dimension.extendedMedium
                ),
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
                MaterialTheme.dimension.medium
            else MaterialTheme.dimension.large
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = MaterialTheme.colorScheme.tertiary)
                .border(width = 1.dp, color = ForStroke.copy(alpha = 0.1f))
                .padding(
                    horizontal = MaterialTheme.dimension.extendedMedium,
                    vertical = verticalSpacing
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfilePicture(
                    Modifier.size(MaterialTheme.dimension.extraLarge * 2),
                    userPhotoUrl = user?.photoUrl,
                    imageUri = null,
                    iconTint = ForStroke
                )
                FlowColumn(
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    user?.let {
                        it.firstName?.let { firstName ->
                            Row {
                                Text(
                                    firstName,
                                    modifier = Modifier.height(MaterialTheme.dimension.larger),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                )
                                it.lastName?.let { lastName ->
                                    Text(
                                        " $lastName",
                                        modifier = Modifier.height(MaterialTheme.dimension.larger),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                                    )
                                }
                            }
                        }
                        Text(
                            it.email,
                            modifier = Modifier.height(MaterialTheme.dimension.large),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        it.phoneNumber?.let { phoneNumber ->
                            Text(
                                formatPhoneNumber(phoneNumber),
                                modifier = Modifier.height(MaterialTheme.dimension.large),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                            )
                        }
                    }
                }
            }
            IconButton(
                modifier = Modifier.padding(end = MaterialTheme.dimension.medium),
                onClick = {
                    navController.navigate(ScreenRoutes.EDIT_PROFILE)
                }
            ) {
                Icon(
                    modifier = Modifier.size(MaterialTheme.dimension.larger),
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
            modifier = Modifier.padding(
                start = MaterialTheme.dimension.extendedMedium,
                end = MaterialTheme.dimension.extendedMedium,
                top = MaterialTheme.dimension.large
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.large)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.tertiary)
                    .border(
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
                            .height(MaterialTheme.dimension.huge)
                            .padding(start = MaterialTheme.dimension.extendedMedium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                        )
                        IconButton(
                            modifier = Modifier.padding(end = MaterialTheme.dimension.larger),
                            onClick = { navController.navigate(route) }
                        ) {
                            Icon(
                                modifier = Modifier.size(MaterialTheme.dimension.larger),
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
                Icons.Outlined.Place to "Ростов-на-Дону, 18-линия 8",
                Icons.Outlined.Phone to "8 (904) 340-55-56",
                Icons.Outlined.Email to "office@wis.software"
            )
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.extendedMedium)) {
                Text(
                    "О магазине",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                )
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiary)
                        .border(
                            width = 1.dp,
                            color = ForStroke.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    magazineInfo.forEach { (icon, text) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.dimension.huge)
                                .padding(start = MaterialTheme.dimension.large),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = MaterialTheme.dimension.large)
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
            modifier = Modifier.padding(
                start = MaterialTheme.dimension.extendedMedium,
                top = MaterialTheme.dimension.extendedMedium
            ),
            onClick = { userViewModel.logoutUser() },
            border = null,
            contentPadding = PaddingValues(
                start = MaterialTheme.dimension.medium,
                end = MaterialTheme.dimension.extendedMedium
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(MaterialTheme.dimension.large)
                        .padding(end = MaterialTheme.dimension.small)
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
internal fun LoginText(paddingTop: Dp = MaterialTheme.dimension.medium) =
    Text(
        modifier = Modifier.padding(top = paddingTop),
        textAlign = TextAlign.Center,
        text = "Войдите\n или зарегистрируйтесь",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
    )
