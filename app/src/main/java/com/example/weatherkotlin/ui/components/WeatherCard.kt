package com.example.weatherkotlin.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherkotlin.data.model.CityWeather
import com.example.weatherkotlin.data.model.PreviewData
import com.example.weatherkotlin.ui.theme.WeatherCardBackground
import com.example.weatherkotlin.ui.theme.WeatherTextPrimary
import com.example.weatherkotlin.ui.theme.WeatherkotlinTheme

@Composable
fun WeatherCard(
    cityWeather: CityWeather,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WeatherCardBackground)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 左側：圖示 + 城市資訊
            Column {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${cityWeather.weatherIcon}@2x.png",
                    contentDescription = cityWeather.weatherDescription,
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = cityWeather.cityName,
                    color = WeatherTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = cityWeather.weatherDescription,
                    color = WeatherTextPrimary,
                    fontSize = 14.sp
                )
            }

            // 右側：溫度資訊
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${cityWeather.currentTemp}",
                    color = WeatherTextPrimary,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "最高 ${cityWeather.highTemp}",
                    color = WeatherTextPrimary,
                    fontSize = 14.sp
                )
                Text(
                    text = "最低 ${cityWeather.lowTemp}",
                    color = WeatherTextPrimary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E2A47)
@Composable
private fun WeatherCardPreview() {
    WeatherkotlinTheme {
        WeatherCard(
            cityWeather = PreviewData.sampleCityWeather,
            onClick = {}
        )
    }
}

@Composable
fun WeatherCardSkeleton(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.1f),
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.1f)
        ),
        start = Offset(shimmerOffset - 500f, 0f),
        end = Offset(shimmerOffset, 0f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WeatherCardBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 左側骨架
            Column {
                // 天氣圖標佔位
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 城市名稱佔位
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmerBrush)
                )
                Spacer(modifier = Modifier.height(4.dp))
                // 天氣描述佔位
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmerBrush)
                )
            }

            // 右側骨架
            Column(
                horizontalAlignment = Alignment.End
            ) {
                // 溫度佔位
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 最高溫佔位
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmerBrush)
                )
                Spacer(modifier = Modifier.height(4.dp))
                // 最低溫佔位
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmerBrush)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E2A47)
@Composable
private fun WeatherCardSkeletonPreview() {
    WeatherkotlinTheme {
        WeatherCardSkeleton()
    }
}
