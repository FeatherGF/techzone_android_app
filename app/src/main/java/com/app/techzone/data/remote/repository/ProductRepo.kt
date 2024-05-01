package com.app.techzone.data.remote.repository

import com.app.techzone.data.remote.api.ApiConstants
import com.app.techzone.data.remote.api.ProductApi
import com.app.techzone.data.remote.model.Accessory
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.IBaseProduct
import com.app.techzone.data.remote.model.Laptop
import com.app.techzone.data.remote.model.ProductList
import com.app.techzone.data.remote.model.ProductType
import com.app.techzone.data.remote.model.Smartphone
import com.app.techzone.data.remote.model.Smartwatch
import com.app.techzone.data.remote.model.Tablet
import com.app.techzone.data.remote.model.Television
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
        }
    }

    suspend fun getProductType(productId: Int): ProductType? {
        return try {
            productApi.getProductType(productId)
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getProduct(productId: Int): IBaseProduct? =
        callApiWithOptionalAccessToken { token ->
            productApi.getProductDetail(token = token, productId = productId)
        }

    suspend fun getSmartphone(phoneId: Int): Smartphone? = callApiWithOptionalAccessToken { token ->
        productApi.getSmartphone(token = token, phoneId = phoneId)
    }

    suspend fun getLaptop(laptopId: Int): Laptop? = callApiWithOptionalAccessToken { token ->
        productApi.getLaptop(token = token, laptopId = laptopId)
    }

    suspend fun getAccessory(accessoryId: Int): Accessory? =
        callApiWithOptionalAccessToken { token ->
            productApi.getAccessory(token = token, accessoryId = accessoryId)
        }

    suspend fun getSmartwatch(watchId: Int): Smartwatch? = callApiWithOptionalAccessToken { token ->
        productApi.getSmartwatch(token = token, watchId = watchId)
    }

    suspend fun getTablet(tabletId: Int): Tablet? = callApiWithOptionalAccessToken { token ->
        productApi.getTablet(token = token, tabletId = tabletId)
    }

    suspend fun getTelevision(televisionId: Int): Television? =
        callApiWithOptionalAccessToken { token ->
            productApi.getTelevision(token = token, televisionId = televisionId)
        }

    suspend fun getByCategoryOrAllProducts(
        category: String = ApiConstants.Endpoints.products,
        pageSize: Int = 20,
        pageNumber: Int = 1
    ): ProductList<BaseProduct>? {
        val categoryEndpoint = when (category) {
            ApiConstants.Endpoints.products -> productApi::getProducts
            ApiConstants.Endpoints.smartphones -> productApi::getSmartphones
            ApiConstants.Endpoints.smartwatches -> productApi::getSmartwatches
            ApiConstants.Endpoints.tablets -> productApi::getTablets
            ApiConstants.Endpoints.accessories -> productApi::getAccessories
            ApiConstants.Endpoints.laptops -> productApi::getLaptops
            ApiConstants.Endpoints.televisions -> productApi::getTelevisions
            else -> return null
        }
        return callApiWithOptionalAccessToken { token ->
            categoryEndpoint(token, pageSize, pageNumber)
        }
    }
}