package com.example.search.domain.usecase

import com.example.search.domain.model.SearchResult
import com.example.search.domain.repository.SearchRepository
import javax.inject.Inject

/**
 * 搜尋城市 Use Case
 */
class SearchCitiesUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): List<SearchResult> {
        return repository.searchCity(query)
    }
}
