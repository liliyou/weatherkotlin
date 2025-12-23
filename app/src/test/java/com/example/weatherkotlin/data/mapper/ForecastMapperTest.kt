package com.example.weatherkotlin.data.mapper

import com.example.weatherkotlin.data.remote.dto.ForecastItem
import com.example.weatherkotlin.data.remote.dto.ForecastMain
import com.example.weatherkotlin.data.remote.dto.ForecastResponse
import com.example.weatherkotlin.data.remote.dto.Weather
import org.junit.Assert.assertEquals
import org.junit.Test

class ForecastMapperTest {

    @Test
    fun `mapHourlyWeather returns 現在 for first item within 1 hour`() {
        val now = System.currentTimeMillis()
        val items = listOf(
            createForecastItem(
                dt = now / 1000,
                temp = 25.0,
                icon = "01d",
                description = "晴天",
                dtTxt = "2024-12-23 12:00:00"
            )
        )

        val result = ForecastMapper.mapHourlyWeather(items, now)

        assertEquals(1, result.size)
        assertEquals("現在", result[0].time)
        assertEquals(25, result[0].temp)
        assertEquals("01d", result[0].weatherIcon)
        assertEquals("晴天", result[0].weatherDescription)
    }

    @Test
    fun `mapHourlyWeather takes only first 8 items`() {
        val now = System.currentTimeMillis()
        val items = (0..9).map { i ->
            createForecastItem(
                dt = (now / 1000) + (i * 3600),
                temp = 20.0 + i,
                icon = "01d",
                description = "晴天",
                dtTxt = "2024-12-23 ${12 + i}:00:00"
            )
        }

        val result = ForecastMapper.mapHourlyWeather(items, now)

        assertEquals(8, result.size)
    }

    @Test
    fun `mapHourlyWeather uses default icon when weather list is empty`() {
        val now = System.currentTimeMillis()
        val items = listOf(
            ForecastItem(
                dt = now / 1000,
                main = ForecastMain(temp = 25.0, tempMin = 20.0, tempMax = 30.0),
                weather = emptyList(),
                dtTxt = "2024-12-23 12:00:00"
            )
        )

        val result = ForecastMapper.mapHourlyWeather(items, now)

        assertEquals("01d", result[0].weatherIcon)
        assertEquals("", result[0].weatherDescription)
    }

    @Test
    fun `mapDailyWeather groups items by date`() {
        val items = listOf(
            createForecastItem(dt = 0, temp = 20.0, icon = "01d", description = "晴天", dtTxt = "2024-12-23 09:00:00"),
            createForecastItem(dt = 0, temp = 25.0, icon = "01d", description = "晴天", dtTxt = "2024-12-23 12:00:00"),
            createForecastItem(dt = 0, temp = 22.0, icon = "01d", description = "晴天", dtTxt = "2024-12-23 15:00:00"),
            createForecastItem(dt = 0, temp = 18.0, icon = "02d", description = "多雲", dtTxt = "2024-12-24 09:00:00"),
            createForecastItem(dt = 0, temp = 23.0, icon = "02d", description = "多雲", dtTxt = "2024-12-24 12:00:00")
        )

        val result = ForecastMapper.mapDailyWeather(items)

        assertEquals(2, result.size)
    }

    @Test
    fun `mapDailyWeather returns 今天 for first day`() {
        val items = listOf(
            createForecastItem(dt = 0, temp = 25.0, icon = "01d", description = "晴天", dtTxt = "2024-12-23 12:00:00")
        )

        val result = ForecastMapper.mapDailyWeather(items)

        assertEquals("今天", result[0].dayOfWeek)
    }

    @Test
    fun `mapDailyWeather calculates correct high and low temps`() {
        val items = listOf(
            createForecastItem(
                dt = 0,
                temp = 20.0,
                tempMin = 18.0,
                tempMax = 22.0,
                icon = "01d",
                description = "晴天",
                dtTxt = "2024-12-23 09:00:00"
            ),
            createForecastItem(
                dt = 0,
                temp = 28.0,
                tempMin = 25.0,
                tempMax = 30.0,
                icon = "01d",
                description = "晴天",
                dtTxt = "2024-12-23 12:00:00"
            ),
            createForecastItem(
                dt = 0,
                temp = 22.0,
                tempMin = 20.0,
                tempMax = 24.0,
                icon = "01d",
                description = "晴天",
                dtTxt = "2024-12-23 15:00:00"
            )
        )

        val result = ForecastMapper.mapDailyWeather(items)

        assertEquals(30, result[0].highTemp)
        assertEquals(18, result[0].lowTemp)
    }

    @Test
    fun `mapDailyWeather prefers 12 00 item for weather icon`() {
        val items = listOf(
            createForecastItem(dt = 0, temp = 20.0, icon = "09d", description = "雨天", dtTxt = "2024-12-23 09:00:00"),
            createForecastItem(dt = 0, temp = 25.0, icon = "01d", description = "晴天", dtTxt = "2024-12-23 12:00:00"),
            createForecastItem(dt = 0, temp = 22.0, icon = "04d", description = "多雲", dtTxt = "2024-12-23 15:00:00")
        )

        val result = ForecastMapper.mapDailyWeather(items)

        assertEquals("01d", result[0].weatherIcon)
        assertEquals("晴天", result[0].weatherDescription)
    }

    @Test
    fun `mapDailyWeather takes only first 5 days`() {
        val items = (0..6).flatMap { day ->
            listOf(
                createForecastItem(
                    dt = 0,
                    temp = 25.0,
                    icon = "01d",
                    description = "晴天",
                    dtTxt = "2024-12-${23 + day} 12:00:00"
                )
            )
        }

        val result = ForecastMapper.mapDailyWeather(items)

        assertEquals(5, result.size)
    }

    @Test
    fun `toForecastResult returns complete forecast`() {
        val now = System.currentTimeMillis()
        val response = ForecastResponse(
            list = listOf(
                createForecastItem(
                    dt = now / 1000,
                    temp = 25.0,
                    icon = "01d",
                    description = "晴天",
                    dtTxt = "2024-12-23 12:00:00"
                ),
                createForecastItem(
                    dt = (now / 1000) + 3600,
                    temp = 26.0,
                    icon = "01d",
                    description = "晴天",
                    dtTxt = "2024-12-23 15:00:00"
                )
            )
        )

        val result = ForecastMapper.toForecastResult(response, now)

        assertEquals(2, result.hourlyWeather.size)
        assertEquals(1, result.dailyWeather.size)
    }

    private fun createForecastItem(
        dt: Long,
        temp: Double,
        tempMin: Double = temp - 2,
        tempMax: Double = temp + 2,
        icon: String,
        description: String,
        dtTxt: String
    ): ForecastItem {
        return ForecastItem(
            dt = dt,
            main = ForecastMain(temp = temp, tempMin = tempMin, tempMax = tempMax),
            weather = listOf(Weather(description = description, icon = icon)),
            dtTxt = dtTxt
        )
    }
}
