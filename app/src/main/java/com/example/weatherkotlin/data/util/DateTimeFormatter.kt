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
     * @return "HH時" 格式
     */
    fun formatHourlyTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH時", Locale.TAIWAN)
        return sdf.format(Date(timestamp * 1000))
    }

    /**
     * 檢查日期是否為今天
     *
     * @param dateStr 日期字串，格式 "yyyy-MM-dd"
     * @return true 如果是今天
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
     * 格式化星期幾
     *
     * @param dateStr 日期字串，格式 "yyyy-MM-dd"
     * @return "今天"、"週一" ~ "週日" 或原始字串（解析失敗時）
     */
    fun formatDayOfWeek(dateStr: String): String {
        if (isToday(dateStr)) return "今天"
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
