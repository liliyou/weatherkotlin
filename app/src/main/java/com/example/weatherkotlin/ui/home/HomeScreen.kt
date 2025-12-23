package com.example.weatherkotlin.ui.home

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherkotlin.domain.model.CityWeather
import com.example.weatherkotlin.domain.model.PreviewData
import com.example.weatherkotlin.ui.components.WeatherCard
import com.example.weatherkotlin.ui.components.WeatherCardSkeleton
import com.example.weatherkotlin.ui.theme.WeatherBackground
import com.example.weatherkotlin.ui.theme.WeatherSearchBar
import com.example.weatherkotlin.ui.theme.WeatherTextPrimary
import com.example.weatherkotlin.ui.theme.WeatherkotlinTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSearchClick: () -> Unit,
    onCityClick: (CityWeather) -> Unit,
    onPermissionResult: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    ) { permissions ->
        val granted = permissions.values.any { it }
        onPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        if (!uiState.locationPermissionRequested) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WeatherBackground)
    ) {
        // 標題列：天氣 + 搜尋按鈕
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "天氣",
                color = WeatherTextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(WeatherSearchBar)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜尋",
                    tint = WeatherTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize(),
            state = pullToRefreshState,
            indicator = {
                // 隱藏預設圓圈指示器，用 skeleton 代替
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 下拉刷新或初始載入時顯示 skeleton
                if (uiState.isRefreshing || (uiState.isLoading && uiState.cityWeatherList.isEmpty())) {
                    val skeletonCount = maxOf(uiState.cityWeatherList.size, 1)
                    items(skeletonCount, key = { "skeleton_$it" }) {
                        WeatherCardSkeleton()
                    }
                } else {
                    // 城市列表（帶動畫）
                    items(
                        items = uiState.cityWeatherList,
                        key = { it.id }
                    ) { cityWeather ->
                        WeatherCard(
                            cityWeather = cityWeather,
                            onClick = { onCityClick(cityWeather) },
                            modifier = Modifier.animateItem(
                                fadeInSpec = tween(300),
                                fadeOutSpec = tween(300),
                                placementSpec = tween(300)
                            )
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    WeatherkotlinTheme {
        HomeScreen(
            uiState = HomeUiState(
                cityWeatherList = listOf(PreviewData.sampleCityWeather) + PreviewData.sampleCityWeatherList,
                isLoading = false,
                hasLocationPermission = true,
                locationPermissionRequested = true
            ),
            onSearchClick = {},
            onCityClick = {},
            onPermissionResult = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenLoadingPreview() {
    WeatherkotlinTheme {
        HomeScreen(
            uiState = HomeUiState(isLoading = true),
            onSearchClick = {},
            onCityClick = {},
            onPermissionResult = {},
            onRefresh = {}
        )
    }
}
