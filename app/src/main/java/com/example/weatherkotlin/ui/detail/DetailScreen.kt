package com.example.weatherkotlin.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherkotlin.domain.model.DailyWeather
import com.example.weatherkotlin.domain.model.HourlyWeather
import com.example.weatherkotlin.domain.model.PreviewData
import coil.compose.AsyncImage
import com.example.weatherkotlin.data.remote.WeatherApi
import com.example.weatherkotlin.ui.components.DailyWeatherColumn
import com.example.weatherkotlin.ui.components.HourlyWeatherRow
import com.example.weatherkotlin.ui.theme.WeatherBackground
import com.example.weatherkotlin.ui.theme.WeatherTextPrimary
import com.example.weatherkotlin.ui.theme.WeatherkotlinTheme

@Composable
fun DetailScreen(
    cityName: String,
    hourlyWeather: List<HourlyWeather>,
    dailyWeather: List<DailyWeather>,
    canDelete: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentWeatherIcon = hourlyWeather.firstOrNull()?.weatherIcon ?: "01d"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WeatherBackground)
    ) {
        // 頂部導航列
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = WeatherTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            if (canDelete) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "刪除",
                        tint = WeatherTextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 今日天氣圖示
            AsyncImage(
                model = WeatherApi.getLargeIconUrl(currentWeatherIcon),
                contentDescription = "今日天氣",
                modifier = Modifier.size(150.dp)
            )

            // 地點
            Text(
                text = cityName,
                color = WeatherTextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 今日天氣時間表區域
            HourlyWeatherRow(
                hourlyWeatherList = hourlyWeather,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 7 日天氣
            DailyWeatherColumn(
                dailyWeatherList = dailyWeather,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    WeatherkotlinTheme {
        DetailScreen(
            cityName = "台北",
            hourlyWeather = PreviewData.sampleHourlyWeather,
            dailyWeather = PreviewData.sampleDailyWeather,
            canDelete = true,
            onBackClick = {},
            onDeleteClick = {}
        )
    }
}
