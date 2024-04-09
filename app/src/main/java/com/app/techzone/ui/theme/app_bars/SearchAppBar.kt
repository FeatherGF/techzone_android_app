package com.app.techzone.ui.theme.app_bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.techzone.ui.theme.RoundBorder28

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    searchSuggestions: List<Suggestion>,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    Surface(
        modifier = Modifier
            .height(335.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        SearchBar(
            modifier = Modifier
                .focusRequester(focusRequester)
                .width(255.dp),
            shape = RoundBorder28,
            query = text,
            onQueryChange = onTextChange,
            onSearch = onSearchClicked,
            active = true,
            onActiveChange = { },
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
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    if (text.isNotEmpty()) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 25.dp)
                                .clickable { onTextChange("") },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Button",
                            tint = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                        )
                    }
                    Text(
                        text = "Отменить",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(end = 23.dp)
                            .clickable {
                                onTextChange("")
                                onCloseClicked()
                            }
                    )
                }
            },
            windowInsets = WindowInsets.statusBars,
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
                dividerColor = Color.Gray.copy(alpha = 0.1f),
            )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = searchSuggestions.size,
                    key = { index -> searchSuggestions[index].id },
                    itemContent = { index ->
                        val suggestion = searchSuggestions[index]
                        Row(
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    top = 20.dp,
                                    end = 16.dp,
                                    bottom = 20.dp
                                )
                                .fillMaxSize()
                                .clickable {
                                    onTextChange(suggestion.name)
                                    onSearchClicked(suggestion.name)
                                    onCloseClicked()
                                }
                        ) {
                            Text(
                                text = suggestion.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                            )
                            Image(
                                imageVector = Icons.Outlined.ChevronRight,
                                contentDescription = null
                            )
                        }
                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
                    }
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}