package com.app.techzone.ui.theme.product_detail.characteristics

import com.app.techzone.data.remote.model.Laptop
import com.app.techzone.utils.boolToString

data class LaptopCharacteristics(val laptop: Laptop) : ICharacteristicClass {
    inner class Common : ICharacteristic {
        override val label: String = "Заводские данные о товаре"
        override val items = mapOf(
            "Модель" to laptop.model,
            "Код товара" to laptop.id.toString(),
            "Дата выпуска" to laptop.dateRelease,
            "Описание" to laptop.description,
        )
    }

    inner class Main : ICharacteristic {
        override val label: String = "Основные характеристики"
        override val items = mapOf(
            "Цвет товара" to laptop.colorMain,
            "Операционная система" to laptop.operatingSystem,
            "Материал корпуса" to laptop.material,
            "Дополнительный цвет товара" to laptop.colorOther,
            "Тип корпуса" to laptop.material,
            "Вес, кг" to laptop.weight.toString(),
            "Максимальное время работы, ч" to laptop.batteryLife.toString(),
            "Потребление, Вт" to laptop.consumption.toString(),
            "Комплектация" to laptop.equipment,
        )
    }

    inner class Measurements : ICharacteristic {
        override val label: String = "Размеры"
        override val items = mapOf(
            "Высота, в миллиметрах" to laptop.height.toString(),
            "Длина, в миллиметрах" to laptop.width.toString(),
            "Толщина, в миллиметрах" to laptop.thickness.toString(),
        )
    }

    inner class Memory : ICharacteristic {
        override val label: String = "Память"
        override val items = mapOf(
            "Встроенная память, в ГБ" to laptop.memory.toString(),
            "Оперативная память, в ГБ" to laptop.memoryRam.toString(),
        )
    }

    inner class InputDevices : ICharacteristic {
        override val label: String = "Устройства ввода"
        override val items = mapOf(
            "Раскладка клавиатуры" to laptop.keyboardLayout,
            "Подсветка клавиатуры" to laptop.keyboardBacklight,
            "Микрофон" to boolToString(laptop.microphone),
            "Голосовой ассистент" to laptop.voiceAssistant,
            "HDMI порты" to boolToString(laptop.hdmiPorts),
            "WiFi" to boolToString(laptop.wifiAvailability),
            "Стандарт WiFi" to laptop.wifiStandard,
            "Отпечаток пальца" to boolToString(laptop.fingerprintScanner),
            "Тачпад" to laptop.touchpad,
            "Разьем для наушников" to boolToString(laptop.headphoneOutput),
            "Технология звука" to laptop.soundTechnology,
            "Мощность звука" to laptop.soundPower,
        )
    }

    inner class Display : ICharacteristic {
        override val label: String = "Экран"
        override val items = mapOf(
            "Разрешение экрана" to laptop.screenResolution,
            "Диагональ экрана, в дюймах" to laptop.screenDiagonal,
            "Тип экрана" to laptop.matrixType,
            "Частота экрана" to laptop.matrixFrequency.toString(),
            "Яркость экрана" to laptop.matrixBrightness,
            "Поддержка HDR" to boolToString(laptop.hdrSupport)
        )
    }

    inner class GraphicAccelerator : ICharacteristic {
        override val label: String = "Графический ускоритель"
        override val items = mapOf(
            "Вид графического ускорителя" to laptop.typeGraphicsAccelerator,
            "Модель видеокарты" to laptop.videoCardModel,
            "Дискретная графическая модель" to laptop.discreteGraphics,
            "Видеочип" to laptop.videoChip,
            "Тип видеопамяти" to laptop.videoMemoryType,
            "Объем видеопамяти" to laptop.videoMemory.toString(),
            "Тактовая частота" to laptop.clockSpeed.toString(),
        )
    }

    override val allInfo = listOf(
        Main(),
        Common(),
        Display(),
        Memory(),
        Measurements(),
        InputDevices(),
        GraphicAccelerator(),
    )
}