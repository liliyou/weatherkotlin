package com.example.weatherkotlin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherkotlin.domain.model.CityWeather

@Entity(tableName = "city_weather")
data class CityWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val apiCityId: Long = 0,
    val cityName: String,
    val country: String = "",
    val weatherDescription: String,
    val weatherIcon: String,
    val currentTemp: Int,
    val highTemp: Int,
    val lowTemp: Int,
    val lat: Double,
    val lon: Double,
    val position: Int = 0
)

/**
 * 轉換為 Domain Model
 */
fun CityWeatherEntity.toDomainModel(): CityWeather {
    return CityWeather(
        id = id,
        apiCityId = apiCityId,
        cityName = cityName,
        country = country,
        weatherDescription = weatherDescription,
        weatherIcon = weatherIcon,
        currentTemp = currentTemp,
        highTemp = highTemp,
        lowTemp = lowTemp,
        lat = lat,
        lon = lon
    )
}
