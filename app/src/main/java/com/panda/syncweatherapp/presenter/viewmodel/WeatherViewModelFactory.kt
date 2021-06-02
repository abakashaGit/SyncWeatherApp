package com.panda.syncweatherapp.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.panda.syncweatherapp.domain.usecase.GetWeatherByCityUseCase

class WeatherViewModelFactory(private val weatherUseCase: GetWeatherByCityUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherActivityViewModel(
            weatherUseCase
        ) as T
    }
}