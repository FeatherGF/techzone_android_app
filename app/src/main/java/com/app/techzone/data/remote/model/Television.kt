package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class Television(
    @SerializedName("angle_view")
    val angleView: String,
    @SerializedName("codecs")
    val codecs: String,
    @SerializedName("color_main")
    override val colorMain: String,
    @SerializedName("color_variations")
    override val colorVariations: List<ColorVariation>,
    @SerializedName("color_other")
    val colorOther: String,
    @SerializedName("consumption")
    val consumption: Int,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("date_release")
    val dateRelease: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("discount")
    override val discountPercentage: Int,
    val equipment: String,
    @SerializedName("hdmi_ports")
    val hdmiPorts: Boolean,
    @SerializedName("hdmi_version")
    val hdmiVersion: Any,
    @SerializedName("hdr_support")
    val hdrSupport: Boolean,
    @SerializedName("headphone_output")
    val headphoneOutput: Boolean,
    val height: Double,
    override val id: Int,
    @SerializedName("id_provider")
    val idProvider: Int,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("management_application")
    val managementApplication: String,
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
    @SerializedName("operating_system")
    val operatingSystem: String,
    override val photos: List<Photo>?,
    override val price: Int,
    @SerializedName("quantity")
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
    @SerializedName("smartphone_control")
    val smartphoneControl: Boolean,
    @SerializedName("sound_power")
    val soundPower: String,
    @SerializedName("sound_surround")
    val surroundSound: Boolean,
    @SerializedName("sound_technology")
    val soundTechnology: String,
    val subwoofer: Boolean,
    val thickness: Double,
    override val type: String,
    @SerializedName("usb_ports")
    val usbPorts: String,
    @SerializedName("voice_assistant")
    val voiceAssistant: String,
    val weight: Double,
    val width: Double,
    @SerializedName("wifi_availability")
    val wifiAvailability: Boolean,
    @SerializedName("wifi_standard")
    val wifiStandard: String,
    @SerializedName("average_rating")
    override val rating: Float?,
    @SerializedName("is_favourite")
    override val isFavorite: Boolean,
    @SerializedName("bluetooth_control")
    val bluetoothControl: Boolean,
    @SerializedName("is_in_cart")
    override val isInCart: Boolean
): IDetailedProduct