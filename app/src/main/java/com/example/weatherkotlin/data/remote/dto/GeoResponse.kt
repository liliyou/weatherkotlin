package com.example.weatherkotlin.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeoResponse(
    @SerializedName("name") val name: String,
    @SerializedName("local_names") val localNames: Map<String, String>? = null,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("state") val state: String? = null
)
