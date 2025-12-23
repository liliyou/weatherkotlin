package com.example.weatherkotlin.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CityWeatherDao {

    @Query("SELECT * FROM city_weather ORDER BY id DESC")
    fun getAllCityWeather(): Flow<List<CityWeatherEntity>>

    @Query("SELECT * FROM city_weather WHERE id = :id")
    suspend fun getCityWeatherById(id: Long): CityWeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityWeather(cityWeather: CityWeatherEntity): Long

    @Update
    suspend fun updateCityWeather(cityWeather: CityWeatherEntity)

    @Delete
    suspend fun deleteCityWeather(cityWeather: CityWeatherEntity)

    @Query("DELETE FROM city_weather WHERE id = :id")
    suspend fun deleteCityWeatherById(id: Long)
}
