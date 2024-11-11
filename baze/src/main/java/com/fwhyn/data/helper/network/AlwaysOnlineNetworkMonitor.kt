package com.fwhyn.data.helper.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlwaysOnlineNetworkMonitor @Inject constructor() : NetworkMonitor {
    override val isOnline: Boolean = true
}
