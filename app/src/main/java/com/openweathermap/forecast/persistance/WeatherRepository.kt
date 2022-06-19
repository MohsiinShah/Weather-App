package com.openweathermap.forecast.persistance

import androidx.room.withTransaction
import com.openweathermap.forecast.api.ApiService
import com.openweathermap.forecast.utils.MyAppPreferences
import com.openweathermap.forecast.utils.UrlUtils
import com.openweathermap.forecast.utils.networkBoundResource
import kotlinx.coroutines.delay
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: ApiService,
    private val db: WeatherDatabase,
    private val sharedPrefs: MyAppPreferences
) {
    private val restaurantDao = db.restaurantDao()

    fun getWeatherData() = networkBoundResource(
        query = {
            restaurantDao.getWeatherData()
        },
        fetch = {
            delay(1000)
            api.getWeatherData(
                sharedPrefs.getSelectedCity()?.city?.lat?.toString() ?: "51.5072",
                sharedPrefs.getSelectedCity()?.city?.lon?.toString() ?: "0.1276",
                "minutely,hourly",
                UrlUtils.API_ACCESS_KEY,
                if (sharedPrefs.getIfCelsius()) "metric" else "imperial"
            )
        },
        saveFetchResult = { restaurants ->
            db.withTransaction {
                restaurantDao.deleteAllWeatherData()
                restaurantDao.insertWeatherData(restaurants)
            }
        }
    )
}