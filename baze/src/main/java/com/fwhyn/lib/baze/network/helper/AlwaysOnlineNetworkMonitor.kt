package com.fwhyn.lib.baze.network.helper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AlwaysOnlineNetworkMonitor : NetworkMonitor {
    override val isOnline: Flow<Boolean> = flowOf(true)
}
