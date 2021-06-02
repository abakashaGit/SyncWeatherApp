package com.panda.syncweatherapp.data.repository.dataSourceImpl

import android.util.Log
import com.panda.syncweatherapp.data.api.RetrofitBuilder
import com.panda.syncweatherapp.data.api.WeatherApi
import com.panda.syncweatherapp.data.model.WeatherData
import com.panda.syncweatherapp.data.repository.dataSource.WeatherRemoteDataSource
import retrofit2.Call
import retrofit2.Response

class WeatherRemoteDataSourceImpl():WeatherRemoteDataSource {

    override suspend fun getWeatherByCity(city: String): Response<WeatherData> {
        val request = RetrofitBuilder.buildService(WeatherApi::class.java)
        Log.i("MyTAG", request.toString())
        return request.getWeatherByCity(city)
    }
}