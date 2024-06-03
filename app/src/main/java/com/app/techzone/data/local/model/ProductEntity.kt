package com.app.techzone.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.techzone.data.remote.model.Photo

@Entity
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val pk: Int? = null,
    val id: Int,
    val name: String,
    val price: Int,
    val photos: List<Photo>?,
    val discountPercentage: Int,
    val reviewsCount: Int,
    val rating: Float?,
    val lastUpdated: Long,

    val sorting: String,

    val pageNumber: Int,
    val totalPages: Int,
)
