package com.app.techzone.ui.theme.product_detail.characteristics

import com.app.techzone.utils.boolToString
import com.app.techzone.data.remote.model.Smartwatch

data class SmartwatchCharacteristics(val smartwatch: Smartwatch): ICharacteristicClass {
    inner class Common: ICharacteristic {
        override val label: String = "Заводские данные о товаре"
        override val items = mapOf(
            "Модель" to smartwatch.model,
            "Код товара" to smartwatch.id.toString(),
            "Дата выпуска" to smartwatch.dateRelease,
            "Описание" to smartwatch.description,
        )
    }

    inner class Main: ICharacteristic {
        override val label: String = "Основные характеристики"
        override val items = mapOf(
            "Цвет товара" to smartwatch.colorMain,
            "Операционная система" to smartwatch.operatingSystem,
            "Материал корпуса" to smartwatch.material,
            "Дополнительный цвет товара" to smartwatch.colorOther,
            "Степень защиты" to smartwatch.degreeProtection,
            "Тип корпуса" to smartwatch.material,
            "Вес, кг" to smartwatch.weight.toString(),
            "Материал ремешка" to smartwatch.materialBelt,
            "Комплектация" to smartwatch.equipment,
            "Технология звука" to smartwatch.soundTechnology,
            "Выход для наушников" to boolToString(smartwatch.headphoneOutput),
            "Защита от влаги" to smartwatch.waterResistance.toString()
        )
    }

    inner class Battery: ICharacteristic {
        override val label: String = "Питание"
        override val items: Map<String, String> = mapOf(
            "Быстрая зарядка (Fast-charge)" to boolToString(smartwatch.fastCharge),
            "Тип батареи" to smartwatch.accumulatorType,
            "Вместительность батареи" to smartwatch.accumulatorCapacity.toString(),
        )
    }

    inner class Display: ICharacteristic {
        override val label: String = "Экран"
        override val items = mapOf(
            "Разрешение экрана" to smartwatch.screenResolution,
            "Диагональ экрана, в дюймах" to smartwatch.screenDiagonal,
            "Формат экрана" to smartwatch.screenFormat,
            "Тип экрана" to smartwatch.matrixType,
            "Частота экрана" to smartwatch.matrixFrequency.toString(),
            "Яркость экрана" to smartwatch.matrixBrightness,
            "Плотность пикселей" to smartwatch.pixelDensity.toString(),
        )
    }

    inner class Measurements: ICharacteristic {
        override val label: String = "Размеры"
        override val items = mapOf(
            "Высота, в сантиметрах" to smartwatch.height.toString(),
            "Длина, в сантиметрах" to smartwatch.width.toString(),
            "Толщина, в сантиметрах" to smartwatch.thickness.toString(),
        )
    }

    inner class Memory: ICharacteristic {
        override val label: String = "Память"
        override val items = mapOf(
            "Встроенная память, в ГБ" to smartwatch.memory.toString(),
            "Оперативная память, в ГБ" to smartwatch.memoryRam.toString(),
        )
    }
    
    override val allInfo: List<ICharacteristic> = listOf(
        Main(),
        Common(),
        Display(),
        Battery(),
        Memory(),
        Measurements(),
    )
}
