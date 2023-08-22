package com.example.aplikacijazaprognozuvremena.activities

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikacijazaprognozuvremena.publicdata.Cities.cities
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.databinding.FragmentSearchBinding
import com.example.aplikacijazaprognozuvremena.searchadapter.SearchAdapter
import com.example.aplikacijazaprognozuvremena.viewmodel.SharedViewModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(val name: String, val country: String) : Parcelable


interface OnCitySelectedListener {
    fun onCitySelected(city: City)
}

class SearchFragment : Fragment(), OnCitySelectedListener {
    private lateinit var binding: FragmentSearchBinding
    private val cityList = cities
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCitySelected(city: City) {
        viewModel.setSelectedCity(city)
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.citiesRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SearchAdapter(cityList, this)
        binding.citiesRecyclerView.adapter = adapter

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                val filteredCities = cityList.filter { city ->
                    city.name.contains(searchText, ignoreCase = true)
                }
                adapter.submitList(filteredCities)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        @Suppress("UNUSED_VARIABLE")
        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.stopFetchingData()
            findNavController().navigate(R.id.homeFragment)
        }
    }
}