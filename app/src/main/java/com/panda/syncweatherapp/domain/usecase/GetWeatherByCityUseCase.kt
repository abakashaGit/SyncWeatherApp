package com.panda.syncweatherapp.domain.usecase

import com.panda.syncweatherapp.data.model.WeatherData
import com.panda.syncweatherapp.data.util.WeatherResource
import com.panda.syncweatherapp.domain.repository.WeatherRepository

class GetWeatherByCityUseCase(private val weatherRepository: WeatherRepository){
    suspend fun execute():WeatherResource<WeatherData>{
        return weatherRepository.getWeatherByCity()
    }
}