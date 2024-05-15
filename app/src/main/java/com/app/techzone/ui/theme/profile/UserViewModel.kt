package com.app.techzone.ui.theme.profile

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.AuthResult
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.Cart
import com.app.techzone.data.remote.model.FavoritesList
import com.app.techzone.data.remote.model.Order
import com.app.techzone.data.remote.model.OrderItem
import com.app.techzone.data.remote.model.User
import com.app.techzone.data.remote.repository.UserRepo
import com.app.techzone.ui.theme.profile.auth.AuthState
import com.app.techzone.ui.theme.profile.auth.AuthUiEvent
import com.app.techzone.ui.theme.server_response.ServerResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo: UserRepo,
): ViewModel() {

    private val resultChannel = Channel<AuthResult<Unit>>()
    var initialState: AuthResult<Unit> = AuthResult.Unauthorized()
    val authResults = resultChannel.receiveAsFlow()
    var state by mutableStateOf(AuthState())

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _favorites = MutableStateFlow(emptyList<BaseProduct>())
    val favorites = _favorites.asStateFlow()

    private val _cartItems = MutableStateFlow(emptyList<OrderItem>())
    val cartItems = _cartItems.asStateFlow()

    private val _orders = MutableStateFlow(emptyList<Order>())
    val orders = _orders.asStateFlow()

    fun logoutUser() {
        viewModelScope.launch {
            userRepo.logoutUser()
            _cartItems.update { emptyList() }
            _favorites.update { emptyList() }
            resultChannel.send(AuthResult.Unauthorized())
            initialState = AuthResult.Unauthorized()
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            val result: AuthResult<Unit> =
                if (userRepo.deleteUser()){
                    _cartItems.update { emptyList() }
                    _favorites.update { emptyList() }
                    AuthResult.Unauthorized()
                }
                else AuthResult.UnknownError()
            initialState = result
            resultChannel.send(result)
        }
    }

    suspend fun updateUser(
        imageFile: RequestBody? = null,
        firstName: String? = null,
        lastName: String? = null,
        phoneNumber: String? = null
    ) {
        state = state.copy(response = ServerResponse.LOADING)
        val result = userRepo.updateUser(
            imageFile, firstName, lastName, phoneNumber
        )
        initialState = result
        resultChannel.send(result)
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
            _user.update{ response }
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
                response.data?.let { data ->
                    _favorites.update {
                        data.items.map { it.product }
                    }
                }

                resultChannel.send(AuthResult.Authorized())
                state = state.copy(response = ServerResponse.SUCCESS)
            }
        }
    }

    private suspend fun addToFavorite(
        productId: Int,
        snackbarHostState: SnackbarHostState,
        navigateToFavorite: () -> Unit
    ): Boolean {
        val isSuccessful = userRepo.addToFavorite(productId)
        if (isSuccessful == null){
            snackbarHostState.showSnackbar(
                "Что-то пошло не так\nПроверьте подключение к интернету"
            )
            return false
        }
        if (!isSuccessful){
            snackbarHostState.showSnackbar(
                "Авторизуйтесь в приложение, чтобы добавлять товары в избранное"
            )
            return false
        }
        viewModelScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = "Товар добавлен в избранное",
                actionLabel = "Перейти",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed){
                navigateToFavorite()
            }
        }
        loadFavorites()
        return true
    }

    private suspend fun removeFromFavorite(
        productId: Int,
        snackbarHostState: SnackbarHostState,
        navigateToFavorite: () -> Unit
    ): Boolean {
        var isRemoved: Boolean
        val isSuccessful = userRepo.removeFromFavorite(productId)
        if (isSuccessful == null){
            snackbarHostState.showSnackbar(
                "Что-то пошло не так\nПроверьте подключение к интернету"
            )
            return false
        }
        isRemoved = true
        val result = snackbarHostState.showSnackbar(
            message = "Товар удален из избранного",
            actionLabel = "Отмена",
            duration = SnackbarDuration.Short
        )
        if (result == SnackbarResult.ActionPerformed){
            isRemoved = !addToFavorite(productId, snackbarHostState, navigateToFavorite)
        }
        loadFavorites()
        return isRemoved
    }

    fun loadCart() {
        state = state.copy(response = ServerResponse.LOADING)
        viewModelScope.launch {
            val response: AuthResult<Cart> = userRepo.getCart()
            when (response) {
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
            if (response is AuthResult.Authorized) {
                response.data?.let { cart ->
                    _cartItems.update{ cart.items }
                }
                resultChannel.send(AuthResult.Authorized())
                state = state.copy(response = ServerResponse.SUCCESS)
            }
        }
    }

    private suspend fun addToCart(productId: Int, snackbarHostState: SnackbarHostState): Boolean {
        val isSuccessful = userRepo.addToCart(productId)
        if (isSuccessful == null){
            snackbarHostState.showSnackbar(
                "Что-то пошло не так\nПроверьте подключение к интернету"
            )
            return false
        }
        if (!isSuccessful){
            snackbarHostState.showSnackbar(
                "Авторизуйтесь в приложение, чтобы добавлять товары в корзину"
            )
            return false
        }
        val cartResponse: AuthResult<Cart> = userRepo.getCart()
        if (cartResponse !is AuthResult.Authorized)
            return false
        cartResponse.data?.let { cart ->
            _cartItems.update { cart.items }
        }
        return true
    }

    private suspend fun removeFromCart(productId: Int, snackbarHostState: SnackbarHostState): Boolean {
        val isRemoved = userRepo.removeFromCart(productId)
        if (isRemoved == null){
            snackbarHostState.showSnackbar(
                "Что-то пошло не так\nПроверьте подключение к интернету"
            )
            return true
        }
        if (isRemoved) {
            _cartItems.update { listOrderItems ->
                listOrderItems.filter { it.product.id != productId }
            }
        }
        return !isRemoved
    }

    private suspend fun clearCart(): Boolean {
        return try {
            coroutineScope {
                _cartItems.value.map {
                    async {
                        userRepo.removeFromCart(it.product.id)
                    }
                }.awaitAll()
            }
            _cartItems.update { emptyList() }
            true
        } catch (e: CancellationException){
            // if coroutines were cancelled load cart to represent items
            // that weren't deleted
            val response: AuthResult<Cart> = userRepo.getCart()
            if(response is AuthResult.Authorized){
                response.data?.let { cart ->
                    _cartItems.update { cart.items }
                }
            }
            false
        }
    }

    private fun changeQuantityInCart(productId: Int, quantity: Int) {
        viewModelScope.launch {
            userRepo.changeQuantityInCart(productId, quantity)
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
            _orders.update { response.items }
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }

    suspend fun createOrder(orderItemIds: List<Int>, paymentMethod: String): Boolean {
        val order = userRepo.createOrder(orderItemIds, paymentMethod)
        return order != null
    }

    suspend fun onProductAction(action: ProductAction): Boolean {
        when (action){
            is ProductAction.AddToCart -> {
                return addToCart(action.productId, action.snackbarHostState)
            }
            is ProductAction.RemoveFromCart -> {
                return removeFromCart(action.productId, action.snackbarHostState)
            }

            is ProductAction.ChangeQuantityInCart -> {
                changeQuantityInCart(action.productId, action.quantity)
                return true
            }
            is ProductAction.ClearCart -> {
                return clearCart()
            }

            is ProductAction.AddToFavorites -> {
                return addToFavorite(
                    action.productId,
                    action.snackbarHostState,
                    action.navigateToFavorites
                )
            }
            is ProductAction.RemoveFromFavorites -> {
                return removeFromFavorite(
                    action.productId,
                    action.snackbarHostState,
                    action.navigateToFavorites
                )
            }
        }
    }

    fun onAuthEvent(event: AuthUiEvent) {
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

    suspend fun authenticate(): AuthResult<Unit>{
        val result = userRepo.authenticate()
        resultChannel.send(result)
        initialState = result
        return result
    }
}
