package com.example.weatherkotlin.domain.repository

import com.example.weatherkotlin.domain.model.Location

/**
 * 位置服務 Repository 介面
 */
interface LocationRepository {
    /**
     * 檢查是否有位置權限
     */
    fun hasLocationPermission(): Boolean

    /**
     * 取得目前位置
     */
    suspend fun getCurrentLocation(): Location?
}
