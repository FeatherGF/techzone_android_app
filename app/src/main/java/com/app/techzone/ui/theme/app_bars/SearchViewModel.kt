package com.app.techzone.ui.theme.app_bars

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.techzone.data.remote.repository.ProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepo: ProductRepo,
) : ViewModel() {
    var topBarState by mutableStateOf(SearchTopBarState.CLOSED)

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions = _suggestions.asStateFlow()
    var state by mutableStateOf(SearchState())

    fun onEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.SearchClicked -> {
                topBarState = SearchTopBarState.CATALOG_OPENED
                state = state.copy(
                    searchText = "",
                    categoryScreenTitle = event.text
                )
            }
            is SearchUiEvent.SearchTextChanged -> {
                state = state.copy(searchText = event.text)
                if (event.text.length > 1) {
                    loadSuggestions()
                }
                else {
                    _suggestions.update { emptyList() }
                }
            }
            SearchUiEvent.SearchClosed -> {
                topBarState = SearchTopBarState.CLOSED
                state = state.copy(searchText = "")
            }
            SearchUiEvent.SearchOpened -> {
                topBarState = SearchTopBarState.OPENED
            }
            SearchUiEvent.SearchHidden -> {
                topBarState = SearchTopBarState.HIDDEN
            }
            SearchUiEvent.CatalogOpened -> {
                topBarState = SearchTopBarState.CATALOG_OPENED
            }
        }
    }

    private fun loadSuggestions() {
        viewModelScope.launch {
            val suggestions = productRepo.getSuggestions(state.searchText)
            if (suggestions.isNotEmpty()){
                _suggestions.update { suggestions }
            }
        }
    }

    fun updateSearchTopBarState(newValue: SearchTopBarState) {
        topBarState = newValue
    }
}