package com.panda.syncweatherapp.data.util

sealed class WeatherResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : WeatherResource<T>(data)
    class Loading<T>(data: T? = null) : WeatherResource<T>(data)
    class Error<T>(message: String, data: T? = null) : WeatherResource<T>(data, message)
}
