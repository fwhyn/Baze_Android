package com.fwhyn.lib.baze.network.data.helper

import android.Manifest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
}