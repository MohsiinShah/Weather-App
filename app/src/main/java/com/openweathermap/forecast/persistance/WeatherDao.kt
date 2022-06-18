package com.openweathermap.forecast.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openweathermap.forecast.models.WeatherApiResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM WeatherData")
    fun getWeatherData(): Flow<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weather: Weather)

    @Query("DELETE FROM WeatherData")
    suspend fun deleteAllWeatherData()
}