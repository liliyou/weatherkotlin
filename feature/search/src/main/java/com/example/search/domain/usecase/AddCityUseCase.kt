package com.example.search.domain.usecase

import com.example.search.domain.repository.SearchRepository
import javax.inject.Inject

/**
 * 新增城市 Use Case
 * @return true 如果成功新增，false 如果城市已存在
 */
class AddCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double, cityName: String): Boolean {
        val added = repository.addCity(lat, lon, cityName)
        if (added) {
            repository.addSuggestedCity(cityName)
        }
        return added
    }
}
