package com.app.techzone.data.local.mappers

import com.app.techzone.data.local.model.ProductEntity
import com.app.techzone.data.remote.model.BaseProduct

fun BaseProduct.toEntity(
    pageNumber: Int,
    totalPages: Int,
    sorting: String,
) = ProductEntity(
    pk = null,
    id = id,
    name = name,
    photos = photos,
    discountPercentage = discountPercentage,
    reviewsCount = reviewsCount,
    price = price,
    rating = rating,
    lastUpdated = System.currentTimeMillis(),
    pageNumber = pageNumber,
    totalPages = totalPages,
    sorting = sorting
)

fun ProductEntity.toModel() = BaseProduct(
    id = id,
    name = name,
    photos = photos,
    discountPercentage = discountPercentage,
    reviewsCount = reviewsCount,
    price = price,
    rating = rating
)