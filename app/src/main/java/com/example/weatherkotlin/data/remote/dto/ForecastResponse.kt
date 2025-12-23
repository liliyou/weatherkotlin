package com.example.weatherkotlin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("list") val list: List<ForecastItem>
)

data class ForecastItem(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: ForecastMain,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("dt_txt") val dtTxt: String
)

data class ForecastMain(
    @SerializedName("temp") val temp: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double
)
