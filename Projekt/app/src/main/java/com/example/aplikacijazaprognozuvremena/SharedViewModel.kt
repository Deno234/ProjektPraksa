package com.example.aplikacijazaprognozuvremena

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val selectedCity = MutableLiveData<City>()
}