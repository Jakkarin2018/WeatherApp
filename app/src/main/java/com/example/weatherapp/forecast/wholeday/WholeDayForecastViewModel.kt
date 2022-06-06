package com.example.weatherapp.forecast.wholeday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.LocationError
import com.example.weatherapp.util.Resource

class WholeDayForecastViewModelFactory(
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WholeDayForecastViewModel::class.java)) {
            return WholeDayForecastViewModel(forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class WholeDayForecastViewModel(private val forecastRepository: ForecastRepository) : ViewModel() {

    private val _viewState: MutableLiveData<Resource<WholeDayForecastViewState>> = MutableLiveData()
    val viewState: LiveData<Resource<WholeDayForecastViewState>> = _viewState

    fun onLocationObtained(cityName: String) {
        _viewState.value = Resource.loading()

        forecastRepository.loadWholeDayForecast(cityName, {
            _viewState.value = Resource.success(
                WholeDayForecastViewState(
                    it.hourly
                )
            )
        }, {
            _viewState.value = Resource.error(it.resId)
        })
    }

    fun onLocationError() {
        _viewState.value = Resource.error(LocationError.NO_LOCATION.resId)
    }
}