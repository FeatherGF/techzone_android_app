package com.app.techzone.data.remote.api

import com.app.techzone.data.remote.model.Accessory
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.Laptop
import com.app.techzone.data.remote.model.ProductList
import com.app.techzone.data.remote.model.ProductType
import com.app.techzone.data.remote.model.Smartphone
import com.app.techzone.data.remote.model.Smartwatch
import com.app.techzone.data.remote.model.Tablet
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.products)
    suspend fun getProducts(
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.product_type)
    suspend fun getProductType(
        @Path("id_product") productId: Int
    ): ProductType


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.tablets)
    suspend fun getTablets(
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.tabletDetail)
    suspend fun getTablet(
        @Path("id_tablet") tabletId: Int,
    ): Tablet


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.smartphones)
    suspend fun getSmartphones(
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.smartphoneDetail)
    suspend fun getSmartphone(
        @Path("id_smartphone") phoneId: Int,
    ): Smartphone


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.accessories)
    suspend fun getAccessories(
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.accessoryDetail)
    suspend fun getAccessory(
        @Path("id_accessory") accessoryId: Int,
    ): Accessory


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.smartwatches)
    suspend fun getSmartwatches(
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.smartwatchDetail)
    suspend fun getSmartwatch(
        @Path("id_smartwatch") watchId: Int,
    ): Smartwatch


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.laptops)
    suspend fun getLaptops(
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.laptopDetail)
    suspend fun getLaptop(
        @Path("id_laptop") laptopId: Int,
    ): Laptop

}