package com.app.techzone.ui.theme.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.R
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.ui.theme.RoundBorder100
import com.app.techzone.ui.theme.profile.Auth.AuthState
import com.app.techzone.ui.theme.profile.Auth.AuthUiEvent
import com.app.techzone.ui.theme.profile.Auth.AuthViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
fun AuthTopBar(onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ){
        IconButton(onClick = onBackClicked) {
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
    emailText: String,
    onEmailTextChange: (String) -> Unit,
    onEmailSendCode: () -> Unit,
) {
//    var emailText by remember { mutableStateOf("")}
    var errorText by remember { (mutableStateOf("")) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        value = emailText,
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
            .padding(top = 16.dp)
            .fillMaxWidth(),
        onClick = {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                errorText = "Email не действителен"
                return@Button
            }
            println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH")
            onEmailSendCode()
            /*TODO: viewModel.sendAuthCode or something like that*/
        },
        shape = RoundBorder100,
        enabled = emailText.isNotBlank(),
    ) {
        Text("Получить код", style = MaterialTheme.typography.labelLarge)
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
            .padding(top = 20.dp),
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        placeholder = { Text("Код из почты", style = MaterialTheme.typography.bodyLarge) },
        supportingText = {
            if (errorText.isNotBlank()){
                Text(errorText, style = MaterialTheme.typography.bodySmall)
            }
        },
        isError = errorText.isNotBlank(),
    )
    var resendAuthCodeCountDown by remember { mutableIntStateOf(60)}
    if (resendAuthCodeCountDown > 0){
        Text(
            "Отправить повторно через $resendAuthCodeCountDown сек.",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
        )
    } else {
        Text(
            "Отправить код повторно",
            modifier = Modifier.clickable { onEmailResend() },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
    LaunchedEffect(Unit) {
        while (resendAuthCodeCountDown > 0){
            delay(1.seconds)
            resendAuthCodeCountDown--
        }
    }
}

@Composable
fun Authorization(
    onBackClicked: () -> Unit,
    authViewModel: AuthViewModel,
    navigateToProfile: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.tertiary)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthTopBar(onBackClicked)
        val authResultState by authViewModel.authResults.collectAsStateWithLifecycle(initialValue = AuthResult.Unauthorized())
        val state = authViewModel.state
        when (authResultState){
            is AuthResult.UnknownError -> {Text("Unknown error happened")}
            is AuthResult.Unauthorized ->{
                EnterEmailAddress(
                    emailText = state.authEmail,
                    onEmailSendCode = {
                        authViewModel.onEvent(AuthUiEvent.SendAuthCode)
                    },
                    onEmailTextChange = {
                        authViewModel.onEvent(AuthUiEvent.AuthEmailChanged(it))
                    }
                )
            }
            is AuthResult.Authorized -> { navigateToProfile() }
            else -> {
                EnterAuthCode(
                    state = state,
                    authResultState = authResultState,
                    onCodeVerify = {
                        authViewModel.onEvent(AuthUiEvent.VerifyCode)
                    },
                    onAuthCodeChanged = {
                        authViewModel.onEvent(AuthUiEvent.AuthCodeChanged(it))
                    },
                    onEmailResend = {
                        authViewModel.onEvent(AuthUiEvent.SendAuthCode)
                    }
                )
            }
        }
    }
}