package com.app.techzone.data.repository

import com.app.techzone.data.api.PhotosApi
import com.app.techzone.data.api.model.Photo
import javax.inject.Inject


class PhotoRepo @Inject constructor(
    private val photosApi: PhotosApi
) {
    suspend fun getPhotos(): List<Photo>{
        return photosApi.getPhotos()
    }
}