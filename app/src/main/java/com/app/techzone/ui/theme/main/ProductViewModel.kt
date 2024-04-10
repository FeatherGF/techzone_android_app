package com.app.techzone.ui.theme.main

import androidx.lifecycle.ViewModel
import com.app.techzone.model.ProductCard
import com.app.techzone.model.newProducts as defaultNewProducts
import com.app.techzone.model.bestSellerProducts as defaultBestsellerProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel: ViewModel() {
    private val _newProducts = MutableStateFlow(defaultNewProducts)
    val newProducts: StateFlow<List<ProductCard>>
        get() = _newProducts

    private val _bestSellerProducts = MutableStateFlow(defaultBestsellerProducts)
    val bestSellerProducts: StateFlow<List<ProductCard>>
        get() = _bestSellerProducts

}