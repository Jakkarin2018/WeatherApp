package com.example.weatherapp.forecast.wholeday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.weatherapp.R
import com.example.weatherapp.api.Routes
import com.example.weatherapp.api.model.HourForecast
import com.example.weatherapp.databinding.ItemDailyForecastBinding
import com.example.weatherapp.util.TempDisplaySettingManager
import com.example.weatherapp.util.formatTempForDisplay
import java.text.SimpleDateFormat
import java.util.*


class WholeDayForecastViewHolder(
    private val binding: ItemDailyForecastBinding,
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val dateFormat: SimpleDateFormat
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(hourForecast: HourForecast) {
        binding.tempText.text =
            formatTempForDisplay(
                hourForecast.temp, tempDisplaySettingManager.getTempDisplaySetting()
            )
        binding.descriptionText.text = hourForecast.weather[0].description
        binding.humidityText.text = hourForecast.humidity.toString()
        dateFormat.applyPattern("dd-MM-yyyy HH:mm")
        binding.dateText.text =  dateFormat.format(Date(hourForecast.date * 1000))

        val iconId = hourForecast.weather[0].icon
        binding.forecastIcon.load(String.format(Routes.OPEN_WEATHER_MAP_ICON, iconId))
    }
}

class HourlyForecastListAdapter (
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val clickHandler: (HourForecast) -> Unit
) : ListAdapter<HourForecast, WholeDayForecastViewHolder>(HourlyForecastListAdapter.DIFF_CONFIG) {

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<HourForecast>() {
            override fun areItemsTheSame(oldItem: HourForecast, newItem: HourForecast): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(
                oldItem: HourForecast,
                newItem: HourForecast
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WholeDayForecastViewHolder {
        val binding =
            ItemDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val dateFormat =
            SimpleDateFormat(parent.context.getString(R.string.date_format), Locale.getDefault())
        return WholeDayForecastViewHolder(
            binding,
            tempDisplaySettingManager,
            dateFormat
        )
    }

    override fun onBindViewHolder(holder: WholeDayForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
//        holder.itemView.setOnClickListener {
//            clickHandler(getItem(position))
//        }
    }

}