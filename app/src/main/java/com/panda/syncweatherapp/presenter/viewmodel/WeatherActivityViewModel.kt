package com.panda.syncweatherapp.presenter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.panda.syncweatherapp.domain.usecase.GetWeatherByCityUseCase

class WeatherActivityViewModel(weatherUseCase: GetWeatherByCityUseCase):AndroidViewModel(Application()) {

}