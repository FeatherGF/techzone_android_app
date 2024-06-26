package com.app.techzone.ui.theme.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.model.Sorting
import com.app.techzone.ui.theme.app_bars.SearchTopBarState
import com.app.techzone.ui.theme.dimension
import com.app.techzone.ui.theme.profile.CheckProductStatus
import com.app.techzone.ui.theme.profile.LoadingBox
import com.app.techzone.ui.theme.profile.ProductAction
import com.app.techzone.ui.theme.reusables.WideProductCard
import com.app.techzone.ui.theme.server_response.ErrorScreen
import com.app.techzone.ui.theme.server_response.ServerResponse
import com.app.techzone.ui.theme.server_response.ServerResponseState


@Composable
fun CatalogCategoryScreen(
    searchText: String,
    catalogViewModel: CatalogViewModel,
    onChangeView: (SearchTopBarState) -> Unit,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
) {
    val products by catalogViewModel.products.collectAsStateWithLifecycle()
    val priceFilters by catalogViewModel.priceFilters.collectAsState()
    val productFilters by catalogViewModel.productFilters.collectAsState()
    val selectedFilters by catalogViewModel.selectedFilters.collectAsState()
    val selectedPriceRanges = catalogViewModel.selectedPriceRanges
    val (selectedSorting, onSortingSelected) = remember { catalogViewModel.selectedSorting }

    LaunchedEffect(selectedSorting) {
        catalogViewModel.loadByString(searchText)
    }
    LaunchedEffect(searchText) {
        catalogViewModel.clearFilters()
        catalogViewModel.loadByString(searchText)
    }

    when (catalogViewModel.activeScreenState) {
        CatalogScreenEnum.DEFAULT -> {
            onChangeView(SearchTopBarState.CATALOG_OPENED)
            DefaultCatalogView(products = products,
                state = catalogViewModel.state,
                onProductCheckStatus = onProductCheckStatus,
                onProductAction = onProductAction,
                onChangeView = catalogViewModel::updateActiveState,
                selectedSorting = selectedSorting,
                onSortingSelected = onSortingSelected,
                onRefreshSearch = {
                    catalogViewModel.loadByString(searchText)
                }
            )
        }

        CatalogScreenEnum.FILTERS -> {
            onChangeView(SearchTopBarState.HIDDEN)
            FiltersView(
                priceFilters = priceFilters,
                productFilters = productFilters,
                selectedPriceRanges = selectedPriceRanges,
                mutableSelectedFilters = catalogViewModel.mutableSelectedFilters,
                selectedFilters = selectedFilters,
                clearFilters = catalogViewModel::clearFilters,
                onFiltersApplied = {
                    catalogViewModel.loadByString(searchText)
                },
                onBackClicked = {
                    catalogViewModel.updateActiveState(CatalogScreenEnum.DEFAULT)
                },
            )
        }
    }
}


@Composable
fun DefaultCatalogView(
    products: List<BaseProduct>,
    state: ServerResponseState,
    onRefreshSearch: () -> Unit,
    onChangeView: (CatalogScreenEnum) -> Unit,
    onProductCheckStatus: (CheckProductStatus) -> Boolean,
    onProductAction: suspend (ProductAction) -> Boolean,
    selectedSorting: Sorting,
    onSortingSelected: (Sorting) -> Unit
) {
    when (state.response) {
        ServerResponse.LOADING -> {
            LoadingBox()
        }

        ServerResponse.ERROR -> {
            ErrorScreen(onRefreshSearch)
        }

        else -> {
            LazyColumn(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(
                    start = MaterialTheme.dimension.extendedMedium,
                    top = MaterialTheme.dimension.extendedMedium,
                    end = MaterialTheme.dimension.extendedMedium,
                    bottom = MaterialTheme.dimension.extraLarge
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.small)
            ) {
                item {
                    FiltersAndSorting(selectedSorting, onSortingSelected) {
                        onChangeView(CatalogScreenEnum.FILTERS)
                    }
                }
                items(products, key = { it.id }, contentType = { it::class }) { product ->
                    WideProductCard(
                        product = product,
                        onProductCheckStatus = onProductCheckStatus,
                        onProductAction = onProductAction
                    )
                }
            }
        }
    }
}
