package com.openweathermap.forecast.persistance

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Weather::class, Favourites::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun restaurantDao(): WeatherDao

    abstract fun favouritesDao(): FavouritesDao
}