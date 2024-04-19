package com.app.techzone.ui.theme.catalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
class CatalogViewModel @Inject constructor(
    private val productRepo: ProductRepo
) : ViewModel() {
    var activeScreenState by mutableStateOf(CatalogScreenEnum.DEFAULT)

    private val _products = MutableStateFlow(ProductList(emptyList<BaseProduct>()))
    val products: StateFlow<ProductList<BaseProduct>>
        get() = _products

    fun loadByCategory(category: String) {
        viewModelScope.launch {
            _products.value = productRepo.getByCategoryOrAllProducts(category) ?: ProductList(emptyList())
        }
    }

    fun updateActiveState(newValue: CatalogScreenEnum){
        activeScreenState = newValue
    }
}
