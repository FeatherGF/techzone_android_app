package com.app.techzone.ui.theme.product_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.IDetailedProduct
import com.app.techzone.model.ProductTypeEnum
import com.app.techzone.model.getProductType
import com.app.techzone.data.remote.repository.ProductRepo
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.ui.theme.server_response.ServerResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepo: ProductRepo
): ViewModel() {
    var state by mutableStateOf(ServerResponseState())

    private val _product = MutableStateFlow<IDetailedProduct?>(null)
    val product: StateFlow<IDetailedProduct?>
        get() = _product

    fun loadProduct(productId: Int) {
        state = state.copy(response = ServerResponse.LOADING)
        viewModelScope.launch {
            val productType = productRepo.getProductType(productId)
            if (productType == null){
                state = state.copy(response = ServerResponse.ERROR)
                return@launch
            }
            val response = when (getProductType(productType)){
                ProductTypeEnum.SMARTPHONE -> productRepo.getSmartphone(productId)
                ProductTypeEnum.LAPTOP -> productRepo.getLaptop(productId)
                ProductTypeEnum.ACCESSORY -> productRepo.getAccessory(productId)
                ProductTypeEnum.TABLET -> productRepo.getTablet(productId)
                ProductTypeEnum.SMARTWATCH -> productRepo.getSmartwatch(productId)
                ProductTypeEnum.TELEVISION -> productRepo.getTelevision(productId)
                else -> return@launch
            }
            if (response == null) {
                state = state.copy(response = ServerResponse.ERROR)
                return@launch
            }
            _product.value = response
            state = state.copy(response = ServerResponse.SUCCESS)
        }
    }
}