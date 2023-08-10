package com.example.aplikacijazaprognozuvremena.networkstatus

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.example.aplikacijazaprognozuvremena.R
import com.example.aplikacijazaprognozuvremena.viewmodel.SharedViewModel

class NetworkStatusMonitor(context: Context, private val viewModel: SharedViewModel) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            viewModel.getWeatherData(viewModel.selectedCity.value?.name ?: "")
        }

        override fun onLost(network: Network) {
            viewModel._weatherData.postValue(null)
            viewModel._currentDateTime.postValue(SharedViewModel.stanje1)
            viewModel._formattedSunrise.postValue(SharedViewModel.stanje1)
            viewModel._formattedSunset.postValue(SharedViewModel.stanje1)
            viewModel._formattedHumidity.postValue(SharedViewModel.stanje1)
            viewModel._formattedWindSpeed.postValue(SharedViewModel.stanje1)
            viewModel._formattedPressure.postValue(SharedViewModel.stanje1)
            viewModel._feelsLike.postValue(SharedViewModel.stanje1)
            viewModel._todayMinMax.postValue(SharedViewModel.stanje2)
            viewModel._weatherImageResource.postValue(R.drawable.unknown)
            viewModel._currentTemperature.postValue(SharedViewModel.stanje2)
        }
    }

    fun startMonitoring() {
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun stopMonitoring() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
