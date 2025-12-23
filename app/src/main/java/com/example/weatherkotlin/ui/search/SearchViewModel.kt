package com.example.weatherkotlin.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherkotlin.data.local.SearchHistoryRepository
import com.example.weatherkotlin.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val suggestedCities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isAdded: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadSuggestedCities()
    }

    private fun loadSuggestedCities() {
        viewModelScope.launch {
            val cities = searchHistoryRepository.getSuggestedCities()
            _uiState.value = _uiState.value.copy(suggestedCities = cities)
        }
    }

    private var searchJob: Job? = null

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
        _uiState.value = _uiState.value.copy(isLoading = true)
        try {
            val results = repository.searchCity(query)
            val searchResults = results.map { geo ->
                val zhName = geo.localNames?.get("zh")
                val displayName = buildString {
                    append(zhName ?: geo.name)
                    if (zhName != null && zhName != geo.name) {
                        append(" (${geo.name})")
                    }
                    geo.state?.let { append(", $it") }
                }
                SearchResult(
                    cityName = displayName,
                    lat = geo.lat,
                    lon = geo.lon
                )
            }.distinctBy { "${it.lat},${it.lon}" }
            _uiState.value = _uiState.value.copy(
                searchResults = searchResults,
                isLoading = false
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = e.message,
                isLoading = false
            )
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
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val cityName = searchResult.cityName.split(",").first().trim()
                repository.fetchAndSaveWeather(
                    lat = searchResult.lat,
                    lon = searchResult.lon,
                    cityName = cityName
                )
                // 加入建議地點歷史
                searchHistoryRepository.addSuggestedCity(cityName)
                loadSuggestedCities()

                _uiState.value = _uiState.value.copy(
                    isAdded = true,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun onSuggestedCityClick(cityName: String) {
        _uiState.value = _uiState.value.copy(query = cityName)
        viewModelScope.launch {
            searchCity(cityName)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetAddedState() {
        _uiState.value = _uiState.value.copy(isAdded = false)
    }
}
