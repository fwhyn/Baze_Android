package com.fwhyn.deandro.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
class ActivityModule {

//    @Provides
//    @ActivityScoped
//    fun provideActivityState(): ActivityState = ActivityState()
}