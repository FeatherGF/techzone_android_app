package com.app.techzone.data.remote.api

import com.app.techzone.data.remote.model.Accessory
import com.app.techzone.data.remote.model.Banner
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.Filters
import com.app.techzone.data.remote.model.Laptop
import com.app.techzone.data.remote.model.ProductList
import com.app.techzone.data.remote.model.ProductType
import com.app.techzone.data.remote.model.Smartphone
import com.app.techzone.data.remote.model.Smartwatch
import com.app.techzone.data.remote.model.SuggestionsList
import com.app.techzone.data.remote.model.Tablet
import com.app.techzone.data.remote.model.Television
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ProductApi {

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.products)
    suspend fun getProducts(
        @Header("Authorization") token: String? = null,
        @Query("sort") sort: String? = null,
        @QueryMap(encoded = true) queryParams: Map<String, String>,
        @Query("size_page") pageSize: Int? = null,
        @Query("number_page") pageNumber: Int? = null,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.productType)
    suspend fun getProductType(
        @Path("id_product") productId: Int
    ): ProductType

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.banners)
    suspend fun getBanners(): ArrayList<Banner>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.productsFilters)
    suspend fun getFilters(@Query("model") category: String): Filters?

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.suggestions)
    suspend fun getSuggestions(@Query("query") query: String): SuggestionsList

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.search)
    suspend fun search(
        @Header("Authorization") token: String? = null,
        @Query("query") query: String,
        @Query("sort") sort: String? = null,
        @QueryMap(encoded = true) queryParams: Map<String, String>,
        @Query("size_page") pageSize: Int? = null,
        @Query("number_page") pageNumber: Int? = null,
    ): ProductList<BaseProduct>


    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.tablets)
    suspend fun getTablets(
        @Header("Authorization") token: String? = null,
        @Query("sort") sort: String? = null,
        @QueryMap(encoded = true) queryParams: Map<String, String>,
        @Query("size_page") pageSize: Int? = null,
        @Query("number_page") pageNumber: Int? = null,
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
        @Query("sort") sort: String? = null,
        @QueryMap(encoded = true) queryParams: Map<String, String>,
        @Query("size_page") pageSize: Int? = null,
        @Query("number_page") pageNumber: Int? = null,
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
        @Query("sort") sort: String? = null,
        @QueryMap(encoded = true) queryParams: Map<String, String>,
        @Query("size_page") pageSize: Int? = null,
        @Query("number_page") pageNumber: Int? = null,
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
        @Query("sort") sort: String? = null,
        @QueryMap(encoded = true) queryParams: Map<String, String>,
        @Query("size_page") pageSize: Int? = null,
        @Query("number_page") pageNumber: Int? = null,
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
        @Query("sort") sort: String? = null,
        @QueryMap(encoded = true) queryParams: Map<String, String>,
        @Query("size_page") pageSize: Int? = null,
        @Query("number_page") pageNumber: Int? = null,
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
        @Query("sort") sort: String? = null,
        @QueryMap(encoded = true) queryParams: Map<String, String>,
        @Query("size_page") pageSize: Int? = null,
        @Query("number_page") pageNumber: Int? = null,
    ): ProductList<BaseProduct>

    @Headers("Accept: application/json")
    @GET(ApiConstants.Endpoints.televisionsDetail)
    suspend fun getTelevision(
        @Header("Authorization") token: String? = null,
        @Path("id_television") televisionId: Int,
    ): Television
}