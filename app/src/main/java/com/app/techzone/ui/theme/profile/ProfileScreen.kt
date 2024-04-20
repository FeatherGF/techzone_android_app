package com.app.techzone.ui.theme.profile
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.ui.theme.profile.Auth.AuthViewModel
import kotlinx.coroutines.flow.collect


@Composable
fun ProfileScreen(
    navigateToAuth: () -> Unit,
    authViewModel: AuthViewModel
) {
    val authResultState by authViewModel.authResults.collectAsStateWithLifecycle(authViewModel.initialState)
    when (authResultState){
        is AuthResult.Authorized ->{
            UserProfile(authViewModel)
        }
        is AuthResult.Unauthorized -> {
            UnauthorizedScreen(navigateToAuth = navigateToAuth)
        }
        is AuthResult.UnknownError ->{
            Text("Unknown error occurred")
        }
        else -> {}
    }
}


@Composable
fun UserProfile(
    authViewModel: AuthViewModel
) {
    val user by authViewModel.user.collectAsStateWithLifecycle()
    // TODO: доверстать
    user?.let {
        Text(it.email)
        it.firstName?.let { firstName ->
            Text(firstName)
        }
        it.lastName?.let {lastName ->
            Text(lastName)
        }
        it.phoneNumber?.let {phoneNumber ->
            Text(phoneNumber)
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
    ){
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