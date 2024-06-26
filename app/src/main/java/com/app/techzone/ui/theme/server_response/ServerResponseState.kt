package com.app.techzone.ui.theme.server_response


enum class ServerResponse {
    LOADING,
    ERROR,
    SUCCESS,
    UNAUTHORIZED,
}

data class ServerResponseState(
    val response: ServerResponse = ServerResponse.LOADING
)
