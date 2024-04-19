package com.app.techzone.ui.theme.product_detail.characteristics

import com.app.techzone.data.remote.model.Smartphone

data class SmartphoneCharacteristics(val smartphone: Smartphone) : ICharacteristicClass {
    inner class CommonInfo : ICharacteristic {
        override val label: String = "Заводские данные о товаре"
        override val items: Map<String, String> = mapOf(
            "Модель" to smartphone.model,
            "Код товара" to smartphone.id.toString(),
            "Дата выпуска" to smartphone.dateRelease,
        )
    }

    inner class MainInfo : ICharacteristic {
        override val label: String = "Основные характеристики"
        override val items: Map<String, String> = mapOf(
            "Операционная система" to smartphone.operatingSystem,
            "Диагональ экрана, в дюймах" to smartphone.screenDiagonal,
            "Оперативная память, в ГБ" to smartphone.memoryRam.toString(),
            "Встроенная память, в ГБ" to smartphone.memory.toString(),
            "Модель процессора" to smartphone.processorModel,
            "Частота процессора" to smartphone.processorFrequency.toString(),
            "Количество ядер процессора" to smartphone.numberCores.toString(),
            "Формат сим-карты" to smartphone.simCardFormat,
            "Количество основных камер" to smartphone.numberCameras.toString(),
        )
    }

    inner class Measurements : ICharacteristic {
        override val label: String = "Размеры и вес"
        override val items = mapOf(
            "Высота, в сантиметрах" to smartphone.width.toString(),
            "Длина, в сантиметрах" to smartphone.height.toString(),
            "Толщина, в сантиметрах" to smartphone.thickness.toString(),
        )
    }

    override val allInfo: List<ICharacteristic> = listOf(
        CommonInfo(),
        MainInfo(),
        Measurements()
    )
}
