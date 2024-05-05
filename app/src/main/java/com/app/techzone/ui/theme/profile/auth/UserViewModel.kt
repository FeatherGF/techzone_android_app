package com.app.techzone.ui.theme.profile.auth

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.data.remote.model.FavoritesList
import com.app.techzone.data.remote.model.IBaseProduct
import com.app.techzone.data.remote.model.Order
import com.app.techzone.data.remote.model.OrderItem
import com.app.techzone.data.remote.model.User
import com.app.techzone.data.remote.repository.ProductRepo
import com.app.techzone.data.remote.repository.UserRepo
import com.app.techzone.ui.theme.server_response.ServerResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val productRepo: ProductRepo
): ViewModel() {

    private val resultChannel = Channel<AuthResult<Unit>>()
    var initialState: AuthResult<Unit> = AuthResult.Unauthorized()
    val authResults = resultChannel.receiveAsFlow()
    var state by mutableStateOf(AuthState())

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _favorites = MutableStateFlow(emptyList<IBaseProduct>())
    val favorites = _favorites.asStateFlow()

    private val _cartItems = MutableStateFlow(emptyList<OrderItem>())
    val cartItems = _cartItems.asStateFlow()

    private val _orders = MutableStateFlow(emptyList<Order>())
    val orders = _orders.asStateFlow()

    init {
        authenticate()
        loadFavorites()
    }

    fun logoutUser(){
        viewModelScope.launch {
            userRepo.logoutUser()
            resultChannel.send(AuthResult.Unauthorized())
            initialState = AuthResult.Unauthorized()
        }
    }

    fun deleteUser(){
        viewModelScope.launch {
            val result: AuthResult<Unit> =
                if (userRepo.deleteUser())
                    AuthResult.Unauthorized()
                else AuthResult.UnknownError()
            initialState = result
            resultChannel.send(result)
        }
    }

    fun updateUser(
        firstName: String? = null,
        lastName: String? = null,
        phoneNumber: String? = null
    ) {
        state = state.copy(response = ServerResponse.LOADING)
        viewModelScope.launch {
            val result = userRepo.updateUser(
                firstName, lastName, phoneNumber
            )
            initialState = result
            resultChannel.send(result)
        }
        state = state.copy(response = ServerResponse.SUCCESS)
    }

    fun loadUser() {
        state = state.copy(response = ServerResponse.LOADING)
        viewModelScope.launch{
            val response = userRepo.getUser()
            if (response == null) {
                state = state.copy(response = ServerResponse.ERROR)
                resultChannel.send(AuthResult.UnknownError())
                return@launch
            }
            _user.value = response
            resultChannel.send(AuthResult.Authorized())
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }

    fun loadFavorites() {
        state = state.copy(response = ServerResponse.LOADING)
        viewModelScope.launch {
            val response: AuthResult<FavoritesList> = userRepo.getFavorites()
            when (response){
                is AuthResult.Unauthorized -> {
                    resultChannel.send(AuthResult.Unauthorized())
                    state = state.copy(response = ServerResponse.UNAUTHORIZED)
                }
                is AuthResult.UnknownError -> {
                    resultChannel.send(AuthResult.UnknownError())
                    state = state.copy(response = ServerResponse.ERROR)
                }
                else -> {}
            }
            if (response is AuthResult.Authorized){
                // TODO: will be optimised after upcoming backend release
                val products = response.data?.items?.map {
                    viewModelScope.async { productRepo.getProduct(it.productId) }
                } ?: emptyList()
//                _favorites.value = products.mapNotNull { it.await() }
                _favorites.update {
                    products.mapNotNull { it.await() }
                }

                resultChannel.send(AuthResult.Authorized())
                state = state.copy(response = ServerResponse.SUCCESS)
            }
        }
    }

    fun addToFavorite(
        productId: Int,
        snackbarHostState: SnackbarHostState,
        navigateToFavorite: () -> Unit
    ): Int {
        viewModelScope.launch {
            val isSuccessful = userRepo.addToFavorite(productId)
            if (isSuccessful == null){
                snackbarHostState.showSnackbar(
                    "Что-то пошло не так\nПроверьте подключение к интернету"
                )
                return@launch
            }
            if (!isSuccessful){
                snackbarHostState.showSnackbar(
                    "Авторизуйтесь в приложение, чтобы добавлять товары в избранное"
                )
                return@launch
            }
            val result = snackbarHostState.showSnackbar(
                message = "Товар добавлен в избранное",
                actionLabel = "Перейти",
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {
                    navigateToFavorite()
                }
            }
        }
        loadFavorites()
        return _favorites.value.size
    }

    fun removeFromFavorite(
        productId: Int,
        snackbarHostState: SnackbarHostState,
        navigateToFavorite: () -> Unit
    ): Int {
        viewModelScope.launch {
            val isSuccessful = userRepo.removeFromFavorite(productId)
            if (isSuccessful == null){
                snackbarHostState.showSnackbar(
                    "Что-то пошло не так\nПроверьте подключение к интернету"
                )
                return@launch
            }
            if (!isSuccessful){
                snackbarHostState.showSnackbar(
                    "Авторизуйтесь в приложение, чтобы добавлять товары в избранное"
                )
                return@launch
            }
            val result = snackbarHostState.showSnackbar(
                message = "Товар удален из избранного",
                actionLabel = "Отмена",
                duration = SnackbarDuration.Short
            )
            when (result){
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {
//                    userRepo.addToFavorite(productId)
                    addToFavorite(productId, snackbarHostState, navigateToFavorite)
                }
            }
        }
        loadFavorites()
        return _favorites.value.size
    }

    fun loadCart() {
        state = state.copy(response = ServerResponse.LOADING)
        viewModelScope.launch {
            val response = userRepo.getCart()
            if (response == null) {
                state = state.copy(response = ServerResponse.ERROR)
                return@launch
            }

            // TODO: change to regular orderItem when backend adds quantity in responses
            _cartItems.value = response.orderItem.onEach {
                it.quantity = 1
            }
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }

    fun loadOrders() {
        state = state.copy(response = ServerResponse.LOADING)
        viewModelScope.launch {
            val response = userRepo.getOrders()
            if (response == null) {
                state = state.copy(response = ServerResponse.ERROR)
                return@launch
            }
            _orders.value = response.items
            // TODO: change to regular orderItem when backend adds quantity in responses
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }

    fun onEvent(event: AuthUiEvent) {
        when (event){
            is AuthUiEvent.AuthEmailChanged -> {
                state = state.copy(authEmail = event.value)
            }
            is AuthUiEvent.SendAuthCode -> {
                sendAuthCode()
            }

            is AuthUiEvent.AuthCodeChanged -> {
                state = state.copy(authCode = event.value)
            }
            is AuthUiEvent.VerifyCode -> {
                verifyCode()
            }
        }
    }

    private fun sendAuthCode(){
        viewModelScope.launch {
            state = state.copy(response = ServerResponse.LOADING)
            val result = userRepo.sendAuthenticationCode(
                email = state.authEmail
            )
            resultChannel.send(result)
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }

    private fun verifyCode(){
        viewModelScope.launch {
            state = state.copy(response = ServerResponse.LOADING)
            var result = userRepo.authorize(
                email = state.authEmail,
                code = state.authCode.toInt()
            )
            if (result is AuthResult.Unauthorized){
                result = AuthResult.CodeIncorrect()
            }
            resultChannel.send(result)
            if (result is AuthResult.Authorized) {
                initialState = result
            }
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }

    private fun authenticate(){
        viewModelScope.launch {
            state = state.copy(response = ServerResponse.LOADING)
            val result = userRepo.authenticate()
            resultChannel.send(result)
            initialState = result
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }
}