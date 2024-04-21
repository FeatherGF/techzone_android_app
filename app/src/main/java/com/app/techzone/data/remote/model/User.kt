package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val email: String,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null
)

data class UserUpdateRequest(
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null
)