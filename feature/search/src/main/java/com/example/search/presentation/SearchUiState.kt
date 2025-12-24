package com.example.search.presentation

import com.example.search.domain.model.AddedCityInfo
import com.example.search.domain.model.SearchResult

/**
 * 搜尋畫面 UI 狀態
 */
data class SearchUiState(
    val query: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val suggestedCities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val addedCity: AddedCityInfo? = null,
    val error: String? = null
)
