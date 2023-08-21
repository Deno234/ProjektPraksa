package com.example.aplikacijazaprognozuvremena.networkstatus

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import com.example.aplikacijazaprognozuvremena.activities.City
import com.example.aplikacijazaprognozuvremena.viewmodel.SharedViewModel

class NetworkStatusMonitor(context: Context, private val viewModel: SharedViewModel) {
    private var lastCity: City? = null
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            viewModel.selectedCity.value?.let { city ->
                if (city != lastCity || viewModel.status.value == "Učitava se...") {
                    viewModel.getWeatherData(city.name + "," + city.country)
                    Log.d("NetworkStatusMonitor", "called getWeatherData")
                    lastCity = city
                }
            }
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