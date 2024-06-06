package com.app.techzone.ui.theme.product_detail.characteristics

import com.app.techzone.data.remote.model.Tablet
import com.app.techzone.utils.boolToString

data class TabletCharacteristics(val tablet: Tablet) : ICharacteristicClass {
    inner class Common : ICharacteristic {
        override val label: String = "Заводские данные о товаре"
        override val items: Map<String, String> = mapOf(
            "Модель" to tablet.model,
            "Код товара" to tablet.id.toString(),
            "Дата выпуска" to tablet.dateRelease,
            "Описание" to tablet.description,
        )
    }

    inner class Main : ICharacteristic {
        override val label: String = "Основные характеристики"
        override val items: Map<String, String> = mapOf(
            "Цвет товара" to tablet.colorMain,
            "Операционная система" to tablet.operatingSystem,
            "Материал корпуса" to tablet.material,
            "Дополнительный цвет товара" to tablet.colorOther,
            "Степень защиты" to tablet.degreeProtection,
            "Вес, кг" to tablet.weight.toString(),
            "Комплектация" to tablet.equipment,
            "Сенсоры" to tablet.sensors,
        )
    }

    inner class Battery : ICharacteristic {
        override val label: String = "Питание"
        override val items: Map<String, String> = mapOf(
            "Быстрая зарядка (Fast-charge)" to boolToString(tablet.fastCharge),
            "Тип батареи" to tablet.accumulatorType,
            "Вместительность батареи" to tablet.accumulatorCapacity.toString(),
        )
    }

    inner class Camera : ICharacteristic {
        override val label: String = "Камера"
        override val items: Map<String, String> = mapOf(
            "Количество основных камер" to tablet.numberCameras.toString(),
            "Разрешение основной камеры" to tablet.cameraQuality,
            "Разрешение фронтальной камеры" to tablet.frontCameraQuality,
            "Максимальное разрешение записи видео" to tablet.videoFormat,
            "Стабилизация видео" to boolToString(tablet.opticalStabilization),
        )
    }

    inner class Display : ICharacteristic {
        override val label: String = "Экран"
        override val items: Map<String, String> = mapOf(
            "Разрешение экрана" to tablet.screenResolution,
            "Диагональ экрана, в дюймах" to tablet.screenDiagonal,
            "Тип экрана" to tablet.matrixType,
            "Частота экрана" to tablet.matrixFrequency.toString(),
            "Плотность пикселей" to tablet.pixelDensity.toString(),
            "Яркость экрана" to tablet.matrixBrightness,
        )
    }

    inner class Communication : ICharacteristic {
        override val label: String = "Связь"
        override val items: Map<String, String> = mapOf(
            "Поддержка Lte" to boolToString(tablet.supportLte),
            "Формат сим-карты" to tablet.simCardFormat,
            "Модуль связи" to boolToString(tablet.communicateModule)
        )
    }

    inner class MemoryAndProcessor : ICharacteristic {
        override val label: String = "Память и процессор"
        override val items: Map<String, String> = mapOf(
            "Оперативная память, в ГБ" to tablet.memoryRam.toString(),
            "Встроенная память, в ГБ" to tablet.memory.toString(),
            "Модель процессора" to tablet.processorModel,
            "Частота процессора" to tablet.processorFrequency.toString(),
            "Количество ядер процессора" to tablet.numberCores.toString(),
        )
    }

    inner class Measurements : ICharacteristic {
        override val label: String = "Размеры"
        override val items = mapOf(
            "Высота, в миллиметрах" to tablet.height.toString(),
            "Длина, в миллиметрах" to tablet.width.toString(),
            "Толщина, в миллиметрах" to tablet.thickness.toString(),
        )
    }

    override val allInfo: List<ICharacteristic> = listOf(
        Main(),
        Common(),
        Display(),
        Battery(),
        MemoryAndProcessor(),
        Measurements(),
        Communication(),
        Camera(),
    )
}
