package com.app.techzone.data.local.mappers

import com.app.techzone.data.local.model.ProductEntity
import com.app.techzone.data.remote.model.BaseProduct
import com.app.techzone.data.remote.model.PagingBaseProduct
import kotlin.random.Random

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
    sorting = sorting,
    isActive = isActive
)

fun ProductEntity.toModel() = PagingBaseProduct(
    pk = pk ?: Random.nextInt(),
    id = id,
    name = name,
    photos = photos,
    discountPercentage = discountPercentage,
    reviewsCount = reviewsCount,
    price = price,
    rating = rating,
    isActive = isActive
)