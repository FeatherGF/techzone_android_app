package com.app.techzone.data.remote.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
import com.app.techzone.data.remote.api.AuthRepository
import com.app.techzone.data.remote.api.UserApi
import com.app.techzone.data.remote.model.AddFavoriteRequest
import com.app.techzone.data.remote.model.AddToCartRequest
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.data.remote.model.ChangeQuantityRequest
import com.app.techzone.data.remote.model.CreateOrderRequest
import com.app.techzone.data.remote.model.Order
import com.app.techzone.data.remote.model.OrderCreated
import com.app.techzone.data.remote.model.OrdersList
import com.app.techzone.data.remote.model.ProductInCartResponse
import com.app.techzone.data.remote.model.User
import com.app.techzone.model.AuthenticationRequest
import com.app.techzone.model.AuthorizationRequest
import com.app.techzone.model.SendCodeRequest
import com.app.techzone.model.TokenResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import okio.source
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.inject.Inject


class ContentUriRequestBody(
    private val contentResolver: ContentResolver,
    private val contentUri: Uri
) : RequestBody() {

    override fun contentType(): MediaType? {
        val contentType = contentResolver.getType(contentUri)
        return contentType?.toMediaTypeOrNull()
    }

    override fun writeTo(sink: BufferedSink) {
        val inputStream = contentResolver.openInputStream(contentUri)
            ?: throw IOException("Couldn't open content URI for reading")

        inputStream.source().use { source ->
            sink.writeAll(source)
        }
    }
}


class UserRepo @Inject constructor(
    private val userApi: UserApi,
    private val prefs: EncryptedSharedPreferencesImpl,
) : AuthRepository {

    private suspend fun <T> handleExceptions(call: suspend () -> AuthResult<T>): AuthResult<T> {
        return try {
            call()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: IOException) {
            AuthResult.UnknownError()
        }
    }

    fun logoutUser() = prefs.sharedPreferences.edit().clear().apply()

    suspend fun getUser(): User? {
        authenticate()
        val accessToken = prefs.getKey(PreferencesKey.accessToken) ?: return null
        return try {
            userApi.getUser(accessToken)
        } catch (e: IndexOutOfBoundsException) {
            null
        } catch (e: IOException) {
            null
        }
    }

    suspend fun updateUser(
        imageFile: RequestBody? = null,
        firstName: String? = null,
        lastName: String? = null,
        phoneNumber: String? = null
    ): AuthResult<Unit> {
        return handleExceptions {
            authenticate()
            val accessToken = prefs.getKey(PreferencesKey.accessToken)
                ?: return@handleExceptions AuthResult.Unauthorized()
            if (imageFile != null){
                val imagePart = MultipartBody.Part.createFormData(
                    "photo",
                    "profile_photo.jpg",
                    imageFile
                )
                userApi.updateUser(
                    token = accessToken,
                    photo = imagePart,
                    firstName = firstName.orEmpty().toRequestBody(firstName?.toMediaTypeOrNull()),
                    lastName = lastName.orEmpty().toRequestBody(lastName?.toMediaTypeOrNull()),
                    phoneNumber = phoneNumber.orEmpty().toRequestBody(phoneNumber?.toMediaTypeOrNull())
                )
            } else {
                userApi.updateUserWithoutPhoto(
                    token = accessToken,
                    firstName = firstName.orEmpty().toRequestBody(firstName?.toMediaTypeOrNull()),
                    lastName = lastName.orEmpty().toRequestBody(lastName?.toMediaTypeOrNull()),
                    phoneNumber = phoneNumber.orEmpty().toRequestBody(phoneNumber?.toMediaTypeOrNull())
                )
            }
            AuthResult.Authorized()
        }
    }

    suspend fun deleteUser(): Boolean {
        authenticate()
        val accessToken = prefs.getKey(PreferencesKey.accessToken) ?: return false
        val userIdToDelete = decodeToken(accessToken).get("sub").toString().toInt()
        return try {
            val deletedUser = userApi.deleteUser(accessToken)
            userIdToDelete == deletedUser.id
        } catch (e: IOException) {
            false
        } catch (e: HttpException) {
            false
        }
    }

    override suspend fun sendAuthenticationCode(email: String): AuthResult<Unit> {
        return handleExceptions {
            userApi.sendAuthenticationCode(
                SendCodeRequest(email)
            )
            AuthResult.CodeSent()
        }
    }

    override suspend fun authorize(email: String, code: Int): AuthResult<Unit> {
        return handleExceptions {
            val tokenResponse = userApi.authorize(
                request = AuthorizationRequest(
                    email = email,
                    code = code
                )
            )
            storeTokens(tokenResponse)
            AuthResult.Authorized()
        }
    }

    private fun storeTokens(tokenResponse: TokenResponse) {
        prefs.sharedPreferences.edit().putString(
            PreferencesKey.accessToken, tokenResponse.accessToken
        ).apply()
        prefs.sharedPreferences.edit().putString(
            PreferencesKey.refreshToken, tokenResponse.refreshToken
        ).apply()
    }

    private fun decodeToken(token: String, partIndex: Int = 1): JSONObject {
        if (partIndex !in 0..1) {
            throw IllegalArgumentException("partIndex parameter can not have value other than 0 or 1")
        }
        val decodedToken =
            String(
                Base64.decode(token.split(".")[partIndex], Base64.DEFAULT),
                StandardCharsets.UTF_8
            )
        return JSONObject(decodedToken)
    }

    private fun isTokenExpired(decodedToken: JSONObject): Boolean {
        return decodedToken.get("exp").toString().toLong() < (System.currentTimeMillis() / 1000)
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return handleExceptions {
            val accessToken = prefs.getKey(PreferencesKey.accessToken)
                ?: return@handleExceptions AuthResult.Unauthorized()

            if (accessToken.isNotEmpty() && !isTokenExpired(decodeToken(accessToken))) {
                return@handleExceptions AuthResult.Authorized()
            }

            val refreshToken = prefs.getKey(PreferencesKey.refreshToken)
                ?: return@handleExceptions AuthResult.Unauthorized()

            if (isTokenExpired(decodeToken(refreshToken))) {
                return@handleExceptions AuthResult.Unauthorized()
            }

            // if only refresh token is passed. then backend will generate new access token
            // (which is expired at this point in code) and refresh token.
            // After that both of them will be stored. If user logs in after a month of inactivity
            // he will be requested to log in again using email
            val tokenResponse = userApi.authenticate(
                request = AuthenticationRequest(
                    refreshToken = refreshToken
                )
            )

            storeTokens(tokenResponse)
            AuthResult.Authorized()
        }
    }

    // Favorites
    @Suppress("UNCHECKED_CAST")
    suspend fun <T> getFavorites(): AuthResult<T> {
        authenticate()
        val accessToken =
            prefs.getKey(PreferencesKey.accessToken) ?: return AuthResult.Unauthorized()
        return try {
            val favorites = userApi.getFavorites(accessToken)
            AuthResult.Authorized(favorites as T)
        } catch (e: IOException) {
            AuthResult.UnknownError()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        }
    }

    suspend fun addToFavorite(productId: Int): Boolean? {
        authenticate()
        val accessToken = prefs.getKey(PreferencesKey.accessToken) ?: return false
        return try {
            userApi.addFavorite(accessToken, AddFavoriteRequest(productId))
            true
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            false
        }
    }

    suspend fun removeFromFavorite(productId: Int): Boolean? {
        authenticate()
        val accessToken = prefs.getKey(PreferencesKey.accessToken) ?: return false
        return try {
            userApi.removeFavorite(accessToken, productId)
            true
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            false
        }
    }

    // Orders
    suspend fun getOrders(): OrdersList? {
        authenticate()
        val accessToken = prefs.getKey(PreferencesKey.accessToken) ?: return null
        return try {
            userApi.getOrders(accessToken)
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            null
        }
    }

    suspend fun createOrder(orderItemIds: List<Int>, paymentMethod: String): OrderCreated? {
        authenticate()
        val accessToken = prefs.getKey(PreferencesKey.accessToken) ?: return null
        return try {
            userApi.createOrder(
                token = accessToken,
                request = CreateOrderRequest(
                    orderItemIds, paymentMethod
                )
            )
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            null
        }
    }

    suspend fun getOrder(orderId: Int): Order? {
        authenticate()
        val accessToken = prefs.getKey(PreferencesKey.accessToken) ?: return null
        return try {
            userApi.getOrder(accessToken, orderId)
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            null
        }
    }

    // Cart
    @Suppress("UNCHECKED_CAST")
    suspend fun <T> getCart(): AuthResult<T> {
        authenticate()
        val accessToken =
            prefs.getKey(PreferencesKey.accessToken) ?: return AuthResult.Unauthorized()
        return try {
            val cart = userApi.getCart(accessToken)
            AuthResult.Authorized(cart as T)
        } catch (e: IOException) {
            AuthResult.UnknownError()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        }
    }

    suspend fun addToCart(productId: Int): ProductInCartResponse? {
        authenticate()
        val accessToken = prefs.getKey(PreferencesKey.accessToken) ?: return null
        return try {
            userApi.addToCart(
                token = accessToken,
                request = AddToCartRequest(productId)
            )
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            null
        }
    }

    suspend fun removeFromCart(productId: Int): Boolean? {
        authenticate()
        val accessToken = prefs.getKey(PreferencesKey.accessToken) ?: return false
        return try {
            userApi.removeFromCart(
                token = accessToken,
                productId = productId
            )
            true
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            false
        }
    }

    suspend fun changeQuantityInCart(productId: Int, quantity: Int): ProductInCartResponse? {
        authenticate()
        val accessToken = prefs.getKey(PreferencesKey.accessToken) ?: return null
        return try {
            userApi.changeQuantityInCart(
                token = accessToken,
                productId = productId,
                request = ChangeQuantityRequest(quantity)
            )
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            null
        }
    }
}