package com.openweathermap.forecast.persistance

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openweathermap.forecast.models.Alert
import com.openweathermap.forecast.models.Current
import com.openweathermap.forecast.models.Daily
import java.lang.reflect.Type


@Entity(tableName = "WeatherData")
@TypeConverters(AlertTypeConverters::class, DailyTypeConverters::class, CurrentTypeConverters::class)
data class Weather(
    @PrimaryKey(autoGenerate = true) val ID: Int,
    val alerts: List<Alert>?,
    val current: Current,
    val daily: List<Daily>?,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)

object AlertTypeConverters {
    @TypeConverter
    fun stringToAlerts(json: String?): List<Alert>? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Alert>?>() {}.type
        return gson.fromJson<List<Alert>>(json, type)
    }

    @TypeConverter
    fun allertsToString(list: List<Alert>?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Alert?>?>() {}.type
        return gson.toJson(list, type)
    }
}

object CurrentTypeConverters {
    @TypeConverter
    fun stringToCurrent(json: String?): Current {
        val gson = Gson()
        val type: Type = object : TypeToken<Current>() {}.type
        return gson.fromJson<Current>(json, type)
    }

    @TypeConverter
    fun currentToString(list: Current): String {
        val gson = Gson()
        val type: Type = object : TypeToken<Current>() {}.type
        return gson.toJson(list, type)
    }
}

object DailyTypeConverters {
    @TypeConverter
    fun stringToDaily(json: String?): List<Daily?> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily>?>() {}.type
        return gson.fromJson<List<Daily>>(json, type)
    }

    @TypeConverter
    fun dailyToString(list: List<Daily>?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily>?>() {}.type
        return gson.toJson(list, type)
    }
}