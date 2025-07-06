package com.fwhyn.app.deandro.common.di

import com.fwhyn.app.deandro.common.ui.handler.GeneralStringResourceManager
import com.fwhyn.lib.baze.common.model.Status
import com.fwhyn.lib.baze.compose.helper.ActivityRetainedState
import com.fwhyn.lib.baze.string.helper.StringIdManager
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
    fun provideGeneralMessageHandler(): StringIdManager<Status> = GeneralStringResourceManager()
}