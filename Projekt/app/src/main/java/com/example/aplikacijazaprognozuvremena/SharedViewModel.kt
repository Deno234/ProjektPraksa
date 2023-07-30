package com.example.aplikacijazaprognozuvremena

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    private val selectedCityKey = "selected_city"
    val selectedCity = MutableLiveData<City>(
        City(sharedPreferences.getString(selectedCityKey, "Zagreb")!!, "hr")
    )

    fun setSelectedCity(city: City) {
        selectedCity.value = city
        sharedPreferences.edit().putString(selectedCityKey, city.name).apply()
    }
}

