package com.app.techzone.ui.theme.catalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.IPriceFilter
import com.app.techzone.data.remote.model.IProductFilter
import com.app.techzone.data.remote.model.PriceVariant
import com.app.techzone.model.ProductTypeEnum
import com.app.techzone.model.getProductType
import com.app.techzone.data.remote.repository.ProductRepo
import com.app.techzone.model.sortingOptions
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

    private val _productFilters = MutableStateFlow<List<IProductFilter>>(emptyList())
    val productFilters = _productFilters.asStateFlow()

    private val _priceFilters = MutableStateFlow<IPriceFilter?>(null)
    val priceFilters = _priceFilters.asStateFlow()

    val mutableSelectedFilters = MutableStateFlow(mutableMapOf<String, MutableList<Any>>())
    val selectedFilters = mutableSelectedFilters.asStateFlow()

    val selectedPriceRanges = mutableStateListOf<PriceVariant>()
    val selectedSorting = mutableStateOf(sortingOptions.first())

    fun loadByString(text: String) {
        val productType = getProductType(text)
        if (productType != ProductTypeEnum.PRODUCT) {
            loadByCategory(productType)
        } else {
            loadBySearch(text)
        }
        loadFilters(productType)
    }

    fun clearFilters() {
        mutableSelectedFilters.update {
            it.apply {
                values.forEach { list -> list.clear() }
            }
        }
        selectedPriceRanges.clear()
        selectedSorting.value = sortingOptions.first()
    }

    private fun getQueryFilters(): Map<String, String> {
        val filters = selectedFilters.value.map { (queryName, queryParams)  ->
            if (queryParams.isEmpty()) return@map "" to ""
            "${queryName}_in" to queryParams.joinToString(prefix = "[", postfix = "]") { param ->
                val parameter = param.toString()
                parameter.toDoubleOrNull()?.let{
                    return@joinToString it.toString()
                }
                parameter.toIntOrNull()?.let {
                    return@joinToString it.toString()
                }
                "\"$parameter\""
            }
        }.toMap()
        val priceFilters = mutableMapOf<String, String>().apply {
            selectedPriceRanges
                .sortedWith(compareBy(nullsFirst()) { it.min })
                .firstOrNull()?.min
                ?.let { put("price_gte", it.toString()) }
            selectedPriceRanges
                .sortedWith(compareByDescending(nullsLast()) { it.max })
                .firstOrNull()?.max
                ?.let { put("price_lte", it.toString()) }
        }
        return priceFilters + filters
    }

    private fun loadFilters(type: ProductTypeEnum) {
        viewModelScope.launch {
            val workaround = if (type == ProductTypeEnum.ACCESSORY) ProductTypeEnum.PRODUCT else type
            productRepo.getFilters(workaround.name.lowercase())?.let { filters ->
                _productFilters.update {
                    filters.productFilters ?: emptyList()
                }
                _priceFilters.update { filters.price }
            }
        }
    }

    private fun loadBySearch(text: String) {
        viewModelScope.launch {
            state = state.copy(response = ServerResponse.LOADING)
            val response = productRepo.searchProducts(
                text = text,
                sorting = selectedSorting.value.queryName,
                queryFilters = getQueryFilters()
            )
            if (response == null){
                state = state.copy(response = ServerResponse.ERROR)
                return@launch
            }
            _products.update { response.items }
            state = state.copy(response = ServerResponse.SUCCESS)

        }
    }

    private fun loadByCategory(categoryType: ProductTypeEnum) {
        viewModelScope.launch {
            state = state.copy(response = ServerResponse.LOADING)
            val response = productRepo.getByCategoryOrAllProducts(
                categoryType,
                sorting = selectedSorting.value.queryName,
                queryFilters = getQueryFilters(),
            )
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

