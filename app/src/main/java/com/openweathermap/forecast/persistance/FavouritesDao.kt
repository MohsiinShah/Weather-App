package com.openweathermap.forecast.persistance

import androidx.room.*
import com.openweathermap.forecast.models.Cities
import com.openweathermap.forecast.models.City
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {

    @Query("SELECT * FROM FavouriteCities")
    fun getCities(): Flow<List<Favourites>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(favourites: Favourites)

    @Query("DELETE FROM FavouriteCities")
    suspend fun deleteAllCities()
}