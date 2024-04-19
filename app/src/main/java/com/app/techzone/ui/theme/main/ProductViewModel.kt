package com.app.techzone.ui.theme.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.ProductList
import com.app.techzone.data.remote.repository.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepo: ProductRepo
): ViewModel() {
    private val _allProducts = MutableStateFlow(ProductList(items = emptyList<BaseProduct>()))
    val allProducts: StateFlow<ProductList<BaseProduct>>
        get() = _allProducts

    init {
        viewModelScope.launch {
            val products =
                productRepo.getByCategoryOrAllProducts() ?: ProductList(items = emptyList())
            _allProducts.value = products
        }
    }

//    private val _newProducts = MutableStateFlow(defaultNewProducts)
//    val newProducts: StateFlow<List<ProductCard>>
//        get() = _newProducts
//
//    private val _bestSellerProducts = MutableStateFlow(defaultBestsellerProducts)
//    val bestSellerProducts: StateFlow<List<ProductCard>>
//        get() = _bestSellerProducts

}