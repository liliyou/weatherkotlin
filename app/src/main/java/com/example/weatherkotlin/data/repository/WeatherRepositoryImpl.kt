package com.example.weatherkotlin.data.repository

import com.example.weatherkotlin.data.local.CityWeatherDao
import com.example.weatherkotlin.data.local.CityWeatherEntity
import com.example.weatherkotlin.data.local.toDomainModel
import com.example.weatherkotlin.data.remote.WeatherApi
import com.example.weatherkotlin.data.remote.dto.ForecastResponse
import com.example.weatherkotlin.data.remote.dto.WeatherResponse
import com.example.weatherkotlin.data.util.DateTimeFormatter
import com.example.weatherkotlin.domain.model.AddCityResult
import com.example.weatherkotlin.domain.model.CityWeather
import com.example.weatherkotlin.domain.model.DailyWeather
import com.example.weatherkotlin.domain.model.ForecastResult
import com.example.weatherkotlin.domain.model.HourlyWeather
import com.example.weatherkotlin.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val cityWeatherDao: CityWeatherDao,
    @Named("apiKey") private val apiKey: String
) : WeatherRepository {

    override fun getAllCityWeather(): Flow<List<CityWeather>> {
        return cityWeatherDao.getAllCityWeather().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getCityWeatherById(id: Long): CityWeather? {
        return cityWeatherDao.getCityWeatherById(id)?.toDomainModel()
    }

    override suspend fun fetchAndSaveWeather(lat: Double, lon: Double, cityName: String?): CityWeather {
        val response = weatherApi.getWeather(lat = lat, lon = lon, apiKey = apiKey)
        val entity = response.toEntity(cityName)
        val id = cityWeatherDao.insertCityWeather(entity)
        return entity.copy(id = id).toDomainModel()
    }

    override suspend fun isCityExists(apiCityId: Long): Boolean {
        return cityWeatherDao.getCityByApiId(apiCityId) != null
    }

    override suspend fun addCityIfNotExists(lat: Double, lon: Double, cityName: String?): AddCityResult {
        val response = weatherApi.getWeather(lat = lat, lon = lon, apiKey = apiKey)
        val existing = cityWeatherDao.getCityByApiId(response.id)
        return if (existing == null) {
            val entity = response.toEntity(cityName)
            val id = cityWeatherDao.insertCityWeather(entity)
            AddCityResult(
                cityWeather = entity.copy(id = id).toDomainModel(),
                isNew = true
            )
        } else {
            // 城市已存在，更新資料並移到最上方
            val updatedEntity = response.toEntity(existing.cityName).copy(id = existing.id)
            cityWeatherDao.updateCityWeather(updatedEntity)
            AddCityResult(
                cityWeather = updatedEntity.toDomainModel(),
                isNew = false
            )
        }
    }

    override suspend fun refreshWeather(cityWeather: CityWeather): CityWeather {
        val response = weatherApi.getWeather(lat = cityWeather.lat, lon = cityWeather.lon, apiKey = apiKey)
        val entity = response.toEntity(cityWeather.cityName).copy(id = cityWeather.id)
        cityWeatherDao.updateCityWeather(entity)
        return entity.toDomainModel()
    }

    override suspend fun refreshAllWeather(): List<CityWeather> {
        val entities = cityWeatherDao.getAllCityWeather().first()
        return entities.map { entity ->
            try {
                val response = weatherApi.getWeather(lat = entity.lat, lon = entity.lon, apiKey = apiKey)
                val updatedEntity = response.toEntity(entity.cityName).copy(id = entity.id)
                cityWeatherDao.updateCityWeather(updatedEntity)
                updatedEntity.toDomainModel()
            } catch (e: Exception) {
                entity.toDomainModel()
            }
        }
    }

    override suspend fun deleteCityWeather(id: Long) {
        cityWeatherDao.deleteCityWeatherById(id)
    }

    override suspend fun getForecast(lat: Double, lon: Double): ForecastResult {
        val response = weatherApi.getForecast(lat = lat, lon = lon, apiKey = apiKey)
        return response.toForecastResult()
    }

    private fun ForecastResponse.toForecastResult(): ForecastResult {
        return ForecastResult(
            hourlyWeather = list.take(8).map { item ->
                val weather = item.weather.firstOrNull()
                HourlyWeather(
                    time = DateTimeFormatter.formatHourlyTime(item.dt),
                    temp = item.main.temp.roundToInt(),
                    weatherIcon = weather?.icon ?: "01d",
                    weatherDescription = weather?.description ?: ""
                )
            },
            dailyWeather = list
                .groupBy { it.dtTxt.split(" ")[0] }
                .entries
                .take(5)
                .map { entry ->
                    val dayItems = entry.value
                    val middayItem = dayItems.find { it.dtTxt.contains("12:00:00") } ?: dayItems.first()
                    val weather = middayItem.weather.firstOrNull()
                    DailyWeather(
                        dayOfWeek = DateTimeFormatter.formatDayOfWeek(entry.key),
                        highTemp = dayItems.maxOfOrNull { it.main.tempMax.roundToInt() } ?: 0,
                        lowTemp = dayItems.minOfOrNull { it.main.tempMin.roundToInt() } ?: 0,
                        weatherIcon = weather?.icon ?: "01d",
                        weatherDescription = weather?.description ?: ""
                    )
                }
        )
    }

    private fun WeatherResponse.toEntity(overrideName: String? = null): CityWeatherEntity {
        val weatherInfo = weather.firstOrNull()
        return CityWeatherEntity(
            apiCityId = id,
            cityName = overrideName ?: name,
            country = sys.country,
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
