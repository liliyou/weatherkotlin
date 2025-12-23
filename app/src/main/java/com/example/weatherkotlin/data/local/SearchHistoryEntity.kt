package com.example.weatherkotlin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey
    val cityName: String,
    val timestamp: Long = System.currentTimeMillis()
)
