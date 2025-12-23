package com.example.weatherkotlin.data.repository

import com.example.search.domain.model.SearchResult
import com.example.search.domain.repository.SearchRepository
import com.example.weatherkotlin.data.local.SearchHistoryRepository
import com.example.weatherkotlin.data.remote.WeatherApi
import com.example.weatherkotlin.domain.repository.WeatherRepository
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    @Named("apiKey") private val apiKey: String,
    private val weatherRepository: WeatherRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : SearchRepository {

    override suspend fun searchCity(query: String): List<SearchResult> {
        val results = weatherApi.searchCity(query = query, apiKey = apiKey)
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

    override suspend fun addCity(lat: Double, lon: Double, cityName: String): Boolean {
        if (weatherRepository.isCityExists(cityName)) {
            return false
        }
        weatherRepository.fetchAndSaveWeather(lat, lon, cityName)
        return true
    }

    override suspend fun getSuggestedCities(): List<String> {
        return searchHistoryRepository.getSuggestedCities()
    }

    override suspend fun addSuggestedCity(cityName: String) {
        searchHistoryRepository.addSuggestedCity(cityName)
    }
}
