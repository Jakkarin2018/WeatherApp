package com.example.weatherapp.forecast.weekly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.weatherapp.R
import com.example.weatherapp.api.Routes
import com.example.weatherapp.api.model.DailyForecast
import com.example.weatherapp.databinding.ItemDailyForecastBinding
import com.example.weatherapp.util.TempDisplaySettingManager
import com.example.weatherapp.util.formatTempForDisplay
import java.text.SimpleDateFormat
import java.util.*

class DailyForecastViewHolder(
    private val binding: ItemDailyForecastBinding,
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val dateFormat: SimpleDateFormat
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dailyForecast: DailyForecast) {
        binding.tempText.text =
            formatTempForDisplay(
                dailyForecast.temp.max, tempDisplaySettingManager.getTempDisplaySetting()
            )
        binding.descriptionText.text = dailyForecast.weather[0].description
        binding.humidityText.text = dailyForecast.humidity.toString()
        binding.dateText.text = dateFormat.format(Date(dailyForecast.date * 1000))

        val iconId = dailyForecast.weather[0].icon
        binding.forecastIcon.load(String.format(Routes.OPEN_WEATHER_MAP_ICON, iconId))
    }
}

class DailyForecastListAdapter(
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val clickHandler: (DailyForecast) -> Unit
) : ListAdapter<DailyForecast, DailyForecastViewHolder>(DIFF_CONFIG) {

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val binding =
            ItemDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val dateFormat =
            SimpleDateFormat(parent.context.getString(R.string.date_format), Locale.getDefault())
        return DailyForecastViewHolder(
            binding,
            tempDisplaySettingManager,
            dateFormat
        )
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
//        holder.itemView.setOnClickListener {
//            clickHandler(getItem(position))
//        }
    }
}