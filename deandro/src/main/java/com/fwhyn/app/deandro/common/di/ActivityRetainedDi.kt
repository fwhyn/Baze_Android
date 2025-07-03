package com.fwhyn.app.deandro.common.di

import com.fwhyn.app.deandro.common.ui.handler.GeneralStringResourceManager
import com.fwhyn.lib.baze.common.data.model.Status
import com.fwhyn.lib.baze.common.ui.helper.StringResourceManager
import com.fwhyn.lib.baze.common.ui.main.ActivityRetainedState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
class ActivityRetainedDi {

    @Provides
    @ActivityRetainedScoped
    fun provideActivityRetainedState(): ActivityRetainedState = ActivityRetainedState()

    @Provides
    fun provideGeneralMessageHandler(): StringResourceManager<Status> = GeneralStringResourceManager()
}