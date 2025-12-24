package com.example.weatherkotlin.data.repository

import com.example.search.domain.model.AddedCityInfo
import com.example.search.domain.model.SearchResult
import com.example.search.domain.repository.SearchRepository
import com.example.weatherkotlin.data.remote.WeatherApi
import com.example.weatherkotlin.domain.repository.WeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherRepository: WeatherRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : SearchRepository {

    override suspend fun searchCity(query: String): List<SearchResult> {
        val results = weatherApi.searchCity(query = query)
        return results.map { geo ->
            val zhName = geo.localNames?.get("zh")
            val displayName = buildString {
                append(zhName ?: geo.name)
                if (zhName != null && zhName != geo.name) {
                    append(" (${geo.name})")
                }
                geo.state?.let { append(", $it") }
            }
            SearchResult(
                cityName = displayName,
                lat = geo.lat,
                lon = geo.lon
            )
        }.distinctBy { "${it.lat},${it.lon}" }
    }

    override suspend fun addCity(lat: Double, lon: Double, cityName: String): AddedCityInfo {
        val result = weatherRepository.addCityIfNotExists(lat, lon, cityName)
        return AddedCityInfo(
            id = result.cityWeather.id,
            lat = result.cityWeather.lat,
            lon = result.cityWeather.lon,
            cityName = result.cityWeather.cityName,
            isNew = result.isNew
        )
    }

    override suspend fun getSuggestedCities(): List<String> {
        return searchHistoryRepository.getSuggestedCities()
    }

    override suspend fun addSuggestedCity(cityName: String) {
        searchHistoryRepository.addSuggestedCity(cityName)
    }
}
