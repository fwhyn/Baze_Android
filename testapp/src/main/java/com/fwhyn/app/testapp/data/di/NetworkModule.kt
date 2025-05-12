package com.fwhyn.app.testapp.data.di

import com.fwhyn.app.testapp.data.helper.NetworkMonitor
import com.fwhyn.app.testapp.data.helper.NetworkMonitorFake
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkMonitor(): NetworkMonitor {
        return NetworkMonitorFake()
    }
}