package com.example.weatherkotlin.ui.home

import android.util.Log
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
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

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
        val hasPermission = locationService.hasLocationPermission()
        Log.d(TAG, "loadInitialWeather: hasPermission=$hasPermission")
        _uiState.value = _uiState.value.copy(
            hasLocationPermission = hasPermission,
            locationPermissionRequested = hasPermission
        )
        if (hasPermission) {
            Log.d(TAG, "loadInitialWeather: fetching current location")
            fetchCurrentLocationWeather()
        } else {
            Log.d(TAG, "loadInitialWeather: fetching default city (Taipei)")
            fetchDefaultCityWeather()
        }
    }

    fun onPermissionResult(granted: Boolean) {
        _uiState.value = _uiState.value.copy(
            hasLocationPermission = granted,
            locationPermissionRequested = true
        )
        if (granted) {
            fetchCurrentLocationWeather()
        } else {
            fetchDefaultCityWeather()
        }
    }

    private fun fetchDefaultCityWeather() {
        Log.d(TAG, "fetchDefaultCityWeather: starting")
        viewModelScope.launch {
            try {
                // 台北座標
                val taipeiLat = 25.0330
                val taipeiLon = 121.5654
                Log.d(TAG, "fetchDefaultCityWeather: calling API lat=$taipeiLat, lon=$taipeiLon")
                val weather = repository.fetchWeatherOnly(taipeiLat, taipeiLon, "台北")
                Log.d(TAG, "fetchDefaultCityWeather: success, city=${weather.cityName}, temp=${weather.currentTemp}")
                _uiState.value = _uiState.value.copy(
                    currentLocationWeather = weather,
                    isLoading = false
                )
                Log.d(TAG, "fetchDefaultCityWeather: uiState updated, currentLocationWeather=${_uiState.value.currentLocationWeather}")
            } catch (e: Exception) {
                Log.e(TAG, "fetchDefaultCityWeather: error", e)
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
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
                // 更新當前位置或預設城市天氣
                if (_uiState.value.hasLocationPermission) {
                    val location = locationService.getCurrentLocation()
                    if (location != null) {
                        val weather = repository.fetchWeatherOnly(location.lat, location.lon)
                        _uiState.value = _uiState.value.copy(currentLocationWeather = weather)
                    }
                } else {
                    val weather = repository.fetchWeatherOnly(25.0330, 121.5654, "台北")
                    _uiState.value = _uiState.value.copy(currentLocationWeather = weather)
                }
                // 更新已儲存城市天氣
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
