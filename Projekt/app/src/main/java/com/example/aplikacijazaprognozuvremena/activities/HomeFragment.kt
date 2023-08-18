package com.example.aplikacijazaprognozuvremena.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.viewmodel.SharedViewModel
import com.example.aplikacijazaprognozuvremena.databinding.HomeFragmentBinding

@SuppressLint("StaticFieldLeak")
private lateinit var binding: HomeFragmentBinding

class HomeFragment : Fragment(), OnCitySelectedListener {

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root

    }

    override fun onCitySelected(city: City) {
        viewModel.selectedCity.value = city
        Log.d("HomeFragment", "onCitySelected")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationTextView: TextView = binding.address
        locationTextView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        viewModel.selectedCity.observe(viewLifecycleOwner)
        { city ->
            locationTextView.text = city.name

            viewModel.getWeatherData(city.name)
            Log.d("HomeFragment", "selected City observe")
        }
    }


}