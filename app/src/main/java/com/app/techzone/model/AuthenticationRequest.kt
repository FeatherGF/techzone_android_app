package com.app.techzone.model

import com.google.gson.annotations.SerializedName

data class AuthenticationRequest(
    @SerializedName("token_access") val accessToken: String? = null,
    @SerializedName("token_refresh") val refreshToken: String
)
