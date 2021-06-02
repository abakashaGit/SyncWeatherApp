package com.panda.syncweatherapp.data.api

import com.panda.syncweatherapp.data.model.WeatherData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q")
        city: String,
        @Query("appid")
        appid: String = "226bf0131282e8c691fefdcc9ad6453b"
    ):Response<WeatherData>

}