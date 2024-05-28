package com.app.techzone.data.remote.model

interface IFilter {
    val id: String
    val variants: List<Any>
}

data class PriceVariant(
    val label: String = "",
    val min: Int? = null,
    val max: Int? = null,
)

interface IPriceFilters : IFilter {
    override val id: String
    val min: String?
    val max: String?
    override val variants: List<Any>
}

data class PriceFilter(
    override val id: String,
    override val max: String?,
    override val min: String?,
    override val variants: List<Any>
) : IPriceFilters

typealias FiltersType = Map<String, PriceFilter>
