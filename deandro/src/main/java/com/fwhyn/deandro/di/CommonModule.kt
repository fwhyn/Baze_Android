package com.fwhyn.deandro.di

import com.fwhyn.baze.ui.main.MainUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
class CommonModule {

    @Provides
    @ActivityRetainedScoped
    fun provideMainUiState(): MainUiState = MainUiState()
}