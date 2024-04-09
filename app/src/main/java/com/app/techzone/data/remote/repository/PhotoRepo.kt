package com.app.techzone.data.remote.repository

import com.app.techzone.data.remote.api.PhotosApi
import com.app.techzone.data.remote.model.Photo
import javax.inject.Inject


class PhotoRepo @Inject constructor(
    private val photosApi: PhotosApi
) {
    suspend fun getPhotos(): List<Photo>{
        return photosApi.getPhotos()
    }
}