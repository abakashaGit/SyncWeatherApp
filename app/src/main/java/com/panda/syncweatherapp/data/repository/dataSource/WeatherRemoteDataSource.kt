package com.panda.syncweatherapp.data.repository.dataSource

import com.panda.syncweatherapp.data.model.WeatherData
import retrofit2.Call
import retrofit2.Response

interface WeatherRemoteDataSource {
    suspend fun getWeatherByCity(city:String):Response<WeatherData>
}