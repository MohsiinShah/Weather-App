package com.openweathermap.forecast.models

data class WeatherApiResponse(
    val alerts: List<Alert>,
    val current: Current,
    val daily: List<Daily>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)