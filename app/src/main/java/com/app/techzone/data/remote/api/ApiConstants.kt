package com.app.techzone.data.remote.api

object ApiConstants {
    const val BASE_URL = "http://wis-techzone.ru/api/v1/"

    object Endpoints {

        // products
        const val products = "products"
        const val product_type = "products/type/{id_product}"

        // search
        const val suggestions = "products/suggestions"
        const val search = "products/search"

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
        const val televisions = "televisions"
        const val televisionsDetail = "televisions/{id_television}"

        // user
        const val users = "users"

        // authentication
        const val sendAuthCode = "users/send-authentication-code"
        const val authorize = "users/authentication"
        const val authenticate = "users/valid-token"

        // favorites
        const val favorites = "favourites"
        const val favoritesDetail = "favourites/{id_product}"

        // orders
        const val orders = "orders"
        const val ordersDetail = "orders/{id_order}"

        // reviews
        const val reviewDetail = "reviews/{id_review}"
        const val addReview = "reviews/{id_product}"

        // cart
        const val cart = "cart"
        const val cartDetail = "cart/{id_product}"
    }

}