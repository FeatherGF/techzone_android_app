package com.app.techzone.data.remote.api

object ApiConstants {
    const val BASE_URL = "http://wis-techzone.ru/api/v1/"

    object Endpoints {

        // products
        const val products = "products"
        const val product_type = "products/type/{id_product}"

        const val smartphones = "smartphones"
        const val smartphoneDetail = "smartphones/{id_smartphone}"
        const val tablets = "tablets"
        const val tabletDetail = "tablets/{id_tablet}"
        const val accessories = "accessories"
        const val accessoryDetail = "accessories/{id_accessory}"
        const val laptops = "laptops"
        const val laptopDetail = "laptops/{id_laptop}"
        const val smartwatches = "smartwatches"
        const val smartwatchDetail = "smartwatches/{id_smartwatch}"

        // user
        const val userDetail = "users/{id_user}"

        // authentication
        const val sendAuthCode = "users/send-authentication-code"
        const val authorize = "users/authentication"
        const val authenticate = "users/valid-token"

    }

}