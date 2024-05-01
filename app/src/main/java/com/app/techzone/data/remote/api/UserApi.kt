package com.app.techzone.data.remote.api

import com.app.techzone.data.remote.model.AddFavoriteRequest
import com.app.techzone.data.remote.model.FavoriteItem
import com.app.techzone.data.remote.model.FavoritesList
import com.app.techzone.data.remote.model.UserUpdateRequest
import com.app.techzone.data.remote.model.User
import com.app.techzone.model.AuthenticationRequest
import com.app.techzone.model.AuthorizationRequest
import com.app.techzone.model.SendCodeRequest
import com.app.techzone.model.TokenResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


interface UserApi {
    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.userDetail)
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("id_user") userPathId: Int,
    ): User

    @Headers("Accept: application/json")
    @PATCH(ApiConstants.Endpoints.userDetail)
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id_user") userPathId: Int,
        @Body userUpdateRequest: UserUpdateRequest
    ): User

    @Headers("Accept: application/json")
    @POST(ApiConstants.Endpoints.userDetail)
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id_user") userPathId: Int,
    ): User


    // Authentication
    @Headers("Accept: application/json")
    @POST(ApiConstants.Endpoints.sendAuthCode)
    suspend fun sendAuthenticationCode(@Body request: SendCodeRequest)

    @Headers("Accept: application/json")
    @POST(ApiConstants.Endpoints.authorize)
    suspend fun authorize(@Body request: AuthorizationRequest): TokenResponse

    @Headers("Accept: application/json")
    @POST(ApiConstants.Endpoints.authenticate)
    suspend fun authenticate(@Body request: AuthenticationRequest): TokenResponse


    // Favorites
    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.favorites)
    suspend fun getFavorites(@Header("Authorization") token: String): FavoritesList

    @Headers("Accept: application/json")
    @POST(ApiConstants.Endpoints.favorites)
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Body request: AddFavoriteRequest,
    ): FavoriteItem

    @Headers("Accept: application/json")
    @DELETE(ApiConstants.Endpoints.favoritesDetail)
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Path("id_product") productId: Int,
    )
}