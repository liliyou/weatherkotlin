package com.example.weatherkotlin.data.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Date time formatting utility
 */
object DateTimeFormatter {

    /**
     * Format hourly weather time
     *
     * @param timestamp Unix timestamp (seconds)
     * @return "HH" format (e.g., "14")
     */
    fun formatHourlyTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH", Locale.TAIWAN)
        return sdf.format(Date(timestamp * 1000))
    }

    /**
     * Check if date is today
     *
     * @param dateStr Date string, format "yyyy-MM-dd"
     * @return true if today
     */
    fun isToday(dateStr: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
            val today = sdf.format(Date())
            dateStr == today
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Format day of week
     *
     * @param dateStr Date string, format "yyyy-MM-dd"
     * @return "Today", "Mon" ~ "Sun" or original string (on parse failure)
     */
    fun formatDayOfWeek(dateStr: String): String {
        if (isToday(dateStr)) return "Today"
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
            val date = sdf.parse(dateStr) ?: return dateStr
            val calendar = Calendar.getInstance().apply { time = date }
            when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> "Sun"
                Calendar.MONDAY -> "Mon"
                Calendar.TUESDAY -> "Tue"
                Calendar.WEDNESDAY -> "Wed"
                Calendar.THURSDAY -> "Thu"
                Calendar.FRIDAY -> "Fri"
                Calendar.SATURDAY -> "Sat"
                else -> dateStr
            }
        } catch (e: Exception) {
            dateStr
        }
    }
}
