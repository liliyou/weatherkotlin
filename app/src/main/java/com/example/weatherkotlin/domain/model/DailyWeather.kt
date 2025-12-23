package com.example.weatherkotlin.domain.model

/**
 * 每日天氣 Domain Model
 */
data class DailyWeather(
    val dayOfWeek: String,
    val highTemp: Int,
    val lowTemp: Int,
    val weatherIcon: String,
    val weatherDescription: String
)
