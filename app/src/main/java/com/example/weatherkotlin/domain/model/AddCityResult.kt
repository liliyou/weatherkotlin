package com.example.weatherkotlin.domain.model

/**
 * 新增城市操作結果
 */
data class AddCityResult(
    val cityWeather: CityWeather,
    val isNew: Boolean
)
