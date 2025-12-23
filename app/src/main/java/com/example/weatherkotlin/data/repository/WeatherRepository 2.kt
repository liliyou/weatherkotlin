package com.example.weatherkotlin.data.repository

import com.example.weatherkotlin.data.local.CityWeatherDao
import com.example.weatherkotlin.data.local.CityWeatherEntity
import com.example.weatherkotlin.data.local.toCityWeather
import com.example.weatherkotlin.data.model.CityWeather
import com.example.weatherkotlin.data.remote.WeatherApi
import com.example.weatherkotlin.data.remote.dto.GeoResponse
import com.example.weatherkotlin.data.remote.dto.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val cityWeatherDao: CityWeatherDao,
    private val apiKey: String
) {

    fun getAllCityWeather(): Flow<List<CityWeather>> {
        return cityWeatherDao.getAllCityWeather().map { entities ->
            entities.map { it.toCityWeather() }
        }
    }

    suspend fun fetchAndSaveWeather(lat: Double, lon: Double, cityName: String? = null): CityWeather {
        val response = weatherApi.getWeather(
            lat = lat,
            lon = lon,
            apiKey = apiKey
        )
        val entity = response.toEntity(cityName)
        val id = cityWeatherDao.insertCityWeather(entity)
        return entity.copy(id = id).toCityWeather()
    }

    suspend fun refreshWeather(cityWeather: CityWeather): CityWeather {
        val response = weatherApi.getWeather(
            lat = cityWeather.lat,
            lon = cityWeather.lon,
            apiKey = apiKey
        )
        val entity = response.toEntity(cityWeather.cityName).copy(id = cityWeather.id)
        cityWeatherDao.updateCityWeather(entity)
        return entity.toCityWeather()
    }

    suspend fun refreshAllWeather(): List<CityWeather> {
        val cities = cityWeatherDao.getAllCityWeather()
        val result = mutableListOf<CityWeather>()
        cities.collect { entities ->
            entities.forEach { entity ->
                try {
                    val response = weatherApi.getWeather(
                        lat = entity.lat,
                        lon = entity.lon,
                        apiKey = apiKey
                    )
                    val updatedEntity = response.toEntity(entity.cityName).copy(id = entity.id)
                    cityWeatherDao.updateCityWeather(updatedEntity)
                    result.add(updatedEntity.toCityWeather())
                } catch (e: Exception) {
                    result.add(entity.toCityWeather())
                }
            }
        }
        return result
    }

    suspend fun deleteCityWeather(id: Long) {
        cityWeatherDao.deleteCityWeatherById(id)
    }

    suspend fun searchCity(query: String): List<GeoResponse> {
        return weatherApi.searchCity(query = query, apiKey = apiKey)
    }

    private fun WeatherResponse.toEntity(overrideName: String? = null): CityWeatherEntity {
        val weatherInfo = weather.firstOrNull()
        return CityWeatherEntity(
            cityName = overrideName ?: name,
            weatherDescription = weatherInfo?.description ?: "",
            weatherIcon = weatherInfo?.icon ?: "01d",
            currentTemp = main.temp.roundToInt(),
            highTemp = main.tempMax.roundToInt(),
            lowTemp = main.tempMin.roundToInt(),
            lat = coord.lat,
            lon = coord.lon
        )
    }
}
