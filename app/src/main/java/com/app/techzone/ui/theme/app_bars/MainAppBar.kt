package com.app.techzone.ui.theme.app_bars

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MainAppBar(searchViewModel: SearchViewModel) {
    val suggestions by searchViewModel.suggestions.collectAsStateWithLifecycle()
    when (searchViewModel.topBarState) {
        SearchTopBarState.CLOSED -> {
            DefaultAppBar {
                searchViewModel.onEvent(SearchUiEvent.SearchOpened)
            }
        }
        SearchTopBarState.OPENED -> {
            SearchAppBar(
                searchState = searchViewModel.state,
                onEvent = searchViewModel::onEvent,
                suggestions = suggestions
            )
        }
        SearchTopBarState.CATALOG_OPENED -> {
            CatalogCategorySearchBar(
                categoryName = searchViewModel.state.categoryScreenTitle,
                onSearchOpened = { searchViewModel.onEvent(SearchUiEvent.SearchOpened) },
            )
        }
        SearchTopBarState.HIDDEN -> {}
    }
}
