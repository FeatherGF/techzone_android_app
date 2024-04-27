package com.app.techzone.data.remote.model


import com.google.gson.annotations.SerializedName

data class Laptop(
    @SerializedName("discount")
    override val discountPercentage: Int,
    override val type: String,
    @SerializedName("average_rating")
    override val rating: Float?,
    @SerializedName("is_favourite")
    override val isFavorite: Boolean,
    override val photos: List<Photo>,
    override val reviews: List<Review>,
    @SerializedName("reviews_count")
    override val reviewsCount: Int,
    @SerializedName("battery_life")
    val batteryLife: Int,
    @SerializedName("clock_speed")
    val clockSpeed: Int,
    @SerializedName("color_main")
    val colorMain: String,
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
    @SerializedName("discrete_graphics")
    val discreteGraphics: String,
    @SerializedName("fingerprint_scanner")
    val fingerprintScanner: Boolean,
    @SerializedName("hdmi_ports")
    val hdmiPorts: Boolean,
    @SerializedName("hdr_support")
    val hdrSupport: Boolean,
    @SerializedName("headphone_output")
    val headphoneOutput: Boolean,
    override val id: Int,
    @SerializedName("id_provider")
    val idProvider: Int,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("keyboard_backlight")
    val keyboardBacklight: String,
    @SerializedName("keyboard_layout")
    val keyboardLayout: String,
    @SerializedName("length")
    val length: Int,
    @SerializedName("material")
    val material: String,
    @SerializedName("matrix_brightness")
    val matrixBrightness: String,
    @SerializedName("matrix_contrast")
    val matrixContrast: String,
    @SerializedName("matrix_frequency")
    val matrixFrequency: Int,
    val equipment: String,
    @SerializedName("matrix_type")
    val matrixType: String,
    @SerializedName("memory")
    val memory: Int,
    @SerializedName("memory_ram")
    val memoryRam: Int,
    @SerializedName("model")
    val model: String,
    override val name: String,
    @SerializedName("operating_system")
    val operatingSystem: String,
    override val price: Int,
    val quantity: Int,
    @SerializedName("screen_diagonal")
    val screenDiagonal: String,
    @SerializedName("screen_format")
    val screenFormat: String,
    @SerializedName("screen_resolution")
    val screenResolution: String,
    @SerializedName("screen_type")
    val screenType: String,
    @SerializedName("sound_power")
    val soundPower: String,
    @SerializedName("sound_technology")
    val soundTechnology: String,
    val touchpad: String,
    @SerializedName("type_graphics_accelerator")
    val typeGraphicsAccelerator: String,
    @SerializedName("usb_devices")
    val usbDevices: String,
    @SerializedName("video_card_model")
    val videoCardModel: String,
    @SerializedName("video_chip")
    val videoChip: String,
    @SerializedName("video_memory")
    val videoMemory: Int,
    @SerializedName("video_memory_type")
    val videoMemoryType: String,
    @SerializedName("voice_assistant")
    val voiceAssistant: String,
    val weight: Double,
    val height: Double,
    val width: Double,
    val thickness: Double,
    val microphone: Boolean,
    @SerializedName("wifi_availability")
    val wifiAvailability: Boolean,
    @SerializedName("wifi_standard")
    val wifiStandard: String
): IDetailedProduct