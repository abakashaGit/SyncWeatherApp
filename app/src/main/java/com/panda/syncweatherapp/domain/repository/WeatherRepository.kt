package com.panda.syncweatherapp.domain.repository

import com.panda.syncweatherapp.data.model.WeatherData
import com.panda.syncweatherapp.data.util.WeatherResource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherByCity(): WeatherResource<WeatherData>
    fun getSavedWeather():Flow<WeatherData>
}