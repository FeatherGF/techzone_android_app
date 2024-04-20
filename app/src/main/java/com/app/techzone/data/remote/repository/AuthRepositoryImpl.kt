package com.app.techzone.data.remote.repository

import android.content.Context
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.app.techzone.data.remote.api.UserApi
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.data.remote.model.User
import com.app.techzone.dataStore
import com.app.techzone.model.AuthenticationRequest
import com.app.techzone.model.AuthorizationRequest
import com.app.techzone.model.SendCodeRequest
import com.app.techzone.model.TokenResponse
import org.json.JSONObject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.lang.IllegalArgumentException
import java.nio.charset.StandardCharsets
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    @ApplicationContext private val context: Context
): AuthRepository {

    private val dataStore = context.dataStore

    private object PreferencesKey {
        val accessToken = stringPreferencesKey("accessToken")
        val refreshToken = stringPreferencesKey("refreshToken")
    }

    private suspend fun <T>handleExceptions(call: suspend () -> AuthResult<T>): AuthResult<T> {
        return try {
            call()
        } catch (e: HttpException){
            if (e.code() == 401){
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        }
    }

    suspend fun getUser(): User {
        authenticate()
        val accessToken: String = dataStore.data.map { preferences ->
            preferences[PreferencesKey.accessToken] ?: ""
        }.first()
        return userApi.getUser(
            accessToken,
            // backend will add fixes to remove this abomination
            userPathId = decodeToken(accessToken).get("sub").toString().toInt()
        )
    }

    override suspend fun sendAuthenticationCode(email: String): AuthResult<Unit> {
        return handleExceptions {
            userApi.sendAuthenticationCode(
                request = SendCodeRequest(
                    email = email
                )
            )
            AuthResult.CodeSent()
        }
    }

    override suspend fun authorize(email: String, code: Int): AuthResult<Unit> {
        return handleExceptions {
            val tokenResponse = userApi.authorize(
                request = AuthorizationRequest(
                    identifier = email,
                    code = code
                )
            )
            storeTokens(tokenResponse)
            AuthResult.Authorized()
        }
    }

    private suspend fun storeTokens(tokenResponse: TokenResponse){
        dataStore.edit { preferences ->
            preferences[PreferencesKey.accessToken] = tokenResponse.accessToken
            preferences[PreferencesKey.refreshToken] = tokenResponse.refreshToken
        }
    }

    private fun decodeToken(token: String, partIndex: Int = 1): JSONObject {
        if (partIndex !in 0..1) {
            throw IllegalArgumentException("partIndex parameter can not have value other than 0 or 1")
        }
        val decodedToken = String(Base64.decode(token.split(".")[1], Base64.DEFAULT), StandardCharsets.UTF_8)
        return JSONObject(decodedToken)

    }

    private fun isTokenExpired(decodedToken: JSONObject): Boolean {
        return decodedToken.get("exp").toString().toLong() < (System.currentTimeMillis() / 1000)
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return handleExceptions {
            val accessToken: String = dataStore.data.map { preferences ->
                preferences[PreferencesKey.accessToken] ?: ""
            }.first()

            if (accessToken.isNotEmpty() && !isTokenExpired(decodeToken(accessToken))) {
                return@handleExceptions AuthResult.Authorized()
            }

            val refreshToken: String = dataStore.data.map { preferences ->
                preferences[PreferencesKey.refreshToken] ?: ""
            }.first()

            val tokenResponse = userApi.authenticate(
                request = AuthenticationRequest(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )

            storeTokens(tokenResponse)
            AuthResult.Authorized()
        }
    }
}