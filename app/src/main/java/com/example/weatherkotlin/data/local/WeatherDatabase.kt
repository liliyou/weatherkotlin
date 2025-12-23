package com.example.weatherkotlin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CityWeatherEntity::class, SearchHistoryEntity::class],
    version = 3,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun cityWeatherDao(): CityWeatherDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        const val DATABASE_NAME = "weather_database"
    }
}
