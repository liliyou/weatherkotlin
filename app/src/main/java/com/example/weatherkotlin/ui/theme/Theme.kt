package com.example.weatherkotlin.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val WeatherColorScheme = darkColorScheme(
    primary = WeatherCardBackground,
    secondary = WeatherSearchBar,
    tertiary = WeatherTextSecondary,
    background = WeatherBackground,
    surface = WeatherBackground,
    onPrimary = WeatherTextPrimary,
    onSecondary = WeatherTextPrimary,
    onTertiary = WeatherTextPrimary,
    onBackground = WeatherTextPrimary,
    onSurface = WeatherTextPrimary
)

@Composable
fun WeatherkotlinTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = WeatherColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            @Suppress("DEPRECATION")
            window.statusBarColor = WeatherBackground.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
