package com.app.techzone.data.api

import com.app.techzone.data.api.model.Photo
import retrofit2.http.GET


interface PhotosApi {
    @GET(ApiConstants.Endpoints.photos)
    suspend fun getPhotos(): List<Photo>
}