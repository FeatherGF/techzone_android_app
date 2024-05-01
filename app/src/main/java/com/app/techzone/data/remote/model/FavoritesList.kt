package com.app.techzone.data.remote.model

import com.google.gson.annotations.SerializedName

data class FavoriteItem(
    @SerializedName("id_product")
    val productId: Int,
    @SerializedName("id_user")
    val userId: Int,
)

data class FavoritesList(
    val items: List<FavoriteItem>
)

data class AddFavoriteRequest(
    @SerializedName("id_product")
    val productId: Int,
)