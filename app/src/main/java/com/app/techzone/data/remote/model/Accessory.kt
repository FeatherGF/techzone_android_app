package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class Accessory(
    @SerializedName("average_rating")
    override val rating: Float?,
    @SerializedName("color_main")
    val colorMain: String,
    @SerializedName("date_created")
    val dateCreated: String,
    val description: String,
    @SerializedName("discount")
    override val discountPercentage: Int,
    val equipment: String,
    val features: String,
    val height: Int,
    override val id: Int,
    @SerializedName("id_provider")
    val idProvider: Int,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("is_favourite")
    override val isFavorite: Boolean,
    val material: String,
    val model: String,
    override val name: String,
    override val photos: List<Photo>,
    override val price: Int,
    val quantity: Int,
    override val reviews: List<Review>,
    @SerializedName("reviews_count")
    override val reviewsCount: Int,
    val thickness: Int,
    override val type: String,
    val weight: Double,
    val width: Int
): IDetailedProduct