package com.fwhyn.lib.baze.network.data.helper

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}