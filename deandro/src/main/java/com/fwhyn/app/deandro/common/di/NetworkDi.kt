package com.fwhyn.app.deandro.common.di

import com.fwhyn.lib.baze.common.data.helper.network.AlwaysOnlineNetworkMonitor
import com.fwhyn.lib.baze.common.data.helper.network.NetworkMonitor
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