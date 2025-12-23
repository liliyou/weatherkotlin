package com.example.weatherkotlin.data.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class DateTimeFormatterTest {

    @Test
    fun `formatHourlyTime returns 現在 when within 1 hour`() {
        val now = System.currentTimeMillis()
        val timestamp = now / 1000 // 當前時間的 Unix timestamp

        val result = DateTimeFormatter.formatHourlyTime(timestamp, now)

        assertEquals("現在", result)
    }

    @Test
    fun `formatHourlyTime returns 現在 when 30 minutes ago`() {
        val now = System.currentTimeMillis()
        val timestamp = (now / 1000) - (30 * 60) // 30 分鐘前

        val result = DateTimeFormatter.formatHourlyTime(timestamp, now)

        assertEquals("現在", result)
    }

    @Test
    fun `formatHourlyTime returns formatted time when more than 1 hour`() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei")).apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val timestamp = calendar.timeInMillis / 1000
        val now = timestamp - (2 * 3600) // 2 小時前

        val result = DateTimeFormatter.formatHourlyTime(timestamp, now * 1000)

        assertEquals("14時", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Sunday`() {
        // 2024-12-22 是星期日
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-22")
        assertEquals("週日", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Monday`() {
        // 2024-12-23 是星期一
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-23")
        assertEquals("週一", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Tuesday`() {
        // 2024-12-24 是星期二
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-24")
        assertEquals("週二", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Wednesday`() {
        // 2024-12-25 是星期三
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-25")
        assertEquals("週三", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Thursday`() {
        // 2024-12-26 是星期四
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-26")
        assertEquals("週四", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Friday`() {
        // 2024-12-27 是星期五
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-27")
        assertEquals("週五", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Saturday`() {
        // 2024-12-28 是星期六
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-28")
        assertEquals("週六", result)
    }

    @Test
    fun `formatDayOfWeek returns original string for invalid date`() {
        val invalidDate = "invalid-date"
        val result = DateTimeFormatter.formatDayOfWeek(invalidDate)
        assertEquals(invalidDate, result)
    }
}
