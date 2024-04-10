package com.app.techzone.ui.theme.favorite

import androidx.lifecycle.ViewModel
import com.app.techzone.model.ProductCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FavoriteViewModel: ViewModel() {
    private val _favorites = MutableStateFlow(emptyList<ProductCard>())
    val favorites: StateFlow<List<ProductCard>>
        get() = _favorites

    fun addToFavorite(newProductCard: ProductCard) {
        _favorites.update {
            productCards -> productCards + newProductCard
        }
    }

    fun removeFromFavorite(previousProductCard: ProductCard) {
        _favorites.update {
            productCards -> productCards - previousProductCard
        }
    }
}