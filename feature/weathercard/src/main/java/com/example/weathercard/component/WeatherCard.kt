package com.example.weathercard.component

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
import androidx.compose.foundation.layout.widthIn
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
import com.example.weathercard.model.CityWeather
import com.example.weathercard.model.PreviewData
import com.example.weathercard.theme.WeatherTextPrimary
import com.example.weathercard.theme.WeatherTextSecondary
import com.example.weathercard.theme.weatherCardStyle

private val ICON_SIZE = 110.dp
private val CARD_TOP_PADDING = 50.dp

/**
 * 天氣卡片元件
 * 顯示城市名稱、天氣狀態、溫度和動畫圖示
 *
 * @param cityWeather 城市天氣資料
 * @param onClick 點擊回調
 * @param modifier Modifier
 */
@Composable
fun WeatherCard(
    cityWeather: CityWeather,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp)
    ) {
        // 卡片背景（從上方偏移開始，讓圖示懸浮）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = CARD_TOP_PADDING)
                .weatherCardStyle()
                .clickable(onClick = onClick)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // 左側：城市資訊
                Column(
                    modifier = Modifier.widthIn(max = 180.dp)
                ) {
                    // 為圖示預留空間
                    Spacer(modifier = Modifier.height(ICON_SIZE - CARD_TOP_PADDING))
                    Text(
                        text = if (cityWeather.country.isNotEmpty()) {
                            "${cityWeather.cityName}, ${cityWeather.country}"
                        } else {
                            cityWeather.cityName
                        },
                        color = WeatherTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = cityWeather.weatherDescription,
                        color = WeatherTextSecondary,
                        fontSize = 14.sp
                    )
                }

                // 右側：溫度資訊
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${cityWeather.currentTemp}°",
                        color = WeatherTextPrimary,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Light,
                        lineHeight = 44.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "最高 ${cityWeather.highTemp}°",
                        color = WeatherTextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "最低 ${cityWeather.lowTemp}°",
                        color = WeatherTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // 懸浮的天氣動畫圖示
        WeatherAnimatedIcon(
            weatherIcon = cityWeather.weatherIcon,
            contentDescription = cityWeather.weatherDescription,
            size = ICON_SIZE
        )
    }
}

/**
 * 天氣卡片骨架屏
 * 載入中狀態的佔位元件
 */
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
            .widthIn(max = 600.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = CARD_TOP_PADDING)
                .weatherCardStyle()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // 左側骨架
                Column(
                    modifier = Modifier.widthIn(max = 180.dp)
                ) {
                    Spacer(modifier = Modifier.height(ICON_SIZE - CARD_TOP_PADDING))
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(shimmerBrush)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(shimmerBrush)
                    )
                }

                // 右側骨架
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(shimmerBrush)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(13.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(shimmerBrush)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(13.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(shimmerBrush)
                    )
                }
            }
        }

        // 懸浮圖示佔位
        Box(
            modifier = Modifier
                .size(ICON_SIZE)
                .clip(RoundedCornerShape(16.dp))
                .background(shimmerBrush)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E2A47)
@Composable
private fun WeatherCardPreview() {
    WeatherCard(
        cityWeather = PreviewData.sampleCityWeather,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1E2A47)
@Composable
private fun WeatherCardSkeletonPreview() {
    WeatherCardSkeleton()
}
