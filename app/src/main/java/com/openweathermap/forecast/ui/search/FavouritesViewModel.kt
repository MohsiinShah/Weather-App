package com.openweathermap.forecast.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openweathermap.forecast.models.City
import com.openweathermap.forecast.persistance.Favourites
import com.openweathermap.forecast.persistance.WeatherDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
     val db: WeatherDatabase
) : ViewModel() {

    val cities = db.favouritesDao().getCities().asLiveData()

    fun insertCity(favouriteCity: Favourites){
        viewModelScope.launch {
            db.favouritesDao().insertCity(favouriteCity)
        }
    }
}