package com.fwhyn.myapuri

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class CalculateModule {

    @Provides
    fun calculateXyModel(): Calculate<CalculateXyModel.Input, Int> {
        return CalculateXyModel()
    }
}