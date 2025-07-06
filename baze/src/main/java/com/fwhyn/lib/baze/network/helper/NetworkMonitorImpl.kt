package com.fwhyn.lib.baze.network.helper

import android.Manifest
import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged


class NetworkMonitorImpl(
    private val connectivityManager: ConnectivityManager,
    private val networkRequest: NetworkRequest
) : NetworkMonitor {

    @SuppressLint("MissingPermission")
    override val isOnline: Flow<Boolean> = callbackFlow @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE) {
        // Initial status check
        val currentStatus = Util.networkIsOnline(connectivityManager)

        trySend(currentStatus) // Send initial value

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }

            override fun onUnavailable() {
                trySend(false)
            }
        }

        register(callback)

        awaitClose {
            unregister(callback)
        }
    }.distinctUntilChanged()


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun register(callback: ConnectivityManager.NetworkCallback) {
        connectivityManager.registerNetworkCallback(networkRequest, callback)
    }

    private fun unregister(callback: ConnectivityManager.NetworkCallback) {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}