package com.app.techzone.ui.theme.product_detail.characteristics

interface ICharacteristic {
    val label: String
    val items: Map<String, String?>
}

interface ICharacteristicClass {
    val allInfo: List<ICharacteristic>
}
