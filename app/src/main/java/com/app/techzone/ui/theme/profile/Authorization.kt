package com.app.techzone.ui.theme.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.app.techzone.LocalNavController
import com.app.techzone.R
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.ui.theme.RoundBorder100
import com.app.techzone.ui.theme.navigation.ScreenRoutes
import com.app.techzone.ui.theme.profile.auth.AuthState
import com.app.techzone.ui.theme.profile.auth.AuthUiEvent
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
fun AuthTopBar() {
    val navController = LocalNavController.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ){
        IconButton(onClick = navController::popBackStack) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
    LoginText(paddingTop = 28.dp)
}

@Composable
fun EnterEmailAddress(
    state: AuthState,
    onEmailTextChange: (String) -> Unit,
    onEmailSendCode: () -> Unit,
) {
    val navController = LocalNavController.current
    var errorText by remember { (mutableStateOf("")) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        value = state.authEmail,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Companion.White,
            focusedContainerColor = Color.Companion.White
        ),
        onValueChange = {
            onEmailTextChange(it)
            if (errorText.isNotBlank()){
                errorText = ""
            }
        },
        placeholder = { Text("Email", style = MaterialTheme.typography.bodyLarge) },
        supportingText = {
            if (errorText.isNotBlank()){
                Text(errorText, style = MaterialTheme.typography.bodySmall)
            }
        },
        isError = errorText.isNotBlank(),
    )
    Button(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth(),
        onClick = {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.authEmail).matches()) {
                errorText = "Email не действителен"
                return@Button
            }
            onEmailSendCode()
        },
        shape = RoundBorder100,
        enabled = state.authEmail.isNotBlank(),
    ) {
        Text("Получить код", style = MaterialTheme.typography.labelLarge)
    }
    val annotatedString = buildAnnotatedString {
        pushStyle(SpanStyle(color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.7f)))
        append("Нажимая кнопку \"Получить код\", Вы соглашаетесь c условиями ")
        pop()
        pushStringAnnotation(tag = "privacy", annotation = "privacy")
        pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)))
        append("политики конфиденциальности")
        pop()
    }
    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.labelLarge.copy(
            letterSpacing = 0.25.sp,
            fontWeight = FontWeight.Normal
        )
    ) {
        navController.navigate(ScreenRoutes.PRIVACY_POLICY)
    }
    when (state.response){
        ServerResponse.LOADING -> { LoadingBox() }
        ServerResponse.ERROR -> { ErrorScreen() }
        ServerResponse.SUCCESS -> {}
        ServerResponse.UNAUTHORIZED -> {}
    }
}


@Composable
fun EnterAuthCode(
    state: AuthState,
    authResultState: AuthResult<Unit>,
    onAuthCodeChanged: (String) -> Unit,
    onCodeVerify: () -> Unit,
    onEmailResend: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Почта получателя: ",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
        )
        Text(
            state.authEmail,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
        )
    }
    var errorText by remember { (mutableStateOf("")) }
    if (authResultState is AuthResult.CodeIncorrect){
        errorText = "Неверный код"
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .background(MaterialTheme.colorScheme.tertiary),
        value = state.authCode,
        onValueChange = {
            onAuthCodeChanged(it)
            if (errorText.isNotBlank()){
                errorText = ""
            }
            if (state.authCode.length == 5){
                if (!state.authCode.isDigitsOnly()){
                    errorText = "Неверный формат. Принимаются только цифры"
                    return@OutlinedTextField
                }
                onCodeVerify()
            } else if (state.authCode.length > 6){
                errorText = "Неверная длина кода. Код должен состоять из 6 цифр"
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Companion.White,
            focusedContainerColor = Color.Companion.White
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        placeholder = { Text("Код из почты", style = MaterialTheme.typography.bodyLarge) },
        supportingText = {
            if (errorText.isNotBlank()){
                Text(errorText, style = MaterialTheme.typography.bodySmall)
            }
        },
        isError = errorText.isNotBlank(),
    )
    var resendAuthCodeCountDown by remember { mutableIntStateOf(60) }
    LaunchedEffect(Unit) {
        while (resendAuthCodeCountDown > 0){
            delay(1.seconds)
            resendAuthCodeCountDown--
        }
    }
    if (resendAuthCodeCountDown > 0){
        Text(
            "Отправить повторно через $resendAuthCodeCountDown сек.",
            modifier = Modifier.padding(top = 26.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
        )
    } else {
        OutlinedButton(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                resendAuthCodeCountDown = 60
                onEmailResend()
            },
            border = null,
        ) {
            Text(
                "Отправить код повторно",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    when (state.response) {
        ServerResponse.LOADING -> { LoadingBox() }
        ServerResponse.ERROR -> { ErrorScreen() }
        ServerResponse.SUCCESS -> {}
        ServerResponse.UNAUTHORIZED -> {}
    }
}


@Composable
fun LoadingBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun Authorization(
    authResultState: AuthResult<Unit>,
    userViewModel: UserViewModel,
) {
    val navController = LocalNavController.current
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
            .fillMaxSize()
            .padding(top = 36.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (authResultState){
            is AuthResult.UnknownError -> {
                val refreshApiCall = if (userViewModel.state.authCode.isEmpty()) {
                    { userViewModel.onAuthEvent(AuthUiEvent.SendAuthCode) }
                } else if (
                    userViewModel.state.authCode.isNotEmpty()
                    && userViewModel.state.authEmail.isNotEmpty()
                ) {
                    { userViewModel.onAuthEvent(AuthUiEvent.VerifyCode) }
                } else {
                    { userViewModel.loadUser() }
                }
                ErrorScreen(refreshApiCall)
            }
            is AuthResult.Unauthorized ->{
                AuthTopBar()
                EnterEmailAddress(
                    state = userViewModel.state,
                    onEmailSendCode = {
                        userViewModel.onAuthEvent(AuthUiEvent.SendAuthCode)
                    },
                    onEmailTextChange = {
                        userViewModel.onAuthEvent(AuthUiEvent.AuthEmailChanged(it))
                    }
                )
            }
            is AuthResult.Authorized -> { navController.navigate(ScreenRoutes.PROFILE) }
            else -> {
                AuthTopBar()
                EnterAuthCode(
                    state = userViewModel.state,
                    authResultState = authResultState,
                    onCodeVerify = {
                        userViewModel.onAuthEvent(AuthUiEvent.VerifyCode)
                    },
                    onAuthCodeChanged = {
                        userViewModel.onAuthEvent(AuthUiEvent.AuthCodeChanged(it))
                    },
                    onEmailResend = {
                        userViewModel.onAuthEvent(AuthUiEvent.SendAuthCode)
                    }
                )
            }
        }
    }
}