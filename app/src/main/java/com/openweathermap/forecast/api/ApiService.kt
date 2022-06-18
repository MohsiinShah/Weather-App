package com.openweathermap.forecast.api

import com.openweathermap.forecast.models.WeatherApiResponse
import com.openweathermap.forecast.persistance.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/onecall?")
    suspend fun getWeatherData(@Query("lat") lat: String,
                         @Query("lon") lon:String,
                         @Query("exclude") exclusions:String,
                         @Query("APPID") appid:String,
                         @Query("units") unit:String) : Weather
}