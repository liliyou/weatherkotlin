package com.example.weatherkotlin.data.repository

import com.example.weatherkotlin.data.local.CityWeatherDao
import com.example.weatherkotlin.data.local.CityWeatherEntity
import com.example.weatherkotlin.data.local.toDomainModel
import com.example.weatherkotlin.data.remote.WeatherApi
import com.example.weatherkotlin.data.remote.dto.ForecastResponse
import com.example.weatherkotlin.data.remote.dto.GeoResponse
import com.example.weatherkotlin.data.remote.dto.WeatherResponse
import com.example.weatherkotlin.domain.model.CityWeather
import com.example.weatherkotlin.domain.model.DailyWeather
import com.example.weatherkotlin.domain.model.ForecastResult
import com.example.weatherkotlin.domain.model.HourlyWeather
import com.example.weatherkotlin.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val cityWeatherDao: CityWeatherDao,
    private val apiKey: String
) : WeatherRepository {

    override fun getAllCityWeather(): Flow<List<CityWeather>> {
        return cityWeatherDao.getAllCityWeather().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun fetchAndSaveWeather(lat: Double, lon: Double, cityName: String?): CityWeather {
        val response = weatherApi.getWeather(lat = lat, lon = lon, apiKey = apiKey)
        val entity = response.toEntity(cityName)
        val id = cityWeatherDao.insertCityWeather(entity)
        return entity.copy(id = id).toDomainModel()
    }

    override suspend fun isCityExists(cityName: String): Boolean {
        return cityWeatherDao.getCityByName(cityName) != null
    }

    override suspend fun addCityIfNotExists(lat: Double, lon: Double): Boolean {
        val response = weatherApi.getWeather(lat = lat, lon = lon, apiKey = apiKey)
        val cityName = response.name
        val existing = cityWeatherDao.getCityByName(cityName)
        return if (existing == null) {
            val entity = response.toEntity()
            cityWeatherDao.insertCityWeather(entity)
            true
        } else {
            false
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

    /**
     * 搜尋城市（給 SearchRepositoryImpl 使用）
     */
    suspend fun searchCity(query: String): List<GeoResponse> {
        return weatherApi.searchCity(query = query, apiKey = apiKey)
    }

    private fun ForecastResponse.toForecastResult(): ForecastResult {
        val hourlyWeather = list.take(8).map { item ->
            val time = formatHourlyTime(item.dt)
            val weather = item.weather.firstOrNull()
            HourlyWeather(
                time = time,
                temp = item.main.temp.roundToInt(),
                weatherIcon = weather?.icon ?: "01d",
                weatherDescription = weather?.description ?: ""
            )
        }

        val dailyMap = mutableMapOf<String, MutableList<com.example.weatherkotlin.data.remote.dto.ForecastItem>>()
        list.forEach { item ->
            val dateKey = item.dtTxt.split(" ")[0]
            dailyMap.getOrPut(dateKey) { mutableListOf() }.add(item)
        }

        val dailyWeather = dailyMap.entries.take(5).mapIndexed { index, entry ->
            val items = entry.value
            val highTemp = items.maxOfOrNull { it.main.tempMax.roundToInt() } ?: 0
            val lowTemp = items.minOfOrNull { it.main.tempMin.roundToInt() } ?: 0
            val middayItem = items.find { it.dtTxt.contains("12:00:00") } ?: items.first()
            val weather = middayItem.weather.firstOrNull()
            val dayOfWeek = if (index == 0) "今天" else formatDayOfWeek(entry.key)
            DailyWeather(
                dayOfWeek = dayOfWeek,
                highTemp = highTemp,
                lowTemp = lowTemp,
                weatherIcon = weather?.icon ?: "01d",
                weatherDescription = weather?.description ?: ""
            )
        }

        return ForecastResult(
            hourlyWeather = hourlyWeather,
            dailyWeather = dailyWeather
        )
    }

    private fun formatHourlyTime(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis / 1000
        return if (kotlin.math.abs(timestamp - now) < 3600) {
            "現在"
        } else {
            val sdf = SimpleDateFormat("HH時", Locale.TAIWAN)
            sdf.format(Date(timestamp * 1000))
        }
    }

    private fun formatDayOfWeek(dateStr: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
        val date = sdf.parse(dateStr) ?: return dateStr
        val calendar = Calendar.getInstance().apply { time = date }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "週日"
            Calendar.MONDAY -> "週一"
            Calendar.TUESDAY -> "週二"
            Calendar.WEDNESDAY -> "週三"
            Calendar.THURSDAY -> "週四"
            Calendar.FRIDAY -> "週五"
            Calendar.SATURDAY -> "週六"
            else -> dateStr
        }
    }

    private fun WeatherResponse.toEntity(overrideName: String? = null): CityWeatherEntity {
        val weatherInfo = weather.firstOrNull()
        return CityWeatherEntity(
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
