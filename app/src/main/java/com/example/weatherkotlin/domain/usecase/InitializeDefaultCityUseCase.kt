package com.example.weatherkotlin.domain.usecase

import com.example.weatherkotlin.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * 初始化預設城市 Use Case
 */
class InitializeDefaultCityUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    companion object {
        private const val DEFAULT_LAT = 25.0330
        private const val DEFAULT_LON = 121.5654
        private const val DEFAULT_CITY = "台北市"
    }

    suspend operator fun invoke() {
        val cities = weatherRepository.getAllCityWeather().first()
        if (cities.isEmpty()) {
            weatherRepository.fetchAndSaveWeather(DEFAULT_LAT, DEFAULT_LON, DEFAULT_CITY)
        }
    }
}
