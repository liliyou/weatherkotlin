package com.example.weatherkotlin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherkotlin.data.model.DailyWeather
import com.example.weatherkotlin.data.model.PreviewData
import com.example.weatherkotlin.ui.theme.WeatherCardBackground
import com.example.weatherkotlin.ui.theme.WeatherTextPrimary
import com.example.weatherkotlin.ui.theme.WeatherkotlinTheme

@Composable
fun DailyWeatherColumn(
    dailyWeatherList: List<DailyWeather>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(WeatherCardBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "7日天氣",
            color = WeatherTextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        dailyWeatherList.forEach { dailyWeather ->
            DailyWeatherItem(dailyWeather = dailyWeather)
        }
    }
}

@Composable
private fun DailyWeatherItem(
    dailyWeather: DailyWeather,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${dailyWeather.weatherIcon}@2x.png",
            contentDescription = dailyWeather.weatherDescription,
            modifier = Modifier.size(32.dp)
        )
        // 星期
        Text(
            text = dailyWeather.dayOfWeek,
            color = WeatherTextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        // 天氣描述
        Text(
            text = dailyWeather.weatherDescription,
            color = WeatherTextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        // 最低溫
        Text(
            text = "${dailyWeather.lowTemp}°C",
            color = WeatherTextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.7f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
        // 最高溫
        Text(
            text = "${dailyWeather.highTemp}°C",
            color = WeatherTextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.7f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E2A47)
@Composable
private fun DailyWeatherColumnPreview() {
    WeatherkotlinTheme {
        DailyWeatherColumn(
            dailyWeatherList = PreviewData.sampleDailyWeather
        )
    }
}
