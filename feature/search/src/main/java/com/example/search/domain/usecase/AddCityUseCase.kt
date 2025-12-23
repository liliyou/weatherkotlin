package com.example.search.domain.usecase

import com.example.search.domain.repository.SearchRepository
import javax.inject.Inject

/**
 * 新增城市 Use Case
 */
class AddCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double, cityName: String) {
        repository.addCity(lat, lon, cityName)
        repository.addSuggestedCity(cityName)
    }
}
