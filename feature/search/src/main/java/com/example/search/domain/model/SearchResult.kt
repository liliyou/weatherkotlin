package com.example.search.domain.model

/**
 * 搜尋結果 Domain Model
 */
data class SearchResult(
    val cityName: String,
    val lat: Double,
    val lon: Double
)
