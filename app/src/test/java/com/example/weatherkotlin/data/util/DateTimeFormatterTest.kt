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
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val timestamp = calendar.timeInMillis / 1000

        val result = DateTimeFormatter.formatHourlyTime(timestamp)

        assertEquals("14", result)
    }

    @Test
    fun `formatHourlyTime returns correct format for morning`() {
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val timestamp = calendar.timeInMillis / 1000

        val result = DateTimeFormatter.formatHourlyTime(timestamp)

        assertEquals("09", result)
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
    fun `formatDayOfWeek returns Today for today`() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
        val today = sdf.format(Date())

        val result = DateTimeFormatter.formatDayOfWeek(today)

        assertEquals("Today", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Sunday`() {
        // 2024-12-22 is Sunday
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-22")
        assertEquals("Sun", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Monday`() {
        // 2024-12-23 is Monday
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-23")
        assertEquals("Mon", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Tuesday`() {
        // 2025-12-30 is Tuesday
        val result = DateTimeFormatter.formatDayOfWeek("2025-12-30")
        assertEquals("Tue", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Wednesday`() {
        // 2024-12-25 is Wednesday
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-25")
        assertEquals("Wed", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Thursday`() {
        // 2024-12-26 is Thursday
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-26")
        assertEquals("Thu", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Friday`() {
        // 2024-12-27 is Friday
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-27")
        assertEquals("Fri", result)
    }

    @Test
    fun `formatDayOfWeek returns correct day for Saturday`() {
        // 2024-12-28 is Saturday
        val result = DateTimeFormatter.formatDayOfWeek("2024-12-28")
        assertEquals("Sat", result)
    }

    @Test
    fun `formatDayOfWeek returns original string for invalid date`() {
        val invalidDate = "invalid-date"
        val result = DateTimeFormatter.formatDayOfWeek(invalidDate)
        assertEquals(invalidDate, result)
    }
}
