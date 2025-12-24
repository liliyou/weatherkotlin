package com.example.search.domain.model

/**
 * 新增城市後的資訊，用於導航到詳細頁面
 */
data class AddedCityInfo(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val cityName: String,
    val isNew: Boolean
)
