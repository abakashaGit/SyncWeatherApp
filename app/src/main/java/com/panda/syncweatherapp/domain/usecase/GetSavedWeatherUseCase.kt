package com.panda.syncweatherapp.domain.usecase

import com.panda.syncweatherapp.data.model.WeatherData
import com.panda.syncweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetSavedWeatherUseCase(private val weatherRepository: WeatherRepository)
{
    suspend fun execute():Flow<WeatherData>{
        return weatherRepository.getSavedWeather()
    }
}