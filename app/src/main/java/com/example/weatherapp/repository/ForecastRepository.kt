package com.example.weatherapp.repository

import android.util.Log
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.api.createOpenWeatherMapService
import com.example.weatherapp.api.model.CurrentForecast
import com.example.weatherapp.api.model.WeeklyForecast
import com.example.weatherapp.api.model.WholeDayForecast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastRepository(private val language: String) {

    fun loadCurrentForecast(
        cityName: String,
        successCallback: (CurrentForecast) -> Unit,
        failureCallback: (ForecastError) -> Unit,
        countryCode: String = "es"
    ) {
        val call = createOpenWeatherMapService().getCurrentForecast(
            apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
            lang = language,
            cityName = cityName
        )
        call.enqueue(object : Callback<CurrentForecast> {
            override fun onFailure(call: Call<CurrentForecast>, t: Throwable) {
                failureCallback(ForecastError.REQUEST_ERROR)
                Log.e(
                    ForecastRepository::class.java.simpleName,
                    "error loading current forecast",
                    t
                )
            }

            override fun onResponse(
                call: Call<CurrentForecast>,
                response: Response<CurrentForecast>
            ) {
                println(response.toString())
                response.body()?.let { currentForecast ->
                    successCallback(currentForecast)
                } ?: failureCallback(ForecastError.CITY_NAME_NOT_FOUND)
            }
        })
    }

    fun loadWeeklyForecast(
        cityName: String,
        successCallback: (WeeklyForecast) -> Unit,
        failureCallback: (ForecastError) -> Unit
    ) {
        loadCurrentForecast(cityName, { currentForecast ->
            val call = createOpenWeatherMapService().getWeeklyForecast(
                apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
                lang = language,
                lat = currentForecast.coordinates.lat,
                lon = currentForecast.coordinates.lon
            )
            call.enqueue(object : Callback<WeeklyForecast> {
                override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                    failureCallback(ForecastError.REQUEST_ERROR)
                    Log.e(
                        ForecastRepository::class.java.simpleName,
                        "error loading weekly forecast",
                        t
                    )
                }

                override fun onResponse(
                    call: Call<WeeklyForecast>,
                    response: Response<WeeklyForecast>
                ) {
                    response.body()?.let { weeklyForecast ->
                        successCallback(weeklyForecast)
                    } ?: failureCallback(ForecastError.CITY_NAME_NOT_FOUND)
                }
            })
        }, {
            failureCallback(it)
        })
    }

    fun loadWholeDayForecast(
        cityName: String,
        successCallback: (WholeDayForecast) -> Unit,
        failureCallback: (ForecastError) -> Unit
    ) {
        loadCurrentForecast(cityName, { currentForecast ->
            val call = createOpenWeatherMapService().getWholeDayForecast(
                apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
                lang = language,
                lat = currentForecast.coordinates.lat,
                lon = currentForecast.coordinates.lon
            )
            call.enqueue(object : Callback<WholeDayForecast> {
                override fun onFailure(call: Call<WholeDayForecast>, t: Throwable) {
                    failureCallback(ForecastError.REQUEST_ERROR)
                    Log.e(
                        ForecastRepository::class.java.simpleName,
                        "error loading weekly forecast",
                        t
                    )
                }

                override fun onResponse(
                    call: Call<WholeDayForecast>,
                    response: Response<WholeDayForecast>
                ) {
                    response.body()?.let { wholeDayForecast ->
                        successCallback(wholeDayForecast)
                    } ?: failureCallback(ForecastError.CITY_NAME_NOT_FOUND)
                }
            })
        }, {
            failureCallback(it)
        })
    }
}

enum class ForecastError(val resId: Int) {
    CITY_NAME_NOT_FOUND(R.string.label_forecast_request_error_city_name_not_found),
    REQUEST_ERROR(R.string.label_forecast_request_error)
}