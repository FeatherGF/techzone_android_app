package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("phone_number")
    val phoneNumber: String
)