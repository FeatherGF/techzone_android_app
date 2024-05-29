package com.app.techzone.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.ui.graphics.vector.ImageVector

data class Benefit(val imageId: ImageVector, val text: String)

val benefits = listOf(
    Benefit(Icons.Outlined.Percent, "Скидки до -50% на весь ассортимент"),
    Benefit(Icons.Outlined.ChangeCircle, "30 дней на обмен или возврат товара"),
    Benefit(Icons.Outlined.VerifiedUser, "Гарантия качества и страхование техники"),
)