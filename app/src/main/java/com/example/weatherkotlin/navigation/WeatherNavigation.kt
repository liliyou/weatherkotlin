package com.example.weatherkotlin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.search.presentation.SearchScreen
import com.example.search.presentation.SearchViewModel
import com.example.weatherkotlin.domain.model.CityWeather
import com.example.weatherkotlin.ui.detail.DetailScreen
import com.example.weatherkotlin.ui.detail.DetailViewModel
import com.example.weatherkotlin.ui.home.HomeScreen
import com.example.weatherkotlin.ui.home.HomeViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Detail : Screen("detail/{lat}/{lon}/{cityName}/{cityId}") {
        fun createRoute(lat: Double, lon: Double, cityName: String, cityId: Long) =
            "detail/$lat/$lon/${java.net.URLEncoder.encode(cityName, "UTF-8")}/$cityId"
    }
    data object Search : Screen("search")
}

@Composable
fun WeatherNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            HomeScreen(
                uiState = uiState,
                errorMessage = viewModel.errorMessage,
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onCityClick = { cityWeather ->
                    navController.navigate(
                        Screen.Detail.createRoute(
                            lat = cityWeather.lat,
                            lon = cityWeather.lon,
                            cityName = cityWeather.cityName,
                            cityId = cityWeather.id
                        )
                    )
                },
                onPermissionResult = viewModel::onPermissionResult,
                onRefresh = viewModel::refreshAllWeather
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType },
                navArgument("cityName") { type = NavType.StringType },
                navArgument("cityId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getFloat("lat")?.toDouble() ?: 0.0
            val lon = backStackEntry.arguments?.getFloat("lon")?.toDouble() ?: 0.0
            val cityName = java.net.URLDecoder.decode(
                backStackEntry.arguments?.getString("cityName") ?: "",
                "UTF-8"
            )
            val cityId = backStackEntry.arguments?.getLong("cityId") ?: -1L

            val viewModel: DetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(lat, lon, cityName, cityId) {
                viewModel.loadWeather(lat, lon, cityName, cityId)
            }

            LaunchedEffect(uiState.isDeleted) {
                if (uiState.isDeleted) {
                    navController.popBackStack()
                }
            }

            DetailScreen(
                uiState = uiState,
                errorMessage = viewModel.errorMessage,
                onBackClick = { navController.popBackStack() },
                onDeleteClick = { viewModel.deleteCity() },
                onRefresh = { viewModel.refresh() }
            )
        }

        composable(Screen.Search.route) {
            val viewModel: SearchViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.addedCity) {
                uiState.addedCity?.let { city ->
                    viewModel.resetAddedCity()
                    // 清除 Search 頁面，讓 Detail 返回時直接回到 Home
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                    navController.navigate(
                        Screen.Detail.createRoute(
                            lat = city.lat,
                            lon = city.lon,
                            cityName = city.cityName,
                            cityId = city.id
                        )
                    )
                }
            }

            SearchScreen(
                query = uiState.query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = viewModel::onSearch,
                searchResults = uiState.searchResults,
                suggestedCities = uiState.suggestedCities,
                onResultClick = { viewModel.addCity(it) },
                onSuggestedClick = viewModel::onSuggestedCityClick,
                onClose = { navController.popBackStack() },
                errorMessage = viewModel.errorMessage
            )
        }
    }
}
