package com.fwhyn.appsample.di

import com.fwhyn.data.helper.network.AlwaysOnlineNetworkMonitor
import com.fwhyn.data.helper.network.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface HelperModule {

    @Binds
    fun bindsNetworkMonitor(networkMonitor: AlwaysOnlineNetworkMonitor): NetworkMonitor
}