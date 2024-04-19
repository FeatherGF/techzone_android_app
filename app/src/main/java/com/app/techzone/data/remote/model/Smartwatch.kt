package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class Smartwatch(
    @SerializedName("accumulator_capacity")
    val accumulatorCapacity: Int,
    @SerializedName("accumulator_type")
    val accumulatorType: String,
    @SerializedName("color_main")
    val colorMain: String,
    @SerializedName("color_other")
    val colorOther: String,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("date_release")
    val dateRelease: String,
    @SerializedName("degree_protection")
    val degreeProtection: String,
    @SerializedName("description")
    val description: Any,
    @SerializedName("fast_charge")
    val fastCharge: Boolean,
    @SerializedName("headphone_output")
    val headphoneOutput: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("id_provider")
    val idProvider: Int,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("length")
    val length: Int,
    @SerializedName("material")
    val material: String,
    @SerializedName("material_belt")
    val materialBelt: String,
    @SerializedName("matrix_brightness")
    val matrixBrightness: String,
    @SerializedName("matrix_contrast")
    val matrixContrast: String,
    @SerializedName("matrix_frequency")
    val matrixFrequency: Int,
    @SerializedName("matrix_type")
    val matrixType: String,
    @SerializedName("memory")
    val memory: Int,
    @SerializedName("memory_ram")
    val memoryRam: Int,
    @SerializedName("model")
    val model: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("operating_system")
    val operatingSystem: String,
    @SerializedName("pixel_density")
    val pixelDensity: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("screen_diagonal")
    val screenDiagonal: String,
    @SerializedName("screen_format")
    val screenFormat: String,
    @SerializedName("screen_resolution")
    val screenResolution: String,
    @SerializedName("screen_type")
    val screenType: String,
    @SerializedName("sound_technology")
    val soundTechnology: String,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("width")
    val width: Int
)