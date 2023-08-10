package com.example.aplikacijazaprognozuvremena.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.activities.City
import com.example.aplikacijazaprognozuvremena.network.dataclasses.WeatherData
import com.example.aplikacijazaprognozuvremena.networkstatus.NetworkStatusMonitor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val stanje1 = "Učitava se..."
        const val stanje2 = "..."
    }

    private val sharedPreferences =
        application.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    private val selectedCityKey = "selected_city"
    val selectedCity = MutableLiveData(
        City(sharedPreferences.getString(selectedCityKey, "Zagreb")!!, "hr")
    )

    val _weatherData = MutableLiveData<WeatherData?>()

    private val weatherData: MutableLiveData<WeatherData?> = _weatherData

    val _formattedSunrise = MutableLiveData<String>()
    val formattedSunrise: LiveData<String> = _formattedSunrise

    val _formattedSunset = MutableLiveData<String>()
    val formattedSunset: LiveData<String> = _formattedSunset

    val _formattedHumidity = MutableLiveData<String>()
    val formattedHumidity: LiveData<String> = _formattedHumidity

    val _formattedPressure = MutableLiveData<String>()
    val formattedPressure: LiveData<String> = _formattedPressure

    val _formattedWindSpeed = MutableLiveData<String>()
    val formattedWindSpeed: LiveData<String> = _formattedWindSpeed

    val _currentTemperature = MutableLiveData<String>()
    val currentTemperature: LiveData<String> = _currentTemperature

    val _feelsLike = MutableLiveData<String>()
    val feelsLike: LiveData<String> = _feelsLike

    private val _todayMaxTemp = MutableLiveData<String>()
    private val todayMaxTemp: LiveData<String> = _todayMaxTemp

    private val _todayMinTemp = MutableLiveData<String>()

    val _currentDateTime = MutableLiveData<String>()
    val currentDateTime: LiveData<String> = _currentDateTime

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    val _todayMinMax = MutableLiveData<String>()
    val todayMinMax: LiveData<String> = _todayMinMax

    val _weatherImageResource = MutableLiveData<Int>()
    val weatherImageResource: LiveData<Int> = _weatherImageResource
    private val networkStatusMonitor = NetworkStatusMonitor(application, this)


    init {
        getWeatherData("Zagreb,hr")
        networkStatusMonitor.startMonitoring()
    }

    override fun onCleared() {
        super.onCleared()
        networkStatusMonitor.stopMonitoring()
    }

    fun getWeatherData(city: String) {
        viewModelScope.launch {
            try {
                _weatherData.value = WeatherApi.retrofitService.getWeather(city)
                Log.d("HomeFragmentModel", "weatherData: ${_weatherData.value}")
                updateProperties()
            } catch (e: Exception) {
                _weatherData.value = null
                _currentDateTime.value = stanje1
                _formattedSunrise.value = stanje1
                _formattedSunset.value = stanje1
                _formattedHumidity.value = stanje1
                _formattedWindSpeed.value = stanje1
                _currentTemperature.value = stanje2
                _formattedPressure.value = stanje1
                _feelsLike.value = stanje1
                _todayMinMax.value = stanje2
                _weatherImageResource.value = R.drawable.unknown
                Log.d("HomeFragmentModel", "weatherDataNULL: ${_weatherData.value}")
                Log.e("HomeFragmentModel", "$e")
                clearProperties()
            }
        }
    }

    private fun updateProperties() {
        weatherData.value?.let { data ->

            val kelvinToCelsius = { kelvin: Double -> kelvin - 273.15 }

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

            val weatherImageResource = when (data.weather[0].main) {
                "Clouds" -> R.drawable.clouds
                "Clear" -> R.drawable.sunny
                "Rain" -> R.drawable.rainy
                "Snow" -> R.drawable.snowy
                "Fog" -> R.drawable.foggy
                "Drizzle" -> R.drawable.drizzle
                else -> R.drawable.thunderstorm
            }

            _weatherImageResource.value = weatherImageResource
        }
    }


    private fun clearProperties() {
        weatherData.value = null
    }

    fun getWeatherDataValue(): LiveData<WeatherData?> {
        return weatherData
    }

    fun translateWeatherCondition(weatherCondition: String?): String {
        if (weatherCondition == null) {
            return ""
        }
        return when (weatherCondition) {
            "Clear" -> "Vedro"
            "Clouds" -> "Oblaci"
            "Rain" -> "Kiša"
            "Thunderstorm" -> "Grmljavina"
            "Snow" -> "Snijeg"
            "Fog" -> "Magla"
            "Drizzle" -> "Sitna kiša"
            else -> weatherCondition
        }
    }

    fun setSelectedCity(city: City) {
        selectedCity.value = city
        sharedPreferences.edit().putString(selectedCityKey, city.name).apply()
    }
}



