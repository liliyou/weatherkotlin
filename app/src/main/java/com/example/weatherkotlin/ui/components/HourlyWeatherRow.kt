package com.example.weatherkotlin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherkotlin.data.model.HourlyWeather
import com.example.weatherkotlin.data.model.PreviewData
import com.example.weatherkotlin.ui.theme.WeatherCardBackground
import com.example.weatherkotlin.ui.theme.WeatherTextPrimary
import com.example.weatherkotlin.ui.theme.WeatherkotlinTheme

@Composable
fun HourlyWeatherRow(
    hourlyWeatherList: List<HourlyWeather>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(WeatherCardBackground)
            .padding(vertical = 12.dp)
    ) {
        // 標題列：今日天氣 + 滑動指示
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "今日天氣",
                color = WeatherTextPrimary,
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "向右滑動",
                tint = WeatherTextPrimary.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(hourlyWeatherList) { hourlyWeather ->
                HourlyWeatherItem(hourlyWeather = hourlyWeather)
            }
        }
    }
}

@Composable
private fun HourlyWeatherItem(
    hourlyWeather: HourlyWeather,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hourlyWeather.weatherDescription,
            color = WeatherTextPrimary,
            fontSize = 10.sp
        )
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${hourlyWeather.weatherIcon}@2x.png",
            contentDescription = hourlyWeather.weatherDescription,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = "${hourlyWeather.temp}°",
            color = WeatherTextPrimary,
            fontSize = 14.sp
        )
        Text(
            text = hourlyWeather.time,
            color = WeatherTextPrimary,
            fontSize = 10.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E2A47)
@Composable
private fun HourlyWeatherRowPreview() {
    WeatherkotlinTheme {
        HourlyWeatherRow(
            hourlyWeatherList = PreviewData.sampleHourlyWeather
        )
    }
}
