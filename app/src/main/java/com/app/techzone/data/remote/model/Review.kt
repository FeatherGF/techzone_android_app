package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("photo_url")
    val photoUrl: String?,
    val rating: Int,
    val text: String,
    val user: String
)