package com.example.search.domain.usecase

import com.example.search.domain.model.AddedCityInfo
import com.example.search.domain.repository.SearchRepository
import javax.inject.Inject

/**
 * 新增城市 Use Case
 * @return 城市資訊（包含 ID 用於導航）
 */
class AddCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double, cityName: String): AddedCityInfo {
        val result = repository.addCity(lat, lon, cityName)
        if (result.isNew) {
            repository.addSuggestedCity(cityName)
        }
        return result
    }
}
