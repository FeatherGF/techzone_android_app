package com.app.techzone.ui.theme.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.IBaseProduct
import com.app.techzone.data.remote.repository.ProductRepo
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.ui.theme.server_response.ServerResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepo: ProductRepo
): ViewModel() {
    var state by mutableStateOf(ServerResponseState())
    private val _allProducts = MutableStateFlow<List<IBaseProduct>>(emptyList())
    val allProducts: StateFlow<List<IBaseProduct>>
        get() = _allProducts

    fun loadMainProducts() {
        viewModelScope.launch {
            state = state.copy(response = ServerResponse.LOADING)
            val response = productRepo.getByCategoryOrAllProducts()
            if (response == null){
                state = state.copy(response = ServerResponse.ERROR)
                return@launch
            }
            _allProducts.update { response.items }
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }
}