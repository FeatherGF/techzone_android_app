package com.app.techzone.ui.theme.product_detail.characteristics

import com.app.techzone.utils.boolToString
import com.app.techzone.data.remote.model.Television

data class TelevisionCharacteristics(val television: Television): ICharacteristicClass{
    inner class Common: ICharacteristic {
        override val label: String = "Заводские данные о товаре"
        override val items: Map<String, String> = mapOf(
            "Модель" to television.model,
            "Код товара" to television.id.toString(),
            "Дата выпуска" to television.dateRelease,
            "Описание" to television.description,
        )
    }

    inner class Main: ICharacteristic {
        override val label: String = "Основные характеристики"
        override val items: Map<String, String> = mapOf(
            "Цвет товара" to television.colorMain,
            "Операционная система" to television.operatingSystem,
            "Материал корпуса" to television.material,
            "Дополнительный цвет товара" to television.colorOther,
            "Вес, кг" to television.weight.toString(),
            "Комплектация" to television.equipment,
            "Потребление, Вт" to television.consumption.toString(),
            "HDMI порты" to boolToString(television.hdmiPorts),
            "Цифровые тюнеры" to television.codecs,
            "Количество usb портов" to television.usbPorts,
            "Управление со смартфона" to boolToString(television.smartphoneControl),
            "Управление через BlueTooth" to boolToString(television.bluetoothControl),
            "Приложение для управления" to television.managementApplication,
            "WiFi" to boolToString(television.wifiAvailability),
            "Стандарт WiFi" to television.wifiStandard,
        )
    }

    inner class Measurements: ICharacteristic {
        override val label: String = "Размеры"
        override val items = mapOf(
            "Высота, в сантиметрах" to television.height.toString(),
            "Длина, в сантиметрах" to television.width.toString(),
            "Толщина, в сантиметрах" to television.thickness.toString(),
        )
    }

    inner class Display: ICharacteristic {
        override val label: String = "Экран"
        override val items: Map<String, String> = mapOf(
            "Разрешение экрана" to television.screenResolution,
            "Диагональ экрана, в дюймах" to television.screenDiagonal,
            "Тип экрана" to television.matrixType,
            "Частота экрана" to television.matrixFrequency.toString(),
            "Яркость экрана" to television.matrixBrightness,
            "Поддержка HDR" to boolToString(television.hdrSupport),
            "Угол обзора" to television.angleView,
        )
    }

    inner class Sound: ICharacteristic {
        override val label: String = "Звук"
        override val items: Map<String, String> = mapOf(
            "Мощность звука" to television.soundPower,
            "Технология звука" to television.soundTechnology,
            "Разьем для наущников" to boolToString(television.headphoneOutput),
            "Голосовой помощник" to television.voiceAssistant,
            "Сабвуфер" to boolToString(television.subwoofer),
            "Объемное звучание" to boolToString(television.surroundSound),
        )
    }

    inner class Memory: ICharacteristic {
        override val label: String = "Память"
        override val items: Map<String, String> = mapOf(
            "Оперативная память, в ГБ" to television.memoryRam.toString(),
            "Встроенная память, в ГБ" to television.memory.toString(),
        )
    }

    override val allInfo: List<ICharacteristic> = listOf(
        Main(),
        Common(),
        Display(),
        Memory(),
        Measurements(),
        Sound(),
    )
}
