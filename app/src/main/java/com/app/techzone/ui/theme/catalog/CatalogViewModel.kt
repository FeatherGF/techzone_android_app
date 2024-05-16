package com.app.techzone.ui.theme.catalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.api.ApiConstants
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.repository.ProductRepo
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.ui.theme.server_response.ServerResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val productRepo: ProductRepo
) : ViewModel() {
    var activeScreenState by mutableStateOf(CatalogScreenEnum.DEFAULT)
    var state by mutableStateOf(ServerResponseState())

    private val _products = MutableStateFlow(emptyList<BaseProduct>())
    val products = _products.asStateFlow()

    fun loadByString(text: String) {
        if (text in listOf(
                ApiConstants.Endpoints.televisions,
                ApiConstants.Endpoints.laptops,
                ApiConstants.Endpoints.tablets,
                ApiConstants.Endpoints.smartphones,
                ApiConstants.Endpoints.smartwatches,
                ApiConstants.Endpoints.accessories
            )
        ) {
            loadByCategory(text)
        } else {
            loadBySearch(text)
        }
    }

    private fun loadBySearch(text: String) {
        viewModelScope.launch {
            state = state.copy(response = ServerResponse.LOADING)
            val response = productRepo.searchProducts(text)
            if (response == null){
                state = state.copy(response = ServerResponse.ERROR)
                return@launch
            }
            _products.update { response.items }
            state = state.copy(response = ServerResponse.SUCCESS)

        }
    }

    fun loadByCategory(category: String) {
        viewModelScope.launch {
            state = state.copy(response = ServerResponse.LOADING)
            val response = productRepo.getByCategoryOrAllProducts(category)
            if (response == null){
                state = state.copy(response = ServerResponse.ERROR)
                return@launch
            }
            _products.update { response.items }
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }

    fun updateActiveState(newValue: CatalogScreenEnum){
        activeScreenState = newValue
    }
}
