package com.example.weathercard.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** 光澤效果常數 */
private const val GLOSS_ALPHA_START = 0.35f
private const val GLOSS_ALPHA_MID = 0.15f
private const val GLOSS_GRADIENT_SIZE = 300f

/** 卡片樣式常數 */
private val CARD_CORNER_RADIUS = 20.dp
private val CARD_ELEVATION = 8.dp
private const val CARD_SHADOW_ALPHA = 0.3f

/**
 * 光澤效果 Modifier
 * 從左上角到右下角的白色半透明漸層
 */
fun Modifier.glossEffect(): Modifier = this.background(
    Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = GLOSS_ALPHA_START),
            Color.White.copy(alpha = GLOSS_ALPHA_MID),
            Color.Transparent
        ),
        start = Offset(0f, 0f),
        end = Offset(GLOSS_GRADIENT_SIZE, GLOSS_GRADIENT_SIZE)
    )
)

/**
 * 卡片背景漸層 Modifier
 * 從淺色到深色的對角漸層
 */
fun Modifier.cardGradient(): Modifier = this.background(
    Brush.linearGradient(
        colors = listOf(WeatherCardBackgroundLight, WeatherCardBackground),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
)

/**
 * 完整卡片樣式 Modifier
 * 包含：陰影、圓角、漸層背景、光澤效果
 */
fun Modifier.weatherCardStyle(): Modifier = this
    .shadow(
        elevation = CARD_ELEVATION,
        shape = RoundedCornerShape(CARD_CORNER_RADIUS),
        ambientColor = Color.Black.copy(alpha = CARD_SHADOW_ALPHA),
        spotColor = Color.Black.copy(alpha = CARD_SHADOW_ALPHA)
    )
    .clip(RoundedCornerShape(CARD_CORNER_RADIUS))
    .cardGradient()
    .glossEffect()
