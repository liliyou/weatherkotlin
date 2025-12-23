package com.example.weathercard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathercard.model.DailyWeather
import com.example.weathercard.model.PreviewData
import com.example.weathercard.theme.WeatherTextPrimary
import com.example.weathercard.theme.WeatherTextSecondary
import com.example.weathercard.theme.weatherCardStyle

/**
 * 7 日天氣預報列表
 *
 * @param dailyWeatherList 每日天氣資料列表
 * @param modifier Modifier
 */
@Composable
fun DailyWeatherColumn(
    dailyWeatherList: List<DailyWeather>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp)
            .weatherCardStyle()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "7 日天氣",
            color = WeatherTextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        dailyWeatherList.forEachIndexed { index, dailyWeather ->
            DailyWeatherItem(
                dailyWeather = dailyWeather,
                isToday = index == 0
            )
        }
    }
}

@Composable
private fun DailyWeatherItem(
    dailyWeather: DailyWeather,
    isToday: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        WeatherAnimatedIcon(
            weatherIcon = dailyWeather.weatherIcon,
            contentDescription = dailyWeather.weatherDescription,
            size = 36.dp
        )
        // 星期
        Text(
            text = dailyWeather.dayOfWeek,
            color = if (isToday) WeatherTextPrimary else WeatherTextSecondary,
            fontSize = 14.sp,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        // 天氣描述
        Text(
            text = dailyWeather.weatherDescription,
            color = WeatherTextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        // 最低溫
        Text(
            text = "${dailyWeather.lowTemp}°",
            color = WeatherTextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End
        )
        // 最高溫
        Text(
            text = "${dailyWeather.highTemp}°",
            color = WeatherTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E2A47)
@Composable
private fun DailyWeatherColumnPreview() {
    DailyWeatherColumn(
        dailyWeatherList = PreviewData.sampleDailyWeather
    )
}
