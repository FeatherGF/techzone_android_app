package com.app.techzone.data.remote.api

import com.app.techzone.data.remote.model.User
import com.app.techzone.model.AuthenticationRequest
import com.app.techzone.model.AuthorizationRequest
import com.app.techzone.model.SendCodeRequest
import com.app.techzone.model.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface UserApi {
    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.userDetail)
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("id_user") userPathId: Int,
        @Query("id_user") userId: Int = 1
    ): User

    @POST(ApiConstants.Endpoints.sendAuthCode)
    suspend fun sendAuthenticationCode(
        @Body request: SendCodeRequest
    )

    @POST(ApiConstants.Endpoints.authorize)
    suspend fun authorize(
        @Body request: AuthorizationRequest
    ): TokenResponse

    @POST(ApiConstants.Endpoints.authenticate)
    suspend fun authenticate(
        @Body request: AuthenticationRequest
    ): TokenResponse
}