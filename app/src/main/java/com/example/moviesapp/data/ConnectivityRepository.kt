package com.example.moviesapp.data

import android.Manifest
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed interface ConnectivityState {
    data object Unavailable: ConnectivityState
    data object Available: ConnectivityState
}

interface ConnectivityRepository {
    val connectivityState: StateFlow<ConnectivityState>
}


class DefaultConnectivityRepository @Inject constructor(
    private val connectivityManager: ConnectivityManager
): ConnectivityRepository {
    private fun getCurrentConnectivityState(): ConnectivityState {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        return if (isConnected) ConnectivityState.Available else ConnectivityState.Unavailable
    }
    private val _connectivityState =
        MutableStateFlow<ConnectivityState>(getCurrentConnectivityState())

    private val callback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _connectivityState.value = ConnectivityState.Available
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _connectivityState.value = ConnectivityState.Unavailable
        }
    }

    override val connectivityState: StateFlow<ConnectivityState> = _connectivityState.asStateFlow()

    init {
        connectivityManager.registerDefaultNetworkCallback(callback)
    }
}

