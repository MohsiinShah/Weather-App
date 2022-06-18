package com.openweathermap.forecast.ui

import androidx.lifecycle.*
import com.openweathermap.forecast.persistance.Weather
import com.openweathermap.forecast.persistance.WeatherRepository
import com.openweathermap.forecast.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val loadTrigger = MutableLiveData(Unit)

    fun refresh() {
        loadTrigger.value = Unit
    }

    val forecasts: LiveData<
            Resource<Weather>> = loadTrigger.switchMap {
        loadData()
    }

    private fun loadData(): LiveData<
            Resource<Weather>> {
        return repository.getWeatherData().asLiveData()
    }
}