package com.example.weatherkotlin.domain.model

/**
 * 每小時天氣 Domain Model
 */
data class HourlyWeather(
    val time: String,
    val temp: Int,
    val weatherIcon: String,
    val weatherDescription: String
)
