package com.app.techzone.data.remote.repository

import com.app.techzone.data.remote.api.ProductApi
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.IFilters
import com.app.techzone.data.remote.model.ProductList
import com.app.techzone.data.remote.model.ProductType
import com.app.techzone.model.ProductTypeEnum
import com.app.techzone.model.SortingOptions
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class ProductRepo @Inject constructor(
    private val productApi: ProductApi,
    private val prefs: EncryptedSharedPreferencesImpl,
) {
    private suspend fun <T> callApiWithOptionalAccessToken(apiCall: suspend (token: String?) -> T): T? {
        val accessToken = prefs.getKey(PreferencesKey.accessToken)
        return try {
            apiCall(accessToken)
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            null
        }
    }

    suspend fun getProductType(productId: Int): ProductType? {
        return try {
            productApi.getProductType(productId)
        } catch (e: IOException) {
            null
        } catch (e: HttpException) {
            null
        }
    }

    suspend fun getFilters(category: String): IFilters? {
        return try {
            productApi.getFilters(category)
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getSmartphone(phoneId: Int) = callApiWithOptionalAccessToken { token ->
        productApi.getSmartphone(token = token, phoneId = phoneId)
    }

    suspend fun getLaptop(laptopId: Int) = callApiWithOptionalAccessToken { token ->
        productApi.getLaptop(token = token, laptopId = laptopId)
    }

    suspend fun getAccessory(accessoryId: Int) = callApiWithOptionalAccessToken { token ->
        productApi.getAccessory(token = token, accessoryId = accessoryId)
    }

    suspend fun getSmartwatch(watchId: Int) = callApiWithOptionalAccessToken { token ->
        productApi.getSmartwatch(token = token, watchId = watchId)
    }

    suspend fun getTablet(tabletId: Int) = callApiWithOptionalAccessToken { token ->
        productApi.getTablet(token = token, tabletId = tabletId)
    }

    suspend fun getTelevision(televisionId: Int) = callApiWithOptionalAccessToken { token ->
        productApi.getTelevision(token = token, televisionId = televisionId)
    }

    suspend fun getByCategoryOrAllProducts(
        category: ProductTypeEnum,
        sorting: SortingOptions? = null,
        queryFilters: Map<String, String> = mapOf(),
        pageSize: Int? = null,
        pageNumber: Int? = null,
    ): ProductList<BaseProduct>? {
        val categoryEndpoint = when (category) {
            ProductTypeEnum.PRODUCT -> productApi::getProducts
            ProductTypeEnum.SMARTPHONE -> productApi::getSmartphones
            ProductTypeEnum.SMARTWATCH -> productApi::getSmartwatches
            ProductTypeEnum.TABLET -> productApi::getTablets
            ProductTypeEnum.ACCESSORY -> productApi::getAccessories
            ProductTypeEnum.LAPTOP -> productApi::getLaptops
            ProductTypeEnum.TELEVISION -> productApi::getTelevisions
        }
        return callApiWithOptionalAccessToken { token ->
            categoryEndpoint(token, sorting?.name?.lowercase(), queryFilters, pageSize, pageNumber)
        }
    }

    suspend fun getSuggestions(text: String): List<String> {
        return try {
            productApi.getSuggestions(text).suggestions
        } catch (e: IOException){
            emptyList()
        }
    }

    suspend fun searchProducts(
        text: String,
        sorting: SortingOptions,
        queryFilters: Map<String, String>,
        pageSize: Int? = null,
        pageNumber: Int? = null,
    ) = callApiWithOptionalAccessToken { token ->
        productApi.search(
            token = token,
            query = text,
            queryParams = queryFilters,
            sort = sorting.name.lowercase(),
            pageSize = pageSize,
            pageNumber = pageNumber
        )
    }
}