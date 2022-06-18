package com.openweathermap.forecast.persistance

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openweathermap.forecast.models.City

import java.lang.reflect.Type

@Entity(tableName = "FavouriteCities")
@TypeConverters(CityTypeConverters::class)
data class Favourites(
    @PrimaryKey(autoGenerate = true) val ID: Int?,
    val city: City?
)

object CityTypeConverters {
    @TypeConverter
    fun stringToCity(json: String?): City? {
        val gson = Gson()
        val type: Type = object : TypeToken<City>() {}.type
        return gson.fromJson<City>(json, type)
    }

    @TypeConverter
    fun cityToString(list: City): String {
        val gson = Gson()
        val type: Type = object : TypeToken<City>() {}.type
        return gson.toJson(list, type)
    }
}
