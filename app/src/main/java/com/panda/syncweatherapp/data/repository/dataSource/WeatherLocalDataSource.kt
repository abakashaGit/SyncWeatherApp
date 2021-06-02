package com.panda.syncweatherapp.data.repository.dataSource

import com.panda.syncweatherapp.data.model.WeatherData
import retrofit2.Response

interface WeatherLocalDataSource {
    suspend fun getSavedWeather(): Response<WeatherData>
}