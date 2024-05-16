package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class Review(
    val id: Int,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("photo_url")
    val photoUrl: String?,
    val rating: Int,
    val text: String,
    val user: String
)


data class AddReviewRequest(
    val text: String? = null,
    val rating: Int
)