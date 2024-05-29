package com.app.techzone.data.remote.api

import com.app.techzone.data.remote.model.AddFavoriteRequest
import com.app.techzone.data.remote.model.AddReviewRequest
import com.app.techzone.data.remote.model.AddToCartRequest
import com.app.techzone.data.remote.model.Cart
import com.app.techzone.data.remote.model.ChangeQuantityRequest
import com.app.techzone.data.remote.model.CreateOrderRequest
import com.app.techzone.data.remote.model.FavoriteItem
import com.app.techzone.data.remote.model.FavoritesList
import com.app.techzone.data.remote.model.Order
import com.app.techzone.data.remote.model.OrderCreated
import com.app.techzone.data.remote.model.OrdersList
import com.app.techzone.data.remote.model.ProductInCartResponse
import com.app.techzone.data.remote.model.ReviewShort
import com.app.techzone.data.remote.model.User
import com.app.techzone.model.AuthenticationRequest
import com.app.techzone.model.AuthorizationRequest
import com.app.techzone.model.SendCodeRequest
import com.app.techzone.model.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface UserApi {
    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.users)
    suspend fun getUser(@Header("Authorization") token: String): User

    @Headers("Accept: application/json")
    @Multipart
    @PUT(ApiConstants.Endpoints.users)
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
    ): User

    @Headers("Accept: application/json")
    @Multipart
    @PUT(ApiConstants.Endpoints.users)
    suspend fun updateUserWithoutPhoto(
        @Header("Authorization") token: String,
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
    ): User

    @Headers("Accept: application/json")
    @POST(ApiConstants.Endpoints.users)
    suspend fun deleteUser(@Header("Authorization") token: String): User


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


    // Orders
    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.orders)
    suspend fun getOrders(@Header("Authorization") token: String): OrdersList

    @Headers("Accept: application/json")
    @POST(ApiConstants.Endpoints.orders)
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body request: CreateOrderRequest
    ): OrderCreated

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.ordersDetail)
    suspend fun getOrder(
        @Header("Authorization") token: String,
        @Path("id_order") orderId: Int
    ): Order


    // Reviews
    @Headers("Accept: application/json")
    @POST(ApiConstants.Endpoints.addReview)
    suspend fun addReview(
        @Header("Authorization") token: String,
        @Path("id_product") productId: Int,
        @Body request: AddReviewRequest,
    )

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.reviewDetail)
    suspend fun getReview(
        @Header("Authorization") token: String,
        @Path("id_review") reviewId: Int
    ): ReviewShort

    @Headers("Accept: application/json")
    @PATCH(ApiConstants.Endpoints.reviewDetail)
    suspend fun editReview(
        @Header("Authorization") token: String,
        @Path("id_review") reviewId: Int,
        @Body request: AddReviewRequest,
    )

    // Cart
    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.cart)
    suspend fun getCart(@Header("Authorization") token: String): Cart

    @Headers("Accept: application/json")
    @POST(ApiConstants.Endpoints.cart)
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body request: AddToCartRequest
    ): ProductInCartResponse

    @Headers("Accept: application/json")
    @DELETE(ApiConstants.Endpoints.cartDetail)
    suspend fun removeFromCart(
        @Header("Authorization") token: String,
        @Path("id_product") productId: Int
    )

    @Headers("Accept: application/json")
    @PATCH(ApiConstants.Endpoints.cartDetail)
    suspend fun changeQuantityInCart(
        @Header("Authorization") token: String,
        @Path("id_product") productId: Int,
        @Body request: ChangeQuantityRequest
    ): ProductInCartResponse

    @Headers("Accept: application/json")
    @DELETE(ApiConstants.Endpoints.cart)
    suspend fun clearCart(@Header("Authorization") token: String)

}