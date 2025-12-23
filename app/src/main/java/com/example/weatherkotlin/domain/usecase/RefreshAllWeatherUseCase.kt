package com.example.weatherkotlin.domain.usecase

import com.example.weatherkotlin.domain.model.CityWeather
import com.example.weatherkotlin.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * 刷新所有城市天氣 Use Case
 */
class RefreshAllWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): List<CityWeather> {
        return weatherRepository.refreshAllWeather()
    }
}
