package com.example.search.domain.repository

import com.example.search.domain.model.AddedCityInfo
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
     * @return 城市資訊（包含 ID 用於導航）
     */
    suspend fun addCity(lat: Double, lon: Double, cityName: String): AddedCityInfo

    /**
     * 取得建議城市列表
     */
    suspend fun getSuggestedCities(): List<String>

    /**
     * 新增建議城市
     */
    suspend fun addSuggestedCity(cityName: String)
}
