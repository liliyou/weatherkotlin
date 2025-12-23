package com.example.weatherkotlin.data.mapper

import com.example.weatherkotlin.data.remote.dto.ForecastItem
import com.example.weatherkotlin.data.remote.dto.ForecastResponse
import com.example.weatherkotlin.data.util.DateTimeFormatter
import com.example.weatherkotlin.domain.model.DailyWeather
import com.example.weatherkotlin.domain.model.ForecastResult
import com.example.weatherkotlin.domain.model.HourlyWeather
import kotlin.math.roundToInt

/**
 * 天氣預報 DTO 轉換為 Domain Model
 */
object ForecastMapper {

    /**
     * 將 API 回應轉換為 ForecastResult
     *
     * @param response API 回應
     * @param currentTimeMillis 當前時間（毫秒），用於判斷「現在」
     * @return ForecastResult 包含逐時和每日天氣
     */
    fun toForecastResult(
        response: ForecastResponse,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): ForecastResult {
        val hourlyWeather = mapHourlyWeather(response.list, currentTimeMillis)
        val dailyWeather = mapDailyWeather(response.list)
        return ForecastResult(
            hourlyWeather = hourlyWeather,
            dailyWeather = dailyWeather
        )
    }

    /**
     * 轉換逐時天氣（取前 8 筆）
     */
    internal fun mapHourlyWeather(
        items: List<ForecastItem>,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): List<HourlyWeather> {
        return items.take(8).map { item ->
            val weather = item.weather.firstOrNull()
            HourlyWeather(
                time = DateTimeFormatter.formatHourlyTime(item.dt, currentTimeMillis),
                temp = item.main.temp.roundToInt(),
                weatherIcon = weather?.icon ?: "01d",
                weatherDescription = weather?.description ?: ""
            )
        }
    }

    /**
     * 轉換每日天氣（取前 5 天）
     */
    internal fun mapDailyWeather(items: List<ForecastItem>): List<DailyWeather> {
        val dailyMap = items.groupBy { item ->
            item.dtTxt.split(" ")[0]
        }

        return dailyMap.entries.take(5).mapIndexed { index, entry ->
            val dayItems = entry.value
            val highTemp = dayItems.maxOfOrNull { it.main.tempMax.roundToInt() } ?: 0
            val lowTemp = dayItems.minOfOrNull { it.main.tempMin.roundToInt() } ?: 0
            val middayItem = dayItems.find { it.dtTxt.contains("12:00:00") } ?: dayItems.first()
            val weather = middayItem.weather.firstOrNull()
            val dayOfWeek = if (index == 0) "今天" else DateTimeFormatter.formatDayOfWeek(entry.key)

            DailyWeather(
                dayOfWeek = dayOfWeek,
                highTemp = highTemp,
                lowTemp = lowTemp,
                weatherIcon = weather?.icon ?: "01d",
                weatherDescription = weather?.description ?: ""
            )
        }
    }
}
