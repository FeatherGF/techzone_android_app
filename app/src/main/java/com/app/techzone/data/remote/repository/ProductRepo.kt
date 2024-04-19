package com.app.techzone.data.remote.repository

import android.util.Log
import com.app.techzone.data.remote.api.ApiConstants
import com.app.techzone.data.remote.api.ProductApi
import com.app.techzone.data.remote.model.Accessory
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.Laptop
import com.app.techzone.data.remote.model.ProductList
import com.app.techzone.data.remote.model.ProductType
import com.app.techzone.data.remote.model.Smartphone
import com.app.techzone.data.remote.model.Smartwatch
import com.app.techzone.data.remote.model.Tablet
import java.io.IOException
import javax.inject.Inject


class ProductRepo @Inject constructor(
    private val productApi: ProductApi
) {

    suspend fun getProductType(productId: Int): ProductType {
        return productApi.getProductType(productId)
    }

    suspend fun getSmartphone(phoneId: Int): Smartphone? {
        return try {
            productApi.getSmartphone(phoneId)
        } catch (e: Exception) {
            Log.i("product is null", e.toString(), e)
            Log.d("SMARTPHONE", e.toString())
            null
        }
    }

    suspend fun getLaptop(laptopId: Int): Laptop? {
        return try {
            productApi.getLaptop(laptopId)
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getAccessory(accessoryId: Int): Accessory? {
        return try {
            productApi.getAccessory(accessoryId)
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getSmartwatch(watchId: Int): Smartwatch? {
        return try {
            productApi.getSmartwatch(watchId)
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getTablet(tabletId: Int): Tablet? {
        return try {
            productApi.getTablet(tabletId)
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getByCategoryOrAllProducts(
        category: String = ApiConstants.Endpoints.products,
        pageSize: Int = 20,
        pageNumber: Int = 1
    ): ProductList<BaseProduct>? {
        val categoryEndpoint = when(category) {
            ApiConstants.Endpoints.products -> productApi::getProducts
            ApiConstants.Endpoints.smartphones -> productApi::getSmartphones
            ApiConstants.Endpoints.smartwatches -> productApi::getSmartwatches
            ApiConstants.Endpoints.tablets -> productApi::getTablets
            ApiConstants.Endpoints.accessories -> productApi::getAccessories
            ApiConstants.Endpoints.laptops -> productApi::getLaptops
            else -> return null
        }
        return try {
            categoryEndpoint(pageSize, pageNumber)
        } catch (e: IOException) {
            null
        }
    }
}