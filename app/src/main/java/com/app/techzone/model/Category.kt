package com.app.techzone.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Laptop
import androidx.compose.material.icons.outlined.Mouse
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Tablet
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: Int,
    val imageVector: ImageVector,
    val name: String
)

val categories = listOf(
    Category(id = 1, imageVector = Icons.Outlined.Tv, name = "Телевизоры"),
    Category(id = 2, imageVector = Icons.Outlined.Laptop, name = "Ноутбуки"),
    Category(id = 3, imageVector = Icons.Outlined.Tablet, name = "Планшеты"),
    Category(id = 4, imageVector = Icons.Outlined.Smartphone, name = "Смартфоны"),
    Category(id = 5, imageVector = Icons.Outlined.Watch, name = "Смарт-часы"),
    Category(id = 6, imageVector = Icons.Outlined.Mouse, name = "Аксессуары"),
)
