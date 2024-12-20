package com.fwhyn.deandro.di

import com.fwhyn.data.model.Status
import com.fwhyn.deandro.ui.common.GeneralMessageHandler
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