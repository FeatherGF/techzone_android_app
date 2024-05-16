package com.app.techzone.ui.theme.app_bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.techzone.LocalNavController
import com.app.techzone.ui.theme.RoundBorder28
import com.app.techzone.ui.theme.navigation.ScreenRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    searchState: SearchState,
    onEvent: (SearchUiEvent) -> Unit,
    suggestions: List<String>
) {
    val focusRequester = remember { FocusRequester() }
    val navController = LocalNavController.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        SearchBar(
            modifier = Modifier
                .focusRequester(focusRequester)
                .width(255.dp),
            shape = RoundBorder28,
            query = searchState.searchText,
            onQueryChange = { onEvent(SearchUiEvent.SearchTextChanged(it)) },
            onSearch = {
                onEvent(SearchUiEvent.SearchClicked(it))
                navController.navigate(
                    "${ScreenRoutes.CATALOG}/$it"
                )
            },
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
                IconButton(onClick = {
                    onEvent(SearchUiEvent.SearchClicked(searchState.searchText))
                    navController.navigate(
                        "${ScreenRoutes.CATALOG}/${searchState.searchText}"
                    )
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Button",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    if (searchState.searchText.isNotEmpty()) {
                        IconButton(onClick = { onEvent(SearchUiEvent.SearchTextChanged("")) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Button",
                                tint = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                            )
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            navController.currentDestination?.route?.let { currentRoute ->
                                if (currentRoute.startsWith("${ScreenRoutes.CATALOG}/")){
                                    onEvent(SearchUiEvent.CatalogOpened)
                                    return@OutlinedButton
                                }
                            }
                            onEvent(SearchUiEvent.SearchClosed)
                        },
                        border = null,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Отменить",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            },
            windowInsets = WindowInsets.statusBars,
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                dividerColor = Color.Gray.copy(alpha = 0.1f),
                inputFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(suggestions) { suggestion ->
                    Row(
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 16.dp)
                            .fillMaxSize()
                            .clickable {
                                onEvent(SearchUiEvent.SearchClicked(suggestion))
                                navController.navigate(
                                    "${ScreenRoutes.CATALOG}/$suggestion"
                                )
                            }
                    ) {
                        Text(
                            text = suggestion,
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
            }
        }
    }
}