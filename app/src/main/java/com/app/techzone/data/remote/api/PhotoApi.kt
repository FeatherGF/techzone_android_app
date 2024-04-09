package com.app.techzone.data.remote.api

import com.app.techzone.data.remote.model.Photo
import retrofit2.http.GET


interface PhotosApi {
    @GET(ApiConstants.Endpoints.photos)
    suspend fun getPhotos(): List<Photo>
}