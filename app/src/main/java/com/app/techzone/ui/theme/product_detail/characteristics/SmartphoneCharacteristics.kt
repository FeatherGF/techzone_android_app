package com.app.techzone.ui.theme.product_detail.characteristics

import com.app.techzone.utils.boolToString
import com.app.techzone.data.remote.model.Smartphone

data class SmartphoneCharacteristics(val smartphone: Smartphone) : ICharacteristicClass {
    inner class Common: ICharacteristic {
        override val label: String = "Заводские данные о товаре"
        override val items: Map<String, String> = mapOf(
            "Модель" to smartphone.model,
            "Код товара" to smartphone.id.toString(),
            "Дата выпуска" to smartphone.dateRelease,
            "Описание" to smartphone.description,
        )
    }

    inner class Main: ICharacteristic {
        override val label: String = "Основные характеристики"
        override val items: Map<String, String> = mapOf(
            "Цвет товара" to smartphone.colorMain,
            "Операционная система" to smartphone.operatingSystem,
            "Материал корпуса" to smartphone.material,
            "Дополнительный цвет товара" to smartphone.colorOther,
            "Степень защиты" to smartphone.degreeProtection,
            "Вес, кг" to smartphone.weight.toString(),
            "Комплектация" to smartphone.equipment,
            "Сенсоры" to smartphone.sensors,
        )
    }

    inner class Battery: ICharacteristic {
        override val label: String = "Питание"
        override val items: Map<String, String> = mapOf(
            "Быстрая зарядка (Fast-charge)" to boolToString(smartphone.fastCharge),
            "Тип батареи" to smartphone.accumulatorType,
            "Вместительность батареи" to smartphone.accumulatorCapacity.toString(),
        )
    }

    inner class MemoryAndProcessor: ICharacteristic {
        override val label: String = "Память и процессор"
        override val items: Map<String, String> = mapOf(
            "Оперативная память, в ГБ" to smartphone.memoryRam.toString(),
            "Встроенная память, в ГБ" to smartphone.memory.toString(),
            "Модель процессора" to smartphone.processorModel,
            "Частота процессора" to smartphone.processorFrequency.toString(),
            "Количество ядер процессора" to smartphone.numberCores.toString(),
        )
    }

    inner class Measurements: ICharacteristic {
        override val label: String = "Размеры"
        override val items = mapOf(
            "Высота, в сантиметрах" to smartphone.height.toString(),
            "Длина, в сантиметрах" to smartphone.width.toString(),
            "Толщина, в сантиметрах" to smartphone.thickness.toString(),
        )
    }

    inner class Display: ICharacteristic {
        override val label: String = "Экран"
        override val items: Map<String, String> = mapOf(
            "Разрешение экрана" to smartphone.screenResolution,
            "Диагональ экрана, в дюймах" to smartphone.screenDiagonal,
            "Тип экрана" to smartphone.matrixType,
            "Частота экрана" to smartphone.matrixFrequency.toString(),
            "Плотность пикселей" to smartphone.pixelDensity.toString(),
            "Яркость экрана" to smartphone.matrixBrightness,
        )
    }

    inner class Communication: ICharacteristic {
        override val label: String = "Связь"
        override val items: Map<String, String> = mapOf(
            "Поддержка Lte" to boolToString(smartphone.supportLte),
            "Стандарт коммуникации" to smartphone.communicationStandard,
            "Формат сим-карты" to smartphone.simCardFormat,
            "Количество сим-карт" to smartphone.simCardNumber,
            "Технология звука" to smartphone.soundTechnology,
            "Разьем для наущников" to boolToString(smartphone.headphoneOutput),
        )
    }

    inner class Camera: ICharacteristic {
        override val label: String = "Камера"
        override val items: Map<String, String> = mapOf(
            "Количество основных камер" to smartphone.numberCameras.toString(),
            "Разрешение основной камеры" to smartphone.cameraQuality,
            "Разрешение фронтальной камеры" to smartphone.frontCameraQuality,
            "Максимальное разрешение записи видео" to smartphone.videoFormat,
            "Стабилизация видео" to boolToString(smartphone.opticalStabilization),
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
