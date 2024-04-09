package com.app.techzone.ui.theme.app_bars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.techzone.ui.theme.RoundBorder28


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogCategorySearchBar(
    categoryName: String,
    onSearchTriggered: () -> Unit,
    onBackClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(176.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.tertiary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.clickable(onClick = onBackClicked)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        categoryName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                    )
                }
            }
            SearchBar(
                shape = RoundBorder28,
                query = "",
                onQueryChange = {},
                onSearch = { },
                active = false,
                onActiveChange = { onSearchTriggered() },
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
                    dividerColor = Color.Gray.copy(alpha = 0.1f),
                ),
                content = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}