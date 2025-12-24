package com.example.weatherkotlin.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherkotlin.domain.model.CityWeather
import com.example.weatherkotlin.domain.model.DailyWeather
import com.example.weatherkotlin.domain.model.HourlyWeather
import com.example.weatherkotlin.domain.repository.WeatherRepository
import com.example.weatherkotlin.domain.usecase.DeleteCityWeatherUseCase
import com.example.weatherkotlin.domain.usecase.GetForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val cityName: String = "",
    val currentWeatherIcon: String = "01d",
    val currentTemp: Int = 0,
    val hourlyWeather: List<HourlyWeather> = emptyList(),
    val dailyWeather: List<DailyWeather> = emptyList(),
    val canDelete: Boolean = false,
    val isDeleted: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val getForecastUseCase: GetForecastUseCase,
    private val deleteCityWeatherUseCase: DeleteCityWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var currentCityId: Long = -1
    private var currentLat: Double = 0.0
    private var currentLon: Double = 0.0

    fun loadWeather(lat: Double, lon: Double, cityName: String, cityId: Long) {
        if (_uiState.value.cityName == cityName && _uiState.value.hourlyWeather.isNotEmpty()) {
            return // 已經載入過
        }

        currentCityId = cityId
        currentLat = lat
        currentLon = lon

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                cityName = cityName,
                canDelete = cityId > 0
            )
            try {
                // 平行取得當前天氣和預報
                val currentWeatherDeferred = async { weatherRepository.getCityWeatherById(cityId) }
                val forecastDeferred = async { getForecastUseCase(lat = lat, lon = lon) }

                val currentWeather = currentWeatherDeferred.await()
                val forecast = forecastDeferred.await()

                _uiState.value = _uiState.value.copy(
                    currentWeatherIcon = currentWeather?.weatherIcon ?: "01d",
                    currentTemp = currentWeather?.currentTemp ?: 0,
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

    fun refresh() {
        if (currentCityId <= 0) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            try {
                val currentWeather = weatherRepository.getCityWeatherById(currentCityId)
                val refreshedWeather = currentWeather?.let {
                    weatherRepository.refreshWeather(it)
                }
                val forecast = getForecastUseCase(lat = currentLat, lon = currentLon)

                _uiState.value = _uiState.value.copy(
                    currentWeatherIcon = refreshedWeather?.weatherIcon ?: _uiState.value.currentWeatherIcon,
                    currentTemp = refreshedWeather?.currentTemp ?: _uiState.value.currentTemp,
                    hourlyWeather = forecast.hourlyWeather,
                    dailyWeather = forecast.dailyWeather,
                    isRefreshing = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isRefreshing = false
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
