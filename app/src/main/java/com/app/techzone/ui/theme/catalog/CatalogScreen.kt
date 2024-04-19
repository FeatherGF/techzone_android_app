package com.app.techzone.ui.theme.catalog

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.techzone.model.categories
import com.app.techzone.ui.theme.app_bars.SearchViewModel
import com.app.techzone.ui.theme.ForStroke


@Composable
fun CatalogScreen(
    navController: NavController,
    searchViewModel: SearchViewModel
) {
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
                    color = ForStroke.copy(alpha = 0.1f),
                    width = 1.dp,
                    shape = RoundedCornerShape(4.dp)
                )
                .background(color = MaterialTheme.colorScheme.tertiary)
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
                            .clickable {
                                navController.navigate("catalog/${category.endpoint}")
                                searchViewModel.updateCategoryNameState(category.name)
                            }
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
                    // don't render divider after last, because border will do it
                    if (index != categories.size - 1) {
                        HorizontalDivider(color = ForStroke.copy(alpha = 0.1f))
                    }
                }
            )
        }
    }
}
