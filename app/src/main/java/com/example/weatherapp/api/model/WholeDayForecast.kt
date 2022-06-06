package com.example.weatherapp.api.model

import com.squareup.moshi.Json

data class WholeDayForecast (val hourly: List<HourForecast>)

data class HourForecast(
    @field:Json(name = "dt") val date: Long,
    val temp: Float,
    val humidity: Int,
    val weather: List<HourWeatherDescription>
)

data class HourWeatherDescription(val main: String, val description: String, val icon: String)

