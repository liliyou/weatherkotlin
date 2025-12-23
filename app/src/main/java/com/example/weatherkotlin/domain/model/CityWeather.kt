package com.example.weatherkotlin.domain.model

/**
 * 城市天氣 Domain Model
 */
data class CityWeather(
    val id: Long,
    val cityName: String,
    val country: String = "",
    val weatherDescription: String,
    val weatherIcon: String,
    val currentTemp: Int,
    val highTemp: Int,
    val lowTemp: Int,
    val lat: Double,
    val lon: Double
)
