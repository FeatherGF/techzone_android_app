package com.app.techzone.ui.theme.profile.Auth

sealed class AuthUiEvent {
    data class AuthEmailChanged(val value: String): AuthUiEvent()
    data object SendAuthCode: AuthUiEvent()

    data class AuthCodeChanged(val value: String): AuthUiEvent()
    data object VerifyCode: AuthUiEvent()
}