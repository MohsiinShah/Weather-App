package com.openweathermap.forecast.utils


import android.content.SharedPreferences
import com.google.gson.Gson
import com.openweathermap.forecast.models.Current
import com.openweathermap.forecast.persistance.Favourites


class MyAppPreferences(private var mSharedPreferences: SharedPreferences) {

    fun saveSelectedCity(city: Favourites) {
        val gson = Gson()
        val json = gson.toJson(city)
        mSharedPreferences
            .edit()
            .putString(Constants.PreferenceKeys.SELECTED_CITY, json)
            .apply()
    }

    fun getSelectedCity(): Favourites? {
        val gson = Gson()
        val json: String? =
            mSharedPreferences.getString(Constants.PreferenceKeys.SELECTED_CITY, null)
        return gson.fromJson(json, Favourites::class.java)
    }

    fun saveCurrentTemperature(city: Current) {
        val gson = Gson()
        val json = gson.toJson(city)
        mSharedPreferences
            .edit()
            .putString(Constants.PreferenceKeys.CURRENT_TEMPERATURE, json)
            .apply()
    }

    fun getCurrentTemperature(): Current? {
        val gson = Gson()
        val json: String? =
            mSharedPreferences.getString(Constants.PreferenceKeys.CURRENT_TEMPERATURE, null)
        return gson.fromJson(json, Current::class.java)
    }
}