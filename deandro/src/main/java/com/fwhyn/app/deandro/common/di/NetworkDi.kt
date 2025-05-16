package com.fwhyn.app.deandro.common.di

import com.fwhyn.lib.baze.network.data.helper.AlwaysOnlineNetworkMonitor
import com.fwhyn.lib.baze.network.data.helper.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkDi {

    @Provides
    @Singleton
    fun provideNetworkMonitor(): NetworkMonitor = AlwaysOnlineNetworkMonitor()
}