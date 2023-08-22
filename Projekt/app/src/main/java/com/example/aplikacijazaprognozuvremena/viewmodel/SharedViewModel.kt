package com.example.aplikacijazaprognozuvremena.viewmodel

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.activities.City
import com.example.aplikacijazaprognozuvremena.network.dataclasses.WeatherData
import com.example.aplikacijazaprognozuvremena.networkstatus.NetworkStatusMonitor
import com.example.aplikacijazaprognozuvremena.publicdata.WeatherImage.WEATHER_IMAGE_RESOURCES
import com.example.aplikacijazaprognozuvremena.publicdata.WeatherTranslation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val LOADING = "Učitava se..."
        const val DOTS = "..."
    }

    private val sharedPreferences =
        application.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    private val selectedCityKey = "selected_city"
    private val selectedCityCountry = "selected_country"
    private val _selectedCity = MutableLiveData(
        City(
            sharedPreferences.getString(selectedCityKey, "Zagreb") ?: "Zagreb",
            sharedPreferences.getString(selectedCityCountry, "hr") ?: "hr"
        )
    )
    val selectedCity: LiveData<City> = _selectedCity

    private val _weatherData = MutableLiveData<WeatherData?>()

    private val weatherData: MutableLiveData<WeatherData?> = _weatherData

    private val _formattedSunrise = MutableLiveData<String>()
    val formattedSunrise: LiveData<String> = _formattedSunrise

    private val _formattedSunset = MutableLiveData<String>()
    val formattedSunset: LiveData<String> = _formattedSunset

    private val _formattedHumidity = MutableLiveData<String>()
    val formattedHumidity: LiveData<String> = _formattedHumidity

    private val _formattedPressure = MutableLiveData<String>()
    val formattedPressure: LiveData<String> = _formattedPressure

    private val _formattedWindSpeed = MutableLiveData<String>()
    val formattedWindSpeed: LiveData<String> = _formattedWindSpeed

    private val _currentTemperature = MutableLiveData<String>()
    val currentTemperature: LiveData<String> = _currentTemperature

    private val _feelsLike = MutableLiveData<String>()
    val feelsLike: LiveData<String> = _feelsLike

    private val _todayMaxTemp = MutableLiveData<String>()
    private val todayMaxTemp: LiveData<String> = _todayMaxTemp

    private val _todayMinTemp = MutableLiveData<String>()

    private val _currentDateTime = MutableLiveData<String>()
    val currentDateTime: LiveData<String> = _currentDateTime

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    private val _todayMinMax = MutableLiveData<String>()
    val todayMinMax: LiveData<String> = _todayMinMax

    private val _weatherImageResource = MutableLiveData<Int>()
    val weatherImageResource: LiveData<Int> = _weatherImageResource

    private val networkStatusMonitor = NetworkStatusMonitor(application, this)

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private var lastCity: City? = null
    private var shouldFetchData = true

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            _selectedCity.value?.let {
                getWeatherData(it)
            }
            handler.postDelayed(this, 30 * 60 * 1000) //svakih 30 minuta
        }
    }


    init {
        _selectedCity.value?.let {
            getWeatherData(it)
        }
        networkStatusMonitor.startMonitoring()
        handler.post(runnable)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(runnable)
        networkStatusMonitor.stopMonitoring()
    }

    fun getWeatherData(city: City) {

        if (!shouldFetchData) {
            shouldFetchData = true
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                lastCity = city
                val weatherData2 =
                    WeatherApi.retrofitService.getWeather(city.name + "," + city.country)
                withContext(Dispatchers.Main) {
                    _weatherData.value = weatherData2
                    Log.d("HomeFragmentModel", "weatherData: ${_weatherData.value}")
                    updateProperties()
                }
            } catch (e: Exception) {
                lastCity = city
                withContext(Dispatchers.Main) {
                    _weatherData.value = null
                    _currentDateTime.value = LOADING
                    _formattedSunrise.value = LOADING
                    _formattedSunset.value = LOADING
                    _formattedHumidity.value = LOADING
                    _formattedWindSpeed.value = LOADING
                    _currentTemperature.value = DOTS
                    _formattedPressure.value = LOADING
                    _feelsLike.value = LOADING
                    _todayMinMax.value = DOTS
                    _status.value = LOADING
                    _weatherImageResource.value = R.drawable.unknown
                    Log.d("HomeFragmentModel", "weatherDataNULL: ${_weatherData.value}")
                    Log.e("HomeFragmentModel", "$e")
                    clearProperties()
                }
            }
        }
    }

    private fun updateProperties() {
        weatherData.value?.let { data ->

            val kelvinToCelsius = { kelvin: Double -> kelvin - 273.15 }
            _status.value = translateWeatherCondition(data.weather[0].main)
            _currentDateTime.value = dateFormat.format(Date())
            _formattedSunrise.value = timeFormat.format(Date(data.sys.sunrise * 1000))
            _formattedSunset.value = timeFormat.format(Date(data.sys.sunset * 1000))
            _formattedHumidity.value = "${data.main.humidity}%"
            _formattedPressure.value = "${data.main.pressure} hPa"
            _formattedWindSpeed.value = "${data.wind.speed} m/s"
            _currentTemperature.value =
                kelvinToCelsius(data.main.temp).roundToInt().toString() + " °C"
            _feelsLike.value =
                kelvinToCelsius(data.main.feels_like).roundToInt().toString() + " °C"
            _todayMaxTemp.value =
                kelvinToCelsius(data.main.temp_max).roundToInt().toString() + " °C"
            _todayMinTemp.value =
                kelvinToCelsius(data.main.temp_min).roundToInt().toString() + " °C"
            _todayMinMax.value = "${_todayMinTemp.value} /\n ${todayMaxTemp.value}  "
            _weatherImageResource.value = getWeatherImageResource(data.weather[0].main)
        }
    }

    fun checkAndLoadData() {
        selectedCity.value?.let { city ->
            if (city != lastCity || status.value == LOADING) {
                getWeatherData(city)
                Log.d("NetworkStatusMonitor", "called getWeatherData")
                lastCity = city
            }
        }
    }


    private fun clearProperties() {
        weatherData.value = null
    }

    fun getWeatherDataValue(): LiveData<WeatherData?> {
        return weatherData
    }

    private fun getWeatherImageResource(status: String): Int {
        return WEATHER_IMAGE_RESOURCES[status] ?: R.drawable.unknown
    }

    private fun translateWeatherCondition(weatherCondition: String?): String? {
        return WeatherTranslation.translate(weatherCondition)
    }

    fun setSelectedCity(city: City) {
        _selectedCity.value = city
        sharedPreferences.edit().putString(selectedCityKey, city.name).apply()
        sharedPreferences.edit().putString(selectedCityCountry, city.country).apply()
        shouldFetchData = true
    }

    fun stopFetchingData() {
        shouldFetchData = false
    }
}



