package com.app.techzone.ui.theme.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.IDetailedProduct
import com.app.techzone.data.remote.model.ProductTypeEnum
import com.app.techzone.data.remote.model.getProductType
import com.app.techzone.data.remote.repository.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepo: ProductRepo
): ViewModel() {
    private val _product = MutableStateFlow<IDetailedProduct?>(null)
    val product: StateFlow<IDetailedProduct?>
        get() = _product

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            val productType = productRepo.getProductType(productId)
            _product.value = when (getProductType(productType)){
                ProductTypeEnum.SMARTPHONE -> productRepo.getSmartphone(productId)
                ProductTypeEnum.LAPTOP -> productRepo.getLaptop(productId)
                else -> null
            }
        }
    }

}