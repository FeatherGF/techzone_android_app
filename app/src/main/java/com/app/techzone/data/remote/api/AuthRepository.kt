package com.app.techzone.data.remote.api

import com.app.techzone.data.remote.model.AuthResult

interface AuthRepository {
    suspend fun sendAuthenticationCode(email: String): AuthResult<Unit>
    suspend fun authorize(email: String, code: Int): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}