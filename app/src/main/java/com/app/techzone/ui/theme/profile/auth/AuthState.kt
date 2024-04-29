package com.app.techzone.ui.theme.profile.auth

import com.app.techzone.ui.theme.server_response.ServerResponse

data class AuthState(
    val response: ServerResponse = ServerResponse.LOADING,
    val authEmail: String = "",
    val authCode: String = ""
)
