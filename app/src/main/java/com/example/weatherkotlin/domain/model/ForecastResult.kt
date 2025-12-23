package com.example.weatherkotlin.domain.model

/**
 * 天氣預報結果 Domain Model
 */
data class ForecastResult(
    val hourlyWeather: List<HourlyWeather>,
    val dailyWeather: List<DailyWeather>
)
