package com.app.techzone.utils

import com.app.techzone.data.remote.model.Accessory
import com.app.techzone.data.remote.model.IDetailedProduct
import com.app.techzone.data.remote.model.Laptop
import com.app.techzone.model.ProductTypeEnum
import com.app.techzone.data.remote.model.Smartphone
import com.app.techzone.data.remote.model.Smartwatch
import com.app.techzone.data.remote.model.Tablet
import com.app.techzone.data.remote.model.Television
import com.app.techzone.model.getProductType
import com.app.techzone.ui.theme.product_detail.characteristics.AccessoryCharacteristics
import com.app.techzone.ui.theme.product_detail.characteristics.ICharacteristicClass
import com.app.techzone.ui.theme.product_detail.characteristics.LaptopCharacteristics
import com.app.techzone.ui.theme.product_detail.characteristics.SmartphoneCharacteristics
import com.app.techzone.ui.theme.product_detail.characteristics.SmartwatchCharacteristics
import com.app.techzone.ui.theme.product_detail.characteristics.TabletCharacteristics
import com.app.techzone.ui.theme.product_detail.characteristics.TelevisionCharacteristics


fun getProductCharacteristics(product: IDetailedProduct): ICharacteristicClass {
    return when (getProductType(product.type)) {
        ProductTypeEnum.SMARTPHONE -> SmartphoneCharacteristics(product as Smartphone)
        ProductTypeEnum.LAPTOP -> LaptopCharacteristics(product as Laptop)
        ProductTypeEnum.ACCESSORY -> AccessoryCharacteristics(product as Accessory)
        ProductTypeEnum.TABLET -> TabletCharacteristics(product as Tablet)
        ProductTypeEnum.SMARTWATCH -> SmartwatchCharacteristics(product as Smartwatch)
        ProductTypeEnum.TELEVISION -> TelevisionCharacteristics(product as Television)
        // TODO: remake. probably append default characteristic class for base product
        else -> SmartphoneCharacteristics(product as Smartphone)
    }
}
