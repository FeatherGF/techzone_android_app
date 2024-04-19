package com.app.techzone.ui.theme.product_detail.characteristics

import com.app.techzone.data.remote.model.IDetailedProduct
import com.app.techzone.data.remote.model.Laptop
import com.app.techzone.data.remote.model.ProductTypeEnum
import com.app.techzone.data.remote.model.Smartphone
import com.app.techzone.data.remote.model.getProductType


fun getProductCharacteristics(product: IDetailedProduct): ICharacteristicClass {
    return when(getProductType(product.type)) {
        ProductTypeEnum.SMARTPHONE -> SmartphoneCharacteristics(product as Smartphone)
        ProductTypeEnum.LAPTOP -> LaptopCharacteristics(product as Laptop)
        else -> throw NotImplementedError()
    }
}