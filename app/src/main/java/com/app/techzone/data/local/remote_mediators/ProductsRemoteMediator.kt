package com.app.techzone.data.local.remote_mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.app.techzone.data.local.ProductDatabase
import com.app.techzone.data.local.mappers.toEntity
import com.app.techzone.data.local.model.ProductEntity
import com.app.techzone.data.remote.repository.ProductRepo
import com.app.techzone.model.ProductTypeEnum
import com.app.techzone.model.SortingOptions
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class ProductsRemoteMediator(
    private val productDb: ProductDatabase,
    private val productRepo: ProductRepo,
    private val category: ProductTypeEnum,
    private val sorting: SortingOptions? = null
) : RemoteMediator<Int, ProductEntity>() {

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, ProductEntity>,
    ): ProductEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            productDb.productsDao.getKeys(sorting?.name?.lowercase() ?: "new")
                .firstOrNull { it.pageNumber != 1 }
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, ProductEntity>,
    ): ProductEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            productDb.productsDao.getKeys(sorting?.name?.lowercase() ?: "new")
                .sortedByDescending { it.pk }.firstOrNull { it.pageNumber != it.totalPages }
        }
    }

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return try {
            val pageNumber = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    getRemoteKeyForFirstItem(state)?.let {
                        it.pageNumber - 1
                    } ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                LoadType.APPEND -> {
                    getRemoteKeyForLastItem(state)?.let {
                        it.pageNumber + 1
                    } ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
            }
            val products = productRepo.getByCategoryOrAllProducts(
                category = category,
                pageSize = state.config.pageSize,
                pageNumber = pageNumber,
                sorting = sorting
            ) ?: return MediatorResult.Error(IOException())

            val endOfPaginationReached = products.meta.pageNumber == products.meta.pagesTotal

            productDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    productDb.productsDao.clearAll(sorting?.name?.lowercase() ?: "new")
                }
                val productEntities = products.items.map {
                    it.toEntity(
                        pageNumber = products.meta.pageNumber,
                        totalPages = products.meta.pagesTotal,
                        sorting = sorting?.name?.lowercase() ?: "new"
                    )
                }
                productDb.productsDao.upsertAll(productEntities)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}