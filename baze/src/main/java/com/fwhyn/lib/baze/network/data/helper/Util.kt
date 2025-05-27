package com.fwhyn.lib.baze.network.data.helper

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission

object Util {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @JvmStatic
    fun networkIsOnline(connectivityManager: ConnectivityManager): Boolean {
        return connectivityManager.activeNetwork?.let { network ->
            connectivityManager
                .getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } == true
    }

    @JvmStatic
    fun getConnectivityManager(
        context: Context
    ): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @JvmStatic
    fun getNetworkRequestInternet(): NetworkRequest = NetworkRequest
        .Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
}