package com.example.weatherkotlin.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherkotlin.domain.model.HourlyWeather
import com.example.weatherkotlin.domain.model.PreviewData
import com.example.weatherkotlin.ui.theme.WeatherCardBackground
import com.example.weatherkotlin.ui.theme.WeatherTextPrimary
import com.example.weatherkotlin.ui.theme.WeatherTextSecondary
import com.example.weatherkotlin.ui.theme.WeatherkotlinTheme
import com.example.weatherkotlin.ui.theme.weatherCardStyle

@Composable
fun HourlyWeatherRow(
    hourlyWeatherList: List<HourlyWeather>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val canScrollRight by remember {
        derivedStateOf {
            listState.canScrollForward
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp)
            .weatherCardStyle()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "今日天氣",
            color = WeatherTextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Box {
            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .drawWithContent {
                        drawContent()
                        if (canScrollRight) {
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        WeatherCardBackground
                                    ),
                                    startX = size.width - 60.dp.toPx(),
                                    endX = size.width
                                )
                            )
                        }
                    }
            ) {
                items(hourlyWeatherList) { hourlyWeather ->
                    HourlyWeatherItem(hourlyWeather = hourlyWeather)
                }
            }
        }
    }
}

@Composable
private fun HourlyWeatherItem(
    hourlyWeather: HourlyWeather,
    modifier: Modifier = Modifier
) {
    val isNow = hourlyWeather.time == "現在"

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = hourlyWeather.time,
            color = if (isNow) WeatherTextPrimary else WeatherTextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isNow) FontWeight.Bold else FontWeight.Normal
        )
        WeatherAnimatedIcon(
            weatherIcon = hourlyWeather.weatherIcon,
            contentDescription = hourlyWeather.weatherDescription,
            size = 44.dp
        )
        Text(
            text = "${hourlyWeather.temp}°",
            color = WeatherTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = hourlyWeather.weatherDescription,
            color = WeatherTextSecondary,
            fontSize = 11.sp
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
