package com.example.weatherkotlin.domain.repository

import com.example.weatherkotlin.domain.model.AddCityResult
import com.example.weatherkotlin.domain.model.CityWeather
import com.example.weatherkotlin.domain.model.ForecastResult
import kotlinx.coroutines.flow.Flow

/**
 * 天氣資料 Repository 介面
 * Data Layer 實作此介面
 */
interface WeatherRepository {
    /**
     * 取得所有城市天氣（Flow 即時更新）
     */
    fun getAllCityWeather(): Flow<List<CityWeather>>

    /**
     * 依 ID 取得單一城市天氣
     */
    suspend fun getCityWeatherById(id: Long): CityWeather?

    /**
     * 取得並儲存天氣資料
     */
    suspend fun fetchAndSaveWeather(lat: Double, lon: Double, cityName: String? = null): CityWeather

    /**
     * 檢查城市是否已存在（依 API 城市 ID）
     */
    suspend fun isCityExists(apiCityId: Long): Boolean

    /**
     * 新增城市（若已存在則更新）
     * @return 城市資訊與是否為新增
     */
    suspend fun addCityIfNotExists(lat: Double, lon: Double, cityName: String? = null): AddCityResult

    /**
     * 刷新單一城市天氣
     */
    suspend fun refreshWeather(cityWeather: CityWeather): CityWeather

    /**
     * 刷新所有城市天氣
     */
    suspend fun refreshAllWeather(): List<CityWeather>

    /**
     * 刪除城市天氣
     */
    suspend fun deleteCityWeather(id: Long)

    /**
     * 取得天氣預報（時段 + 每日）
     */
    suspend fun getForecast(lat: Double, lon: Double): ForecastResult
}
