package com.openweathermap.forecast.ui.search

import com.openweathermap.forecast.persistance.Favourites

interface FavouriteCityNavigator {
    fun onCitySelected(favCity: Favourites)
}