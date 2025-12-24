package com.example.weatherkotlin.domain.usecase.weather

import com.example.weatherkotlin.domain.model.CityWeather
import com.example.weatherkotlin.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 取得所有城市天氣 Use Case
 */
class GetAllCityWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(): Flow<List<CityWeather>> {
        return weatherRepository.getAllCityWeather()
    }
}
