package com.example.weatherapp.forecast.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.api.model.DailyForecast
import com.example.weatherapp.databinding.FragmentWeeklyForecastBinding
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.Location
import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.util.Status
import com.example.weatherapp.util.TempDisplaySettingManager

class WeeklyForecastFragment : Fragment() {
    private var _binding: FragmentWeeklyForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WeeklyForecastViewModel

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var locationRepository: LocationRepository
    private lateinit var forecastRepository: ForecastRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeeklyForecastBinding.inflate(inflater, container, false)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        locationRepository = LocationRepository(requireContext())
        forecastRepository = ForecastRepository(getString(R.string.language_code))
        viewModel = ViewModelProvider(
            requireActivity(),
            WeeklyForecastViewModelFactory(forecastRepository)
        ).get() as WeeklyForecastViewModel

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        val dailyForecastAdapter = DailyForecastListAdapter(tempDisplaySettingManager) { forecast ->
            showForecastDetails(forecast)
        }
        binding.dailyForecastList.adapter = dailyForecastAdapter

        locationRepository.savedLocation.observe(viewLifecycleOwner, Observer { location ->
            when (location) {
                is Location.CityName -> viewModel.onLocationObtained(location.cityName)
                else -> viewModel.onLocationError()
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState.status == Status.SUCCESS) {
                dailyForecastAdapter.submitList(viewState.data!!.daily)
            }
        })
    }

    private fun showLocationEntry() {
        val action =
            WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    private fun showForecastDetails(forecast: DailyForecast) {
        val iconId = forecast.weather[0].icon
        val temp = forecast.temp.max
        val description = forecast.weather[0].description
        val humidity = forecast.humidity
        val date = forecast.date

        val action =
            WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(
                iconId,
                temp,
                date,
                description
            )
        findNavController().navigate(action)
    }
}