package com.fwhyn.app.testapp.data.helper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NetworkMonitorFake : NetworkMonitor {

    val delayTime = 5000L
    var onlineState = false

    override val isOnline: Flow<Boolean> =
        flowOf(true)
//        flow {
//            while (true) {
//                Log.d("NetworkMonitorFake", "Network status: $onlineState")
//
//                emit(onlineState)
//                delay(delayTime)
//                onlineState = !onlineState
//            }
//        }
}