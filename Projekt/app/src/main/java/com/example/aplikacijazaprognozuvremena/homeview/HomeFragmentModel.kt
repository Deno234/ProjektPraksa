package com.example.aplikacijazaprognozuvremena.homeview

import android.media.Image
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.network.WeatherData
import kotlinx.coroutines.launch
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.*

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class HomeFragmentModel : ViewModel() {

    private val _status = MutableLiveData<WeatherApiStatus>()
    private val _weatherData = MutableLiveData<WeatherData?>()

    val weatherData: MutableLiveData<WeatherData?> = _weatherData
    val status: LiveData<WeatherApiStatus> = _status

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
    val todayMaxTemp: LiveData<String> = _todayMaxTemp

    private val _todayMinTemp = MutableLiveData<String>()
    val todayMinTemp: LiveData<String> = _todayMinTemp

    private val _currentDateTime = MutableLiveData<String>()
    val currentDateTime: LiveData<String> = _currentDateTime

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    private val _todayMinMax = MutableLiveData<String>()
    val todayMinMax: LiveData<String> = _todayMinMax

    private val _weatherImageResource = MutableLiveData<Int>()
    val weatherImageResource: LiveData<Int> = _weatherImageResource

    init {
        getWeatherData("London,uk")
    }

    fun getWeatherData(city: String) {
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                _weatherData.value = WeatherApi.retrofitService.getWeather(city)
                Log.d("HomeFragmentModel", "weatherData: ${_weatherData.value}")
                _status.value = WeatherApiStatus.DONE
                updateProperties()
            } catch (e: Exception) {
                _status.value = WeatherApiStatus.ERROR
                _weatherData.value = null
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
                round(kelvinToCelsius(data.main.temp)).toInt().toString() + " °C"
            _feelsLike.value =
                round(kelvinToCelsius(data.main.feels_like)).toInt().toString() + " °C"
            _todayMaxTemp.value =
                round(kelvinToCelsius(data.main.temp_max)).toInt().toString() + " °C"
            _todayMinTemp.value =
                round(kelvinToCelsius(data.main.temp_min)).toInt().toString() + " °C"
            _todayMinMax.value = "${_todayMinTemp.value} /\n ${todayMaxTemp.value}  "
            val weatherCondition = data.weather[0].main
            val weatherImageResource = when (weatherCondition) {
                "Clouds" -> R.drawable.cloudy
                "Clear" -> R.drawable.sunny
                "Rain" -> R.drawable.rainy
                "Snow" -> R.drawable.snowy
                "Fog" -> R.drawable.foggy
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
            else -> weatherCondition
        }
    }
}

