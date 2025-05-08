package com.fwhyn.deandro.di

import com.fwhyn.lib.baze.ui.main.ActivityRetainedState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
class ActivityRetainedModule {

    @Provides
    @ActivityRetainedScoped
    fun provideActivityRetainedState(): ActivityRetainedState = ActivityRetainedState()
}