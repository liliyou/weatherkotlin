package com.example.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.search.domain.model.SearchResult
import com.example.search.domain.usecase.AddCityUseCase
import com.example.search.domain.usecase.GetSuggestedCitiesUseCase
import com.example.search.domain.usecase.SearchCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val addCityUseCase: AddCityUseCase,
    private val getSuggestedCitiesUseCase: GetSuggestedCitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadSuggestedCities()
    }

    private fun loadSuggestedCities() {
        viewModelScope.launch {
            val cities = getSuggestedCitiesUseCase()
            _uiState.value = _uiState.value.copy(suggestedCities = cities)
        }
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }
        searchJob = viewModelScope.launch {
            delay(300)
            searchCity(query)
        }
    }

    private suspend fun searchCity(query: String) {
        try {
            val results = searchCitiesUseCase(query)
            _uiState.value = _uiState.value.copy(searchResults = results)
        } catch (_: Exception) {
            // 搜尋失敗時清空結果
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
        }
    }

    fun onSearch() {
        val query = _uiState.value.query
        if (query.isNotBlank()) {
            viewModelScope.launch {
                searchCity(query)
            }
        }
    }

    fun addCity(searchResult: SearchResult) {
        viewModelScope.launch {
            try {
                val cityName = searchResult.cityName.split(",").first().trim()
                val result = addCityUseCase(
                    lat = searchResult.lat,
                    lon = searchResult.lon,
                    cityName = cityName
                )
                loadSuggestedCities()
                _uiState.value = _uiState.value.copy(addedCity = result)
            } catch (_: Exception) {
                // 新增失敗時不做處理
            }
        }
    }

    fun onSuggestedCityClick(cityName: String) {
        _uiState.value = _uiState.value.copy(query = cityName)
        viewModelScope.launch {
            searchCity(cityName)
        }
    }

    fun resetAddedCity() {
        _uiState.value = _uiState.value.copy(addedCity = null)
    }
}
