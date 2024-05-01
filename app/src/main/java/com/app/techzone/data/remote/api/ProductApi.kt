package com.app.techzone.data.remote.api

import com.app.techzone.data.remote.model.Accessory
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.Laptop
import com.app.techzone.data.remote.model.ProductList
import com.app.techzone.data.remote.model.ProductType
import com.app.techzone.data.remote.model.Smartphone
import com.app.techzone.data.remote.model.Smartwatch
import com.app.techzone.data.remote.model.Tablet
import com.app.techzone.data.remote.model.Television
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.products)
    suspend fun getProducts(
        @Header("Authorization") token: String? = null,
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.productsDetail)
    suspend fun getProductDetail(
        @Header("Authorization") token: String? = null,
        @Path("id_product") productId: Int
    ): BaseProduct

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.product_type)
    suspend fun getProductType(
        @Path("id_product") productId: Int
    ): ProductType


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.tablets)
    suspend fun getTablets(
        @Header("Authorization") token: String? = null,
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.tabletDetail)
    suspend fun getTablet(
        @Header("Authorization") token: String? = null,
        @Path("id_tablet") tabletId: Int,
    ): Tablet


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.smartphones)
    suspend fun getSmartphones(
        @Header("Authorization") token: String? = null,
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.smartphoneDetail)
    suspend fun getSmartphone(
        @Header("Authorization") token: String? = null,
        @Path("id_smartphone") phoneId: Int,
    ): Smartphone


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.accessories)
    suspend fun getAccessories(
        @Header("Authorization") token: String? = null,
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.accessoryDetail)
    suspend fun getAccessory(
        @Header("Authorization") token: String? = null,
        @Path("id_accessory") accessoryId: Int,
    ): Accessory


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.smartwatches)
    suspend fun getSmartwatches(
        @Header("Authorization") token: String? = null,
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.smartwatchDetail)
    suspend fun getSmartwatch(
        @Header("Authorization") token: String? = null,
        @Path("id_smartwatch") watchId: Int,
    ): Smartwatch


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.laptops)
    suspend fun getLaptops(
        @Header("Authorization") token: String? = null,
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.laptopDetail)
    suspend fun getLaptop(
        @Header("Authorization") token: String? = null,
        @Path("id_laptop") laptopId: Int,
    ): Laptop


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.televisions)
    suspend fun getTelevisions(
        @Header("Authorization") token: String? = null,
        @Query("size_page") pageSize: Int = 20,
        @Query("number_page") pageNumber: Int = 1,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.televisionsDetail)
    suspend fun getTelevision(
        @Header("Authorization") token: String? = null,
        @Path("id_television") televisionId: Int,
    ): Television

}