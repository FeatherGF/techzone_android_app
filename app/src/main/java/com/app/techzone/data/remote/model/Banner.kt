package com.app.techzone.data.remote.model

import com.google.gson.annotations.SerializedName

data class Banner(
    val link: String,
    @SerializedName("id_product") val productId: Int,
)
