package com.app.techzone.ui.theme.profile.auth

data class AuthState(
    val isLoading: Boolean = false,
    val authEmail: String = "",
    val authCode: String = ""
)
