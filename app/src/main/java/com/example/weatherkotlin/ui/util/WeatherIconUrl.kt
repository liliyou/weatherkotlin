package com.example.weatherkotlin.ui.util

/**
 * 天氣圖示 URL 工具
 */
object WeatherIconUrl {
    private const val ICON_BASE_URL = "https://openweathermap.org/img/wn/"

    /** 取得天氣圖示 URL（小尺寸 @2x） */
    fun getIconUrl(iconCode: String): String = "${ICON_BASE_URL}${iconCode}@2x.png"

    /** 取得天氣圖示 URL（大尺寸 @4x） */
    fun getLargeIconUrl(iconCode: String): String = "${ICON_BASE_URL}${iconCode}@4x.png"
}
