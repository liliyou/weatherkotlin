package com.example.weatherkotlin.data.remote

import com.example.weatherkotlin.data.remote.dto.ForecastResponse
import com.example.weatherkotlin.data.remote.dto.GeoResponse
import com.example.weatherkotlin.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): ForecastResponse

    @GET("geo/1.0/direct")
    suspend fun searchCity(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5
    ): List<GeoResponse>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
        private const val ICON_BASE_URL = "https://openweathermap.org/img/wn/"

        /** 取得天氣圖示 URL（小尺寸 @2x） */
        fun getIconUrl(iconCode: String): String = "${ICON_BASE_URL}${iconCode}@2x.png"

        /** 取得天氣圖示 URL（大尺寸 @4x） */
        fun getLargeIconUrl(iconCode: String): String = "${ICON_BASE_URL}${iconCode}@4x.png"
    }
}
