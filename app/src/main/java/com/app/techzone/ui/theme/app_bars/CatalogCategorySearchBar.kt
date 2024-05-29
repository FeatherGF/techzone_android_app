package com.app.techzone.ui.theme.app_bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.techzone.LocalNavController
import com.app.techzone.ui.theme.ForStroke


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogCategorySearchBar(
    categoryName: String,
    onSearchOpened: () -> Unit,
) {
    val navController = LocalNavController.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.tertiary
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth()
                    .padding(top = 52.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = navController::popBackStack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black,
                    )
                }
                Text(
                    categoryName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f),
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.Companion.Transparent,
                )
            }
            SearchBar(
                modifier = Modifier
                    .consumeWindowInsets(PaddingValues(100.dp))
                    .padding(start = 16.dp, bottom = 16.dp),
                query = "",
                onQueryChange = {},
                onSearch = { },
                active = false,
                onActiveChange = { onSearchOpened() },
                placeholder = {
                    Text(
                        text = "Поиск в TechZone",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.scrim
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Button",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    dividerColor = ForStroke,
                ),
                content = {},
            )
        }
    }
}