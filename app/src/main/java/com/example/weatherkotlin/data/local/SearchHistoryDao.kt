package com.example.weatherkotlin.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchHistoryDao {
    @Query("SELECT cityName FROM search_history ORDER BY timestamp DESC LIMIT 5")
    suspend fun getSuggestedCities(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: SearchHistoryEntity)

    @Query("SELECT COUNT(*) FROM search_history")
    suspend fun getCount(): Int
}
