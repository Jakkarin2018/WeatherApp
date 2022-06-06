package com.example.weatherapp.forecast.current

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
import com.example.weatherapp.databinding.FragmentCurrentForecastBinding
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.Location
import com.example.weatherapp.repository.LocationRepository
import com.example.weatherapp.util.TempDisplaySettingManager

class CurrentForecastFragment : Fragment() {
    private var _binding: FragmentCurrentForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CurrentForecastViewModel

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var locationRepository: LocationRepository
    private lateinit var forecastRepository: ForecastRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentForecastBinding.inflate(inflater, container, false)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        locationRepository = LocationRepository(requireContext())
        forecastRepository = ForecastRepository(getString(R.string.language_code))
        viewModel = ViewModelProvider(
            requireActivity(),
            CurrentForecastViewModelFactory(tempDisplaySettingManager, forecastRepository)
        ).get() as CurrentForecastViewModel

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        locationRepository.savedLocation.observe(viewLifecycleOwner, Observer { location ->
            when (location) {
                is Location.CityName -> viewModel.onLocationObtained(location.cityName)
                else -> viewModel.onLocationError()
            }
        })
    }

    private fun showLocationEntry() {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }
}