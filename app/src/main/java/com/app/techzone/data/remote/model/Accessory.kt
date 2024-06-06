package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class Accessory(
    @SerializedName("average_rating") override val rating: Float?,
    @SerializedName("color_main") override val colorMain: String,
    @SerializedName("color_variations") override val colorVariations: List<ColorVariation>,
    @SerializedName("date_created") val dateCreated: String,
    val description: String,
    @SerializedName("discount") override val discountPercentage: Int,
    val equipment: String,
    val features: String,
    val height: Double,
    override val id: Int,
    @SerializedName("id_provider") val idProvider: Int,
    @SerializedName("is_active") override val isActive: Boolean,
    val material: String,
    val model: String,
    override val name: String,
    override val photos: List<Photo>?,
    override val price: Int,
    val quantity: Int,
    override val reviews: List<Review>,
    @SerializedName("reviews_count") override val reviewsCount: Int,
    val thickness: Double,
    override val type: String,
    val weight: Double,
    val width: Double,
    // accessories won't have any memory, can't think of a better approach
    override val memory: Int? = null,
    @SerializedName("memory_variations") override val memoryVariations: List<MemoryVariations>?,
) : IDetailedProduct