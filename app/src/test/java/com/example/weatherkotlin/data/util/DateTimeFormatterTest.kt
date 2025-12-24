package com.example.weatherkotlin.data.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateTimeFormatterTest {

    @Test
    fun `formatHourlyTime returns formatted time`() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei")).apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val timestamp = calendar.timeInMillis / 1000

        val result = DateTimeFormatter.formatHourlyTime(timestamp)

        assertEquals("14時", result)
    }

    @Test
    fun `formatHourlyTime returns correct format for morning`() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei")).apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val timestamp = calendar.timeInMillis / 1000

        val result = DateTimeFormatter.formatHourlyTime(timestamp)

        assertEquals("09時", result)
    }

    @Test
    fun `isToday returns true for today`() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
        val today = sdf.format(Date())

        val result = DateTimeFormatter.isToday(today)

        assertTrue(result)
    }

    @Test
    fun `isToday returns false for yesterday`() {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -1)
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
        val yesterday = sdf.format(calendar.time)

        val result = DateTimeFormatter.isToday(yesterday)

        assertFalse(result)
    }

    @Test
    fun `isToday returns false for invalid date`() {
        val result = DateTimeFormatter.isToday("invalid-date")

        assertFalse(result)
    }

    @Test
    fun `formatDayOfWeek returns 今天 for today`() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
        val today = sdf.format(Date())

        val result = DateTimeFormatter.formatDayOfWeek(today)

        assertEquals("今天", result)
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
        // 2024-12-24 是星期二（今天會返回「今天」，所以用未來日期）
        val result = DateTimeFormatter.formatDayOfWeek("2025-12-30")
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
