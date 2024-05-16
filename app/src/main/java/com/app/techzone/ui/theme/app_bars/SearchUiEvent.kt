package com.app.techzone.ui.theme.app_bars

sealed class SearchUiEvent {
    data class SearchTextChanged(val text: String): SearchUiEvent()
    data class SearchClicked(val text: String): SearchUiEvent()
    data object SearchClosed: SearchUiEvent()
    data object SearchOpened: SearchUiEvent()
    data object SearchHidden: SearchUiEvent()
    data object CatalogOpened: SearchUiEvent()
}
