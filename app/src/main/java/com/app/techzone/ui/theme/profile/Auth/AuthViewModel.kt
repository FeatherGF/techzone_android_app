package com.app.techzone.ui.theme.profile.Auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.data.remote.model.User
import com.app.techzone.data.remote.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
): ViewModel() {

    private val resultChannel = Channel<AuthResult<Unit>>()
    var initialState: AuthResult<Unit> = AuthResult.Unauthorized()
    val authResults = resultChannel.receiveAsFlow()
    var state by mutableStateOf(AuthState())

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?>
        get() = _user

    init {
        authenticate()
        viewModelScope.launch {
            _user.value = authRepository.getUser()
        }
    }

    fun onEvent(event: AuthUiEvent) {
        when (event){
            is AuthUiEvent.AuthEmailChanged -> {
                state = state.copy(authEmail = event.value)
            }
            is AuthUiEvent.SendAuthCode -> {
                sendAuthCode()
            }

            is AuthUiEvent.AuthCodeChanged -> {
                state = state.copy(authCode = event.value)
            }
            is AuthUiEvent.VerifyCode -> {
                verifyCode()
            }
        }
    }

    private fun sendAuthCode(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.sendAuthenticationCode(
                email = state.authEmail
            )
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun verifyCode(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            println("${state.authEmail}, ${state.authCode}")
            var result = authRepository.authorize(
                email = state.authEmail,
                code = state.authCode.toInt()
            )
            if (result is AuthResult.Unauthorized){
                result = AuthResult.CodeIncorrect()
            }
            resultChannel.send(result)
            initialState = result
            state = state.copy(isLoading = false)
        }
    }

    private fun authenticate(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = authRepository.authenticate()
            resultChannel.send(result)
            initialState = result
            state = state.copy(isLoading = false)
        }
    }
}