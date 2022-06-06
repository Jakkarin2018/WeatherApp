package com.example.weatherapp.forecast.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.LocationError
import com.example.weatherapp.util.Resource

class WeeklyForecastViewModelFactory(
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyForecastViewModel::class.java)) {
            return WeeklyForecastViewModel(forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class WeeklyForecastViewModel(private val forecastRepository: ForecastRepository) : ViewModel() {

    private val _viewState: MutableLiveData<Resource<WeeklyForecastViewState>> = MutableLiveData()
    val viewState: LiveData<Resource<WeeklyForecastViewState>> = _viewState

    fun onLocationObtained(cityName: String) {
        _viewState.value = Resource.loading()

        forecastRepository.loadWeeklyForecast(cityName, {
            _viewState.value = Resource.success(
                WeeklyForecastViewState(
                    it.daily
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