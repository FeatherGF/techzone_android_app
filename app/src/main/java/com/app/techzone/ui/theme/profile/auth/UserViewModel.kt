package com.app.techzone.ui.theme.profile.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.data.remote.model.User
import com.app.techzone.data.remote.repository.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo: UserRepo,
): ViewModel() {

    private val resultChannel = Channel<AuthResult<Unit>>()
    var initialState: AuthResult<Unit> = AuthResult.Unauthorized()
    val authResults = resultChannel.receiveAsFlow()
    var state by mutableStateOf(AuthState())

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        authenticate()
    }

    fun logoutUser(){
        viewModelScope.launch {
            userRepo.logoutUser()
            resultChannel.send(AuthResult.Unauthorized())
            initialState = AuthResult.Unauthorized()
        }
    }

    fun deleteUser(){
        viewModelScope.launch {
            val result: AuthResult<Unit> =
                if (userRepo.deleteUser())
                    AuthResult.Unauthorized()
                else AuthResult.UnknownError()
            initialState = result
            resultChannel.send(result)
        }
    }

    fun updateUser(
        firstName: String? = null,
        lastName: String? = null,
        phoneNumber: String? = null
    ) {
        viewModelScope.launch {
            val result = userRepo.updateUser(
                firstName, lastName, phoneNumber
            )
            initialState = result
            resultChannel.send(result)
        }
    }

    fun loadUser() {
        state = state.copy(isLoading = true)
        viewModelScope.launch{
            _user.value = userRepo.getUser()
        }
        state = state.copy(isLoading = false)
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
            val result = userRepo.sendAuthenticationCode(
                email = state.authEmail
            )
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun verifyCode(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            var result = userRepo.authorize(
                email = state.authEmail,
                code = state.authCode.toInt()
            )
            if (result is AuthResult.Unauthorized){
                result = AuthResult.CodeIncorrect()
            }
            resultChannel.send(result)
            if (result is AuthResult.Authorized) {
                initialState = result
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun authenticate(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = userRepo.authenticate()
            resultChannel.send(result)
            initialState = result
            state = state.copy(isLoading = false)
        }
    }
}