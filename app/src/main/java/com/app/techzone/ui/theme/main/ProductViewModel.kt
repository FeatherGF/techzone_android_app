package com.app.techzone.ui.theme.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.IBaseProduct
import com.app.techzone.model.ProductTypeEnum
import com.app.techzone.data.remote.repository.ProductRepo
import com.app.techzone.model.SortingOptions
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.ui.theme.server_response.ServerResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepo: ProductRepo
): ViewModel() {
    var state by mutableStateOf(ServerResponseState())

    private val _popularProducts = MutableStateFlow<List<IBaseProduct>>(emptyList())
    val popularProducts = _popularProducts.asStateFlow()

    private val _newProducts = MutableStateFlow<List<IBaseProduct>>(emptyList())
    val newProducts = _newProducts.asStateFlow()

    fun loadBestSellerProducts() {
        viewModelScope.launch {
            state = state.copy(response = ServerResponse.LOADING)
            val response = productRepo.getByCategoryOrAllProducts(
                ProductTypeEnum.PRODUCT,
                sorting = SortingOptions.POPULAR,
                pageSize = 10
            )
            if (response == null){
                state = state.copy(response = ServerResponse.ERROR)
                return@launch
            }
            _popularProducts.update { response.items }
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }

    fun loadNewProducts() {
        viewModelScope.launch {
            state = state.copy(response = ServerResponse.LOADING)
            val response = productRepo.getByCategoryOrAllProducts(
                ProductTypeEnum.PRODUCT,
                pageSize = 10
            )
            if (response == null){
                state = state.copy(response = ServerResponse.ERROR)
                return@launch
            }
            _newProducts.update { response.items }
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }

}