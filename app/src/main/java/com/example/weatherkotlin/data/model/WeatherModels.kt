package com.example.weatherkotlin.data.model

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

data class HourlyWeather(
    val time: String,
    val temp: Int,
    val weatherIcon: String,
    val weatherDescription: String
)

data class DailyWeather(
    val dayOfWeek: String,
    val highTemp: Int,
    val lowTemp: Int,
    val weatherIcon: String,
    val weatherDescription: String
)

object PreviewData {
    val sampleCityWeather = CityWeather(
        id = 1,
        cityName = "台中市",
        country = "TW",
        weatherDescription = "晴",
        weatherIcon = "01d",
        currentTemp = 27,
        highTemp = 30,
        lowTemp = 22,
        lat = 24.1477,
        lon = 120.6736
    )

    val sampleCityWeatherList = listOf(
        sampleCityWeather,
        CityWeather(
            id = 2,
            cityName = "台北市",
            country = "TW",
            weatherDescription = "多雲",
            weatherIcon = "02d",
            currentTemp = 25,
            highTemp = 28,
            lowTemp = 20,
            lat = 25.0330,
            lon = 121.5654
        )
    )

    val sampleHourlyWeather = listOf(
        HourlyWeather("現在", 27, "01d", "晴"),
        HourlyWeather("14時", 28, "02d", "多雲"),
        HourlyWeather("15時", 27, "02d", "多雲"),
        HourlyWeather("16時", 26, "03d", "陰"),
        HourlyWeather("17時", 25, "04d", "陰"),
        HourlyWeather("18時", 24, "10d", "小雨"),
        HourlyWeather("19時", 23, "10d", "小雨"),
        HourlyWeather("20時", 22, "01n", "晴")
    )

    val sampleDailyWeather = listOf(
        DailyWeather("今天", 30, 22, "01d", "晴"),
        DailyWeather("週一", 28, 20, "02d", "多雲"),
        DailyWeather("週二", 26, 19, "10d", "小雨"),
        DailyWeather("週三", 25, 18, "10d", "小雨"),
        DailyWeather("週四", 27, 19, "02d", "多雲"),
        DailyWeather("週五", 29, 21, "01d", "晴"),
        DailyWeather("週六", 30, 22, "01d", "晴")
    )
}
