package com.example.weatherkotlin.data.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * 日期時間格式化工具
 */
object DateTimeFormatter {

    /**
     * 格式化逐時天氣時間
     *
     * @param timestamp Unix timestamp（秒）
     * @param currentTimeMillis 當前時間（毫秒），預設為系統時間
     * @return "現在" 或 "HH時" 格式
     */
    fun formatHourlyTime(
        timestamp: Long,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): String {
        val now = currentTimeMillis / 1000
        return if (kotlin.math.abs(timestamp - now) < 3600) {
            "現在"
        } else {
            val sdf = SimpleDateFormat("HH時", Locale.TAIWAN)
            sdf.format(Date(timestamp * 1000))
        }
    }

    /**
     * 格式化星期幾
     *
     * @param dateStr 日期字串，格式 "yyyy-MM-dd"
     * @return "週一" ~ "週日" 或原始字串（解析失敗時）
     */
    fun formatDayOfWeek(dateStr: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
            val date = sdf.parse(dateStr) ?: return dateStr
            val calendar = Calendar.getInstance().apply { time = date }
            when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> "週日"
                Calendar.MONDAY -> "週一"
                Calendar.TUESDAY -> "週二"
                Calendar.WEDNESDAY -> "週三"
                Calendar.THURSDAY -> "週四"
                Calendar.FRIDAY -> "週五"
                Calendar.SATURDAY -> "週六"
                else -> dateStr
            }
        } catch (e: Exception) {
            dateStr
        }
    }
}
