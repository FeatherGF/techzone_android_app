package com.app.techzone.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token_access") val accessToken: String,
    @SerializedName("token_refresh") val refreshToken: String
)