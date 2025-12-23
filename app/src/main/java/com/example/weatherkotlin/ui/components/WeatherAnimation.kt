package com.example.weatherkotlin.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * 天氣動畫圖示元件
 * 根據天氣圖示代碼顯示對應的 Lottie 動畫
 *
 * @param weatherIcon OpenWeatherMap 圖示代碼（如 01d, 02n, 10d 等）
 * @param contentDescription 無障礙描述
 * @param modifier Modifier
 * @param size 圖示大小
 */
@Composable
fun WeatherAnimatedIcon(
    weatherIcon: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp
) {
    val animationUrl = getWeatherAnimationUrl(weatherIcon)

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(animationUrl)
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit
    )
}

/**
 * 根據天氣圖示代碼取得對應的 Lottie 動畫 URL
 */
private fun getWeatherAnimationUrl(weatherIcon: String): String {
    val isNight = weatherIcon.endsWith("n")
    val code = weatherIcon.dropLast(1)

    return when (code) {
        "01" -> if (isNight) ANIM_NIGHT_CLEAR else ANIM_DAY_SUNNY
        "02" -> if (isNight) ANIM_NIGHT_CLOUDY else ANIM_DAY_PARTLY_CLOUDY
        "03", "04" -> ANIM_CLOUDY
        "09", "10" -> if (isNight) ANIM_NIGHT_RAIN else ANIM_DAY_RAIN
        "11" -> ANIM_THUNDERSTORM
        "13" -> ANIM_SNOW
        "50" -> ANIM_MIST
        else -> if (isNight) ANIM_NIGHT_CLEAR else ANIM_DAY_SUNNY
    }
}

// LottieFiles 免費動畫 URL（Lottie Simple License）
private const val ANIM_DAY_SUNNY =
    "https://assets-v2.lottiefiles.com/a/5855a50a-1151-11ee-8713-db7d99d1cba7/sxCYH0r96t.json"
private const val ANIM_NIGHT_CLEAR =
    "https://assets-v2.lottiefiles.com/a/e9a1c380-1170-11ee-970a-43c443669001/Nddq9Y61cV.lottie"
private const val ANIM_DAY_PARTLY_CLOUDY =
    "https://assets-v2.lottiefiles.com/a/a0e7e530-1165-11ee-9c6a-97595d5ad655/D6eiX3vz1I.lottie"
private const val ANIM_NIGHT_CLOUDY =
    "https://assets-v2.lottiefiles.com/a/1e859a52-1164-11ee-af47-ab7d342163f9/ioFtLaMLY8.json"
private const val ANIM_CLOUDY =
    "https://assets-v2.lottiefiles.com/a/1e859a52-1164-11ee-af47-ab7d342163f9/ioFtLaMLY8.json"
private const val ANIM_DAY_RAIN =
    "https://assets-v2.lottiefiles.com/a/1e8b4808-1164-11ee-af4c-cfeecc655273/FHtdrLV99c.json"
private const val ANIM_NIGHT_RAIN =
    "https://assets-v2.lottiefiles.com/a/b156571c-1165-11ee-8d93-3f2fb0cc9bb5/OnAjHcAFCA.json"
private const val ANIM_THUNDERSTORM =
    "https://assets-v2.lottiefiles.com/a/35a6b804-5405-11ee-a5b4-6b770abb185b/oarNziiAM1.json"
private const val ANIM_SNOW =
    "https://assets-v2.lottiefiles.com/a/b812a11e-1183-11ee-a65e-df170e015e38/rlJcgkJBix.json"
private const val ANIM_MIST =
    "https://assets-v2.lottiefiles.com/a/5838a158-1151-11ee-870a-5f500d146f4c/guXfjv7EdD.lottie"
