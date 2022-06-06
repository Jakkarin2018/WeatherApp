package com.example.weatherapp.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentLocationEntryBinding
import com.example.weatherapp.repository.Location
import com.example.weatherapp.repository.LocationRepository

class LocationEntryFragment: Fragment() {
    private var _binding: FragmentLocationEntryBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationEntryBinding.inflate(inflater, container, false)
        locationRepository = LocationRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.enterButton.setOnClickListener {
            val cityName: String = binding.cityNameEditText.text.toString()

            if (cityName.isEmpty()) {
                Toast.makeText(requireContext(), R.string.city_name_entry_error, Toast.LENGTH_SHORT)
                    .show()
            } else {
                locationRepository.saveLocation(Location.CityName(cityName))
                findNavController().navigateUp()
            }
        }
    }
}