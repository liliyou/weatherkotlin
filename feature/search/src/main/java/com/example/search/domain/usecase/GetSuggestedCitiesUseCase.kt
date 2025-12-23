package com.example.search.domain.usecase

import com.example.search.domain.repository.SearchRepository
import javax.inject.Inject

/**
 * 取得建議城市 Use Case
 */
class GetSuggestedCitiesUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(): List<String> {
        return repository.getSuggestedCities()
    }
}
