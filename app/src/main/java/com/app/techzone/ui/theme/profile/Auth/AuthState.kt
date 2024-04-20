package com.app.techzone.ui.theme.profile.Auth

data class AuthState(
    val isLoading: Boolean = false,
    val authEmail: String = "",
    val authCode: String = ""
)
