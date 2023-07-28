package com.example.aplikacijazaprognozuvremena.homeview

import android.util.Log
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

    private val _todayMaxTemp = MutableLiveData<String>()
    val todayMaxTemp: LiveData<String> = _todayMaxTemp

    private val _todayMinTemp = MutableLiveData<String>()
    val todayMinTemp: LiveData<String> = _todayMinTemp

    private val _currentDateTime = MutableLiveData<String>()
    val currentDateTime: LiveData<String> = _currentDateTime

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    /**
     * Call getWeatherData() on init so we can display data immediately.
     */
    init {
        getWeatherData()
    }

    /**
     * Gets weather data information from the Weather API Retrofit service and updates the
     * [WeatherData] [LiveData].
     */
    private fun getWeatherData() {
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                _weatherData.value = WeatherApi.retrofitService.getWeather()
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

    /**
     * Updates the properties that are bound to the views in the layout with the weather data.
     */
    /*private fun updateProperties() {
        weatherData.value?.let { data ->
            address.value = data.name
            dateAndTime.value = dateFormat.format(Date(data.dt * 1000))
            statusText.value = data.weather[0].main
            currentTemperature.value =
                String.format("%.1f°C", data.main.temp - 273.15) // iz kelvina u celzijus
            todayImage.value = getImageResource(data.weather[0].icon)
            todayHumidity.value =
                String.format("%d%%", data.main.humidity) // nadodaj postotak
            todayWindSpeed.value =
                String.format("%.1f m/s", data.wind.speed) // nadodaj mjernu jedinicu
            todayMaxTemp.value =
                String.format("%.1f°C", data.main.temp_max - 273.15) // iz kelvina u celzijus
            todayMinTemp.value =
                String.format("%.1f°C", data.main.temp_min - 273.15) // iz kelvina u celzijus
            //
        }
    }*/

    /*private fun updateProperties() {
        weatherData.value?.let { data ->
            weatherData.value = data
        }
    }*/

    private fun updateProperties() {
        weatherData.value?.let { data ->
            val kelvinToCelsius = {kelvin: Double -> kelvin - 273.15}
            _currentDateTime.value = dateFormat.format(Date())
            _formattedSunrise.value = timeFormat.format(Date(data.sys.sunrise * 1000))
            _formattedSunset.value = timeFormat.format(Date(data.sys.sunset * 1000))
            _formattedHumidity.value = "${data.main.humidity}%"
            _formattedPressure.value = "${data.main.pressure} hPa"
            _formattedWindSpeed.value = "${data.wind.speed} m/s"
            //_currentTemperature.value = kelvinToCelsius(data.main.temp).toString()
            _currentTemperature.value = round(kelvinToCelsius(data.main.temp)).toInt().toString() + " °C"
            _todayMaxTemp.value = kelvinToCelsius(data.main.temp_max).toString()
            _todayMinTemp.value = kelvinToCelsius(data.main.temp_min).toString()
        }
    }



    private fun clearProperties() {
        weatherData.value = null
    }

    /**
     * Returns the image resource id that corresponds to the weather icon code.
     */
    private fun getImageResource(icon: String): Int {
        return when (icon) {
            "01d" -> R.drawable.sunny
            "01n" -> R.drawable.clear_night
            "02d" -> R.drawable.partly_cloudy_day
            "02n" -> R.drawable.partly_cloudy_night
            "03d", "03n", "04d", "04n" -> R.drawable.cloudy
            "09d", "09n" -> R.drawable.rainy
            "10d", "10n" -> R.drawable.rainy_sunny
            "11d", "11n" -> R.drawable.thunderstorm
            "13d", "13n" -> R.drawable.snowy
            "50d", "50n" -> R.drawable.foggy
            else -> R.drawable.unknown
        }
    }

    fun getWeatherDataValue(): LiveData<WeatherData?> {
        return weatherData
    }
}
