package com.app.techzone.data.remote.model

import com.google.gson.annotations.SerializedName

data class PriceVariant(
    val label: String = "",
    val min: Int? = null,
    val max: Int? = null,
)

interface IPriceFilter {
    val id: String
    val label: String
    val min: String?
    val max: String?
    val variants: List<Any>
}

interface IProductFilter {
    val id: String
    val label: String
    val variants: List<Any>
}


interface IFilters {
    val productFilters: List<IProductFilter>?
    val price: IPriceFilter?
}

data class ProductFilter(
    override val id: String,
    override val label: String,
    override val variants: List<Any>
) : IProductFilter

data class PriceFilter(
    override val id: String,
    override val label: String,
    override val max: String?,
    override val min: String?,
    override val variants: List<Any>
) : IPriceFilter

data class Filters(
    override val price: PriceFilter?,
    @SerializedName("product_filters") override val productFilters: List<ProductFilter>?
) : IFilters

