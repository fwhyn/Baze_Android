package com.fwhyn.app.deandro.common.di

import com.fwhyn.lib.baze.data.helper.network.AlwaysOnlineNetworkMonitor
import com.fwhyn.lib.baze.data.helper.network.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class HelperModule {

    @Provides
    @Singleton
    fun provideNetworkMonitor(): NetworkMonitor = AlwaysOnlineNetworkMonitor()
}