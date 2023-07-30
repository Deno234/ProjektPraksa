package com.example.aplikacijazaprognozuvremena.homeview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.aplikacijazaprognozuvremena.City
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.SearchFragment
import com.example.aplikacijazaprognozuvremena.SharedViewModel
import com.example.aplikacijazaprognozuvremena.databinding.HomeFragmentBinding

class HomeFragment() : Fragment(), SearchFragment.onCitySelectedListener {

    private val viewModel: HomeFragmentModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            /*val searchFragment = SearchFragment(findNavController())
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, searchFragment).addToBackStack(null).commit()*/
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry
        //val viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        //val viewModel2 = ViewModelProvider(requireActivity()).get(HomeFragmentModel::class.java)
        //viewModel.selectedCity.observe(viewLifecycleOwner)
        currentBackStackEntry?.savedStateHandle?.getLiveData<City>("selectedCity")?.observe(viewLifecycleOwner)
        { city ->
            // Update information on home screen based on selected city
            val cityNameTextView: TextView = view.findViewById(R.id.address)
            cityNameTextView.text = city.name

            viewModel.getWeatherData(city.name)
            Log.d("HomeFragment", "selected City observe")
        }
    }





}