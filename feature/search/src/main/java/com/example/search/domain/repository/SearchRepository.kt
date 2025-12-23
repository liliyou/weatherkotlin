package com.example.search.domain.repository

import com.example.search.domain.model.SearchResult

/**
 * 搜尋功能 Repository 介面
 * 由 app module 實作
 */
interface SearchRepository {
    /**
     * 搜尋城市
     */
    suspend fun searchCity(query: String): List<SearchResult>

    /**
     * 新增城市到收藏
     * @return true 如果成功新增，false 如果城市已存在
     */
    suspend fun addCity(lat: Double, lon: Double, cityName: String): Boolean

    /**
     * 取得建議城市列表
     */
    suspend fun getSuggestedCities(): List<String>

    /**
     * 新增建議城市
     */
    suspend fun addSuggestedCity(cityName: String)
}
