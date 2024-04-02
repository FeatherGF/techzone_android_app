package com.app.techzone.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Laptop
import androidx.compose.material.icons.outlined.Mouse
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Tablet
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


data class Category(val id: Int, val imageVector: ImageVector, val name: String)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun CatalogScreen(){
    val categories = listOf(
        Category(id = 1, imageVector = Icons.Outlined.Tv, name = "Телевизоры"),
        Category(id = 2, imageVector = Icons.Outlined.Laptop, name = "Ноутбуки"),
        Category(id = 3, imageVector = Icons.Outlined.Tablet, name = "Планшеты"),
        Category(id = 4, imageVector = Icons.Outlined.Smartphone, name = "Смартфоны"),
        Category(id = 5, imageVector = Icons.Outlined.Watch, name = "Смарт-часы"),
        Category(id = 6, imageVector = Icons.Outlined.Mouse, name = "Аксессуары"),
    )
    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        Text(
            text = "Каталог",
            modifier = Modifier.padding(start = 16.dp, top = 28.dp),
            style = MaterialTheme.typography.titleLarge,
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp)
                .border(
                    color = MaterialTheme.colorScheme.scrim,
                    width = 1.dp,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            items(
                count = categories.size,
                key = { index -> categories[index].id },
                itemContent = { index ->
                    val category = categories[index]
                    Row(
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { }
                    ) {
                        Row {
                            Icon(
                                modifier = Modifier.padding(horizontal = 28.dp),
                                imageVector = category.imageVector,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 36.dp)
                        )
                    }
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
                }
            )
        }
    }
}
