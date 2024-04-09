package com.app.techzone.ui.theme.catalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class CatalogViewModel : ViewModel() {
    var activeScreenState by mutableStateOf(CatalogScreenEnum.DEFAULT)

    fun updateActiveState(newValue: CatalogScreenEnum){
        activeScreenState = newValue
    }
}
