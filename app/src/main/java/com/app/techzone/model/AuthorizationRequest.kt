package com.app.techzone.model

data class AuthorizationRequest(
    val email: String,
    val code: Int
)
