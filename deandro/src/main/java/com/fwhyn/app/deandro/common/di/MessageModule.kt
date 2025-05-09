package com.fwhyn.app.deandro.common.di

import com.fwhyn.app.deandro.common.ui.handler.GeneralMessageHandler
import com.fwhyn.lib.baze.data.model.Status
import com.fwhyn.lib.baze.ui.helper.MessageHandler
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