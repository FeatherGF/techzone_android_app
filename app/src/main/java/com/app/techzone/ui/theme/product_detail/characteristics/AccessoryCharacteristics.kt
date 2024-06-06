package com.app.techzone.ui.theme.product_detail.characteristics

import com.app.techzone.data.remote.model.Accessory

data class AccessoryCharacteristics(val accessory: Accessory) : ICharacteristicClass {
    inner class Common : ICharacteristic {
        override val label: String = "Заводские данные о товаре"
        override val items: Map<String, String> = mapOf(
            "Модель" to accessory.model,
            "Код товара" to accessory.id.toString(),
            "Описание" to accessory.description,
        )
    }

    inner class Main : ICharacteristic {
        override val label: String = "Основные характеристики"
        override val items: Map<String, String> = mapOf(
            "Цвет товара" to accessory.colorMain,
            "Материал" to accessory.material,
            "Вес, кг" to accessory.weight.toString(),
            "Комплектация" to accessory.equipment,
            "Особенности" to accessory.features,
        )
    }

    inner class Measurements : ICharacteristic {
        override val label: String = "Размеры"
        override val items = mapOf(
            "Высота, в миллиметрах" to accessory.height.toString(),
            "Длина, в миллиметрах" to accessory.width.toString(),
            "Толщина, в миллиметрах" to accessory.thickness.toString(),
        )
    }

    override val allInfo: List<ICharacteristic> = listOf(
        Main(),
        Common(),
        Measurements(),

        )
}
