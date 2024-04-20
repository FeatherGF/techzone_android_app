package com.app.techzone.model

import com.google.gson.annotations.SerializedName

data class AuthenticationRequest(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)
