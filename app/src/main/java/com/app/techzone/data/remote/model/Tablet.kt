package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class Tablet(
    @SerializedName("accumulator_capacity")
    val accumulatorCapacity: Int,
    @SerializedName("accumulator_type")
    val accumulatorType: String,
    @SerializedName("average_rating")
    override val rating: Float?,
    @SerializedName("camera_quality")
    val cameraQuality: String,
    @SerializedName("color_main")
    override val colorMain: String,
    @SerializedName("color_variations")
    override val colorVariations: List<ColorVariation>,
    @SerializedName("color_other")
    val colorOther: String,
    @SerializedName("communicate_module")
    val communicateModule: Boolean,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("date_release")
    val dateRelease: String,
    @SerializedName("degree_protection")
    val degreeProtection: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("discount")
    override val discountPercentage: Int,
    val equipment: String,
    @SerializedName("fast_charge")
    val fastCharge: Boolean,
    @SerializedName("front_camera_quality")
    val frontCameraQuality: String,
    @SerializedName("headphone_output")
    val headphoneOutput: Boolean,
    val height: Double,
    override val id: Int,
    @SerializedName("id_provider")
    val idProvider: Int,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("is_favourite")
    override val isFavorite: Boolean,
    val material: String,
    @SerializedName("matrix_brightness")
    val matrixBrightness: String,
    @SerializedName("matrix_contrast")
    val matrixContrast: String,
    @SerializedName("matrix_frequency")
    val matrixFrequency: Int,
    @SerializedName("matrix_type")
    val matrixType: String,
    override val memory: Int,
    @SerializedName("memory_variations")
    override val memoryVariations: MemoryVariations,
    @SerializedName("memory_ram")
    val memoryRam: Int,
    val model: String,
    override val name: String,
    @SerializedName("number_cameras")
    val numberCameras: Int,
    @SerializedName("number_cores")
    val numberCores: Int,
    @SerializedName("operating_system")
    val operatingSystem: String,
    @SerializedName("optical_stabilization")
    val opticalStabilization: Boolean,
    override val photos: List<Photo>?,
    @SerializedName("pixel_density")
    val pixelDensity: Int,
    override val price: Int,
    @SerializedName("processor_frequency")
    val processorFrequency: Int,
    @SerializedName("processor_model")
    val processorModel: String,
    val quantity: Int,
    override val reviews: List<Review>,
    @SerializedName("reviews_count")
    override val reviewsCount: Int,
    @SerializedName("screen_diagonal")
    val screenDiagonal: String,
    @SerializedName("screen_format")
    val screenFormat: String,
    @SerializedName("screen_resolution")
    val screenResolution: String,
    @SerializedName("screen_type")
    val screenType: String,
    val sensors: String,
    @SerializedName("sim_card_format")
    val simCardFormat: String,
    @SerializedName("sound_technology")
    val soundTechnology: String,
    @SerializedName("support_lte")
    val supportLte: Boolean,
    val thickness: Double,
    override val type: String,
    @SerializedName("video_format")
    val videoFormat: String,
    val weight: Double,
    val width: Double,
    @SerializedName("is_in_cart")
    override val isInCart: Boolean,
): IDetailedProduct