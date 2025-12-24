package com.example.weatherkotlin.domain.usecase

import com.example.weatherkotlin.domain.repository.LocationRepository
import com.example.weatherkotlin.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * 新增目前位置城市 Use Case
 */
class AddCurrentLocationCityUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): Boolean {
        val location = locationRepository.getCurrentLocation() ?: return false
        val result = weatherRepository.addCityIfNotExists(location.lat, location.lon)
        return result.isNew
    }
}
