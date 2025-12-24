package com.example.weatherkotlin.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import coil.compose.AsyncImage
import com.example.weatherkotlin.data.remote.WeatherApi
import com.example.weatherkotlin.domain.model.DailyWeather
import com.example.weatherkotlin.ui.preview.PreviewData
import com.example.weatherkotlin.ui.theme.WeatherTextPrimary
import com.example.weatherkotlin.ui.theme.WeatherTextSecondary
import com.example.weatherkotlin.ui.theme.WeatherkotlinTheme
import com.example.weatherkotlin.ui.theme.weatherCardStyle

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
            text = "5 日天氣",
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
        AsyncImage(
            model = WeatherApi.getIconUrl(dailyWeather.weatherIcon),
            contentDescription = dailyWeather.weatherDescription,
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = dailyWeather.dayOfWeek,
            color = if (isToday) WeatherTextPrimary else WeatherTextSecondary,
            fontSize = 14.sp,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = dailyWeather.weatherDescription,
            color = WeatherTextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "${dailyWeather.lowTemp}°",
            color = WeatherTextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End
        )
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
    WeatherkotlinTheme {
        DailyWeatherColumn(
            dailyWeatherList = PreviewData.sampleDailyWeather
        )
    }
}
