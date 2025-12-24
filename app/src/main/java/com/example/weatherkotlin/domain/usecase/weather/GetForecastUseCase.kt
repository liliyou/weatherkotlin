package com.example.weatherkotlin.domain.usecase.weather

import com.example.weatherkotlin.domain.model.ForecastResult
import com.example.weatherkotlin.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * 取得天氣預報 Use Case
 */
class GetForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): ForecastResult {
        return weatherRepository.getForecast(lat, lon)
    }
}
