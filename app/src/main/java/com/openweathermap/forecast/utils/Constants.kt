package com.openweathermap.forecast.utils

object Constants {

    const val APP_PREFERENCES = "MY_PREFERENCES"
    const val REQUEST_PERMISSIONS_CODE = 1
    const val HOURS_TO_PUSH_NOTIFICATION = 12

    internal object PreferenceKeys{
        const val SELECTED_CITY = "selected_city"
        const val CURRENT_TEMPERATURE = "current_temp"

    }

    internal object INTENTS{
        const val FAV_SELECTED = "fav_selected"
    }
}