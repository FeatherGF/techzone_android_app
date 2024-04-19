package com.app.techzone.ui.theme.product_detail.characteristics

import com.app.techzone.data.remote.model.Laptop

data class LaptopCharacteristics(val laptop: Laptop): ICharacteristicClass {
    inner class CommonInfo: ICharacteristic {
        override val label: String = "общ"
        override val items = mapOf(
            "" to laptop.batteryLife.toString()
        )
    }
    inner class MainInfo: ICharacteristic {
        override val label: String = "осн"
        override val items = mapOf(
            "" to laptop.batteryLife.toString()
        )
    }
    override val allInfo = listOf(
        CommonInfo(),
        MainInfo()
    )
}