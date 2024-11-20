package com.fwhyn.appsample.di

import com.fwhyn.appsample.ui.common.GeneralMessageHandler
import com.fwhyn.data.model.Status
import com.fwhyn.ui.helper.MessageHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class MessageModule {

    @Provides
    fun provideGeneralMessageHandler(): MessageHandler<Status> = GeneralMessageHandler()
}