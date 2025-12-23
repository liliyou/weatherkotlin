package com.example.weatherkotlin.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherkotlin.domain.model.DailyWeather
import com.example.weatherkotlin.domain.model.HourlyWeather
import com.example.weatherkotlin.domain.usecase.DeleteCityWeatherUseCase
import com.example.weatherkotlin.domain.usecase.GetForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val cityName: String = "",
    val hourlyWeather: List<HourlyWeather> = emptyList(),
    val dailyWeather: List<DailyWeather> = emptyList(),
    val canDelete: Boolean = false,
    val isDeleted: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    private val deleteCityWeatherUseCase: DeleteCityWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var currentCityId: Long = -1

    fun loadWeather(lat: Double, lon: Double, cityName: String, cityId: Long) {
        if (_uiState.value.cityName == cityName && _uiState.value.hourlyWeather.isNotEmpty()) {
            return // 已經載入過
        }

        currentCityId = cityId

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                cityName = cityName,
                canDelete = cityId > 0
            )
            try {
                val forecast = getForecastUseCase(lat = lat, lon = lon)
                _uiState.value = _uiState.value.copy(
                    hourlyWeather = forecast.hourlyWeather,
                    dailyWeather = forecast.dailyWeather,
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

    fun deleteCity() {
        if (currentCityId <= 0) return

        viewModelScope.launch {
            try {
                deleteCityWeatherUseCase(currentCityId)
                _uiState.value = _uiState.value.copy(isDeleted = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
