package com.example.weatherkotlin.data.local

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) {
    companion object {
        private val DEFAULT_CITIES = listOf("高雄", "桃園", "台中", "新竹")
    }

    suspend fun getSuggestedCities(): List<String> {
        val count = searchHistoryDao.getCount()
        if (count == 0) {
            // 初始化預設城市
            DEFAULT_CITIES.forEachIndexed { index, city ->
                searchHistoryDao.insertHistory(
                    SearchHistoryEntity(
                        cityName = city,
                        timestamp = System.currentTimeMillis() - index * 1000
                    )
                )
            }
        }
        return searchHistoryDao.getSuggestedCities()
    }

    suspend fun addSuggestedCity(cityName: String) {
        searchHistoryDao.insertHistory(
            SearchHistoryEntity(
                cityName = cityName,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}
