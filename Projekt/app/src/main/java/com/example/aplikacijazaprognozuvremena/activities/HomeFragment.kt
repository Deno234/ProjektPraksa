package com.example.aplikacijazaprognozuvremena.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.viewmodel.SharedViewModel
import com.example.aplikacijazaprognozuvremena.databinding.HomeFragmentBinding

class HomeFragment : Fragment(), OnCitySelectedListener {

    private lateinit var binding: HomeFragmentBinding
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
        viewModel.setSelectedCity(city)
        Log.d("HomeFragment", "onCitySelected")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.address.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        viewModel.selectedCity.observe(viewLifecycleOwner)
        { city ->
            binding.address.text = city.name

            viewModel.getWeatherData(city)
            Log.d("HomeFragment", "selected City observe")
        }
    }


}