package com.example.weatherapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.R

sealed class Location {
    data class CityName(val cityName: String) : Location()
}

enum class LocationError(val resId: Int) {
    NO_LOCATION(R.string.label_location_error)
}

private const val KEY_CITY_NAME = "key_zip_code"

class LocationRepository(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _savedLocation: MutableLiveData<Location> = MutableLiveData()
    val savedLocation: LiveData<Location> = _savedLocation

    init {
        preferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key != KEY_CITY_NAME) return@registerOnSharedPreferenceChangeListener
            broadcastSavedZipCode()
        }
        broadcastSavedZipCode()
    }

    fun saveLocation(location: Location) {
        when (location) {
            is Location.CityName -> preferences.edit().putString(KEY_CITY_NAME, location.cityName)
                .apply()
        }
    }

    private fun broadcastSavedZipCode() {
        val cityName = preferences.getString(KEY_CITY_NAME, "")
        if (cityName != null && cityName.isNotBlank()) {
            _savedLocation.value = Location.CityName(cityName)
        }
    }
}