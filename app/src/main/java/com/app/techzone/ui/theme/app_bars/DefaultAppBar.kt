package com.app.techzone.ui.theme.app_bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.techzone.R
import com.app.techzone.ui.theme.ForStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(onSearchOpened: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomEnd = 28.dp, bottomStart = 28.dp),
        color = MaterialTheme.colorScheme.primary,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, top = 60.dp)
        ) {
            Row(Modifier.fillMaxWidth(0.42f)) {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                )
            }
            SearchBar(
                modifier = Modifier.consumeWindowInsets(PaddingValues(10000.dp)),
                query = "",
                onQueryChange = {},
                onSearch = {},
                active = false,
                onActiveChange = { onSearchOpened() },
                content = {},
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
                    dividerColor = ForStroke
                )
            )
        }
    }
}