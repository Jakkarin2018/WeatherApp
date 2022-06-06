package com.example.weatherapp.forecast.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.LocationError
import com.example.weatherapp.util.Resource
import com.example.weatherapp.util.TempDisplaySettingManager
import com.example.weatherapp.util.formatTempForDisplay

class CurrentForecastViewModelFactory(
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentForecastViewModel::class.java)) {
            return CurrentForecastViewModel(tempDisplaySettingManager, forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CurrentForecastViewModel(
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    private val _viewState: MutableLiveData<Resource<CurrentForecastViewState>> = MutableLiveData()
    val viewState: LiveData<Resource<CurrentForecastViewState>> = _viewState

    fun onLocationObtained(cityName: String) {
        _viewState.value = Resource.loading()

        forecastRepository.loadCurrentForecast(cityName, { currentForecast ->
            _viewState.value = Resource.success(
                CurrentForecastViewState(
                    currentForecast.name,
                    formatTempForDisplay(
                        currentForecast.forecast.temp,
                        tempDisplaySettingManager.getTempDisplaySetting()
                    ),
                    currentForecast.forecast.humidity.toString()
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