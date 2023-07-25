package com.example.aplikacijazaprognozuvremena.homeview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.network.WeatherData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class HomeFragmentModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<WeatherApiStatus>()
    private val _weatherData = MutableLiveData<WeatherData>()

    // The external immutable LiveData for the weather data and the request status
    val weatherData: LiveData<WeatherData> = _weatherData
    val status: LiveData<WeatherApiStatus> = _status

    // The properties that are bound to the views in the layout
    val address = MutableLiveData<String>()
    val dateAndTime = MutableLiveData<String>()
    val statusText = MutableLiveData<String>()
    val currentTemperature = MutableLiveData<String>()
    val todayImage = MutableLiveData<Int>()
    val todayHumidity = MutableLiveData<String>()
    val todayWindSpeed = MutableLiveData<String>()
    val todayMaxTemp = MutableLiveData<String>()
    val todayMinTemp = MutableLiveData<String>()
    val tomorrowImage = MutableLiveData<Int>()
    val tomorrowHumidity = MutableLiveData<String>()
    val tomorrowWindSpeed = MutableLiveData<String>()
    val tomorrowMaxTemp = MutableLiveData<String>()
    val tomorrowMinTemp = MutableLiveData<String>()

    // The formatter for displaying the date and time
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
                _status.value = WeatherApiStatus.DONE
                updateProperties()
            } catch (e: Exception) {
                _status.value = WeatherApiStatus.ERROR
                _weatherData.value = null
                clearProperties()
            }
        }
    }

    /**
     * Updates the properties that are bound to the views in the layout with the weather data.
     */
    private fun updateProperties() {
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
            // TODO: get the forecast data for tomorrow and update the properties accordingly
        }
    }

    /**
     * Clears the properties that are bound to the views in the layout when there is an error.
     */
    private fun clearProperties() {
        address.value = ""
        dateAndTime.value = ""
        statusText.value = ""
        currentTemperature.value = ""
        todayImage.value = null
        todayHumidity.value = ""
        todayWindSpeed.value = ""
        todayMaxTemp.value = ""
        todayMinTemp.value = ""
        tomorrowImage.value = null
        tomorrowHumidity.value = ""
        tomorrowWindSpeed.value = ""
        tomorrowMaxTemp.value = ""
        tomorrowMinTemp.value = ""
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
}
