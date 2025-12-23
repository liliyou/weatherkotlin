package com.example.weatherkotlin.domain.usecase

import com.example.weatherkotlin.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * 刪除城市天氣 Use Case
 */
class DeleteCityWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(id: Long) {
        weatherRepository.deleteCityWeather(id)
    }
}
