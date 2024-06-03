package com.app.techzone.data.remote.model


data class PaginationMeta(
    val objectsCount: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val objectsTotal: Int,
    val pagesTotal: Int,
)

data class ProductList<T>(
    val items: List<T>,
    val meta: PaginationMeta
)