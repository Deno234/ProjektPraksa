package com.example.aplikacijazaprognozuvremena.networkstatus

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.example.aplikacijazaprognozuvremena.viewmodel.SharedViewModel

class NetworkStatusMonitor(context: Context, private val viewModel: SharedViewModel) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            viewModel.getWeatherData(viewModel.selectedCity.value?.name ?: "")
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