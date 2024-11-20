package com.fwhyn.appsample.di

import com.fwhyn.ui.main.MainUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class CommonModule {

    @Provides
    fun provideMainUiState(): MainUiState = MainUiState()
}