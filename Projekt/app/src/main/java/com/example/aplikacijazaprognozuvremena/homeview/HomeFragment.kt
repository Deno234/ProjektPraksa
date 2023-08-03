package com.example.aplikacijazaprognozuvremena.homeview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aplikacijazaprognozuvremena.City
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.SearchFragment
import com.example.aplikacijazaprognozuvremena.SharedViewModel
import com.example.aplikacijazaprognozuvremena.databinding.HomeFragmentBinding

class HomeFragment : Fragment(), SearchFragment.onCitySelectedListener {

    private val viewModel: HomeFragmentModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = HomeFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root

    }

    override fun onCitySelected(city: City) {
        val viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.selectedCity.value = city
        Log.d("HomeFragment", "onCitySelected")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationTextView: TextView = view.findViewById(R.id.address)
        locationTextView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.selectedCity.observe(viewLifecycleOwner)
        { city ->
            locationTextView.text = city.name

            viewModel.getWeatherData(city.name)
            Log.d("HomeFragment", "selected City observe")
        }
    }


}