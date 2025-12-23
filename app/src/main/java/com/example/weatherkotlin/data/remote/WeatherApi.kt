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
        @Query("lang") lang: String = "zh_tw",
        @Query("appid") apiKey: String
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "zh_tw",
        @Query("appid") apiKey: String
    ): ForecastResponse

    @GET("geo/1.0/direct")
    suspend fun searchCity(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<GeoResponse>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }
}
