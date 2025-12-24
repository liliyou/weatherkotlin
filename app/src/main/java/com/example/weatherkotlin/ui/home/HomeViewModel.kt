package com.example.weatherkotlin.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherkotlin.domain.model.CityWeather
import com.example.weatherkotlin.domain.repository.LocationRepository
import com.example.weatherkotlin.domain.usecase.city.AddCurrentLocationCityUseCase
import com.example.weatherkotlin.domain.usecase.city.InitializeDefaultCityUseCase
import com.example.weatherkotlin.domain.usecase.weather.GetAllCityWeatherUseCase
import com.example.weatherkotlin.domain.usecase.weather.RefreshAllWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val cityWeatherList: List<CityWeather> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val hasLocationPermission: Boolean = false,
    val locationPermissionRequested: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCityWeatherUseCase: GetAllCityWeatherUseCase,
    private val refreshAllWeatherUseCase: RefreshAllWeatherUseCase,
    private val addCurrentLocationCityUseCase: AddCurrentLocationCityUseCase,
    private val initializeDefaultCityUseCase: InitializeDefaultCityUseCase,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    init {
        loadInitialWeather()
        observeCityWeather()
    }

    private fun loadInitialWeather() {
        viewModelScope.launch {
            val hasPermission = locationRepository.hasLocationPermission()
            _uiState.value = _uiState.value.copy(
                hasLocationPermission = hasPermission,
                locationPermissionRequested = hasPermission
            )

            if (hasPermission) {
                addCurrentLocationCity()
            } else {
                initializeDefaultCityIfEmpty()
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    private suspend fun initializeDefaultCityIfEmpty() {
        try {
            initializeDefaultCityUseCase()
        } catch (_: Exception) {
            // 忽略初始化錯誤
        }
    }

    fun onPermissionResult(granted: Boolean) {
        _uiState.value = _uiState.value.copy(
            hasLocationPermission = granted,
            locationPermissionRequested = true
        )
        if (granted) {
            viewModelScope.launch {
                addCurrentLocationCity()
            }
        } else {
            viewModelScope.launch {
                initializeDefaultCityIfEmpty()
            }
        }
    }

    private suspend fun addCurrentLocationCity() {
        try {
            addCurrentLocationCityUseCase()
        } catch (_: Exception) {
            // 取得位置失敗時不做處理
        }
    }

    private fun observeCityWeather() {
        viewModelScope.launch {
            getAllCityWeatherUseCase()
                .catch { /* 忽略錯誤 */ }
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
                refreshAllWeatherUseCase()
            } catch (_: Exception) {
                _errorMessage.emit("更新天氣失敗")
            } finally {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }
}
