package com.app.techzone.ui.theme.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.app.techzone.data.local.remote_mediators.ProductsRemoteMediator
import com.app.techzone.data.local.ProductDatabase
import com.app.techzone.data.local.mappers.toModel
import com.app.techzone.data.remote.model.Banner
import com.app.techzone.data.remote.model.PagingBaseProduct
import com.app.techzone.data.remote.repository.ProductRepo
import com.app.techzone.model.ProductTypeEnum
import com.app.techzone.model.SortingOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepo: ProductRepo,
    private val localProductDb: ProductDatabase,
) : ViewModel() {
    private val _banners = MutableStateFlow<List<Banner>>(emptyList())
    val banners = _banners.asStateFlow()

    suspend fun loadBanners() {
        productRepo.getBanners()?.let {
            _banners.value = it
        }
    }

    val productPagingFlow: Flow<PagingData<PagingBaseProduct>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 5, initialLoadSize = 4),
        remoteMediator = ProductsRemoteMediator(
            productDb = localProductDb,
            productRepo = productRepo,
            category = ProductTypeEnum.PRODUCT,
        ),
        pagingSourceFactory = {
            localProductDb.productsDao.pagingSource("new")
        }
    )
        .flow
        .map { pagingData ->
            pagingData.map { it.toModel() }
        }
        .cachedIn(viewModelScope)

    val popularProductsPagingFlow: Flow<PagingData<PagingBaseProduct>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 5, initialLoadSize = 4),
        remoteMediator = ProductsRemoteMediator(
            productDb = localProductDb,
            productRepo = productRepo,
            category = ProductTypeEnum.PRODUCT,
            sorting = SortingOptions.POPULAR
        ),
        pagingSourceFactory = {
            localProductDb.productsDao.pagingSource(SortingOptions.POPULAR.name.lowercase())
        }
    )
        .flow
        .map { pagingData ->
            pagingData.map { it.toModel() }
        }
        .cachedIn(viewModelScope)

}