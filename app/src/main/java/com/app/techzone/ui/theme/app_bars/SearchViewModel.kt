package com.app.techzone.ui.theme.app_bars

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

data class Suggestion(val id: Int, val name: String)


class SearchViewModel : ViewModel() {
    var searchWidgetState by mutableStateOf(SearchWidgetState.CLOSED)

    var searchTextState by mutableStateOf("")

    var categoryName by mutableStateOf("")

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        searchWidgetState = newValue
    }

    fun updateSearchTextState(newValue: String) {
        searchTextState = newValue
    }

    fun updateCategoryNameState(newValue: String) {
        categoryName = newValue
    }

    private val suggestionFlow = flowOf(
        listOf(
            Suggestion(id = 1, "iphone"),
            Suggestion(id = 2, "iphone 15"),
            Suggestion(id = 3, "macbook pro"),
            Suggestion(id = 4, "airpods"),
            Suggestion(id = 5, "apple watch 2"),
        )
    )

    val searchSuggestions: StateFlow<List<Suggestion>> =
        snapshotFlow { searchTextState }.combine(suggestionFlow) {query, suggestions ->
            when {
                query.isNotEmpty() -> suggestions.filter {suggestion ->
                    suggestion.name.contains(query, ignoreCase = true)
                }
                else -> suggestions
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(5_000)
        )
}