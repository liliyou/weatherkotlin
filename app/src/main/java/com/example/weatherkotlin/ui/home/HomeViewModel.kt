package com.example.weatherkotlin.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherkotlin.data.location.LocationService
import com.example.weatherkotlin.data.model.CityWeather
import com.example.weatherkotlin.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val currentLocationWeather: CityWeather? = null,
    val cityWeatherList: List<CityWeather> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val hasLocationPermission: Boolean = false,
    val locationPermissionRequested: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialWeather()
        observeCityWeather()
    }

    private fun loadInitialWeather() {
        viewModelScope.launch {
            // 初始化預設城市（台北）如果資料庫是空的
            initializeDefaultCityIfEmpty()

            val hasPermission = locationService.hasLocationPermission()
            _uiState.value = _uiState.value.copy(
                hasLocationPermission = hasPermission,
                locationPermissionRequested = hasPermission,
                isLoading = false
            )
            if (hasPermission) {
                fetchCurrentLocationWeather()
            }
        }
    }

    private suspend fun initializeDefaultCityIfEmpty() {
        try {
            val cities = repository.getAllCityWeather().first()
            if (cities.isEmpty()) {
                repository.fetchAndSaveWeather(DEFAULT_LAT, DEFAULT_LON, DEFAULT_CITY)
            }
        } catch (_: Exception) {
            // 忽略初始化錯誤
        }
    }

    companion object {
        private const val DEFAULT_LAT = 25.0330
        private const val DEFAULT_LON = 121.5654
        private const val DEFAULT_CITY = "台北市"
    }

    fun onPermissionResult(granted: Boolean) {
        _uiState.value = _uiState.value.copy(
            hasLocationPermission = granted,
            locationPermissionRequested = true
        )
        if (granted) {
            fetchCurrentLocationWeather()
        }
        // 不需要 else，因為台北已在資料庫中
    }

    fun fetchCurrentLocationWeather() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val location = locationService.getCurrentLocation()
                if (location != null) {
                    val weather = repository.fetchWeatherOnly(location.lat, location.lon)
                    _uiState.value = _uiState.value.copy(
                        currentLocationWeather = weather,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    private fun observeCityWeather() {
        viewModelScope.launch {
            repository.getAllCityWeather()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(error = e.message)
                }
                .collect { cityWeatherList ->
                    _uiState.value = _uiState.value.copy(
                        cityWeatherList = cityWeatherList
                    )
                }
        }
    }

    fun refreshAllWeather() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            try {
                // 更新當前位置天氣（如果有權限）
                if (_uiState.value.hasLocationPermission) {
                    val location = locationService.getCurrentLocation()
                    if (location != null) {
                        val weather = repository.fetchWeatherOnly(location.lat, location.lon)
                        _uiState.value = _uiState.value.copy(currentLocationWeather = weather)
                    }
                }
                // 更新已儲存城市天氣（包含台北）
                repository.refreshAllWeather()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
