package com.app.techzone.model

data class AuthorizationRequest(
    val identifier: String,
    val code: Int
)
