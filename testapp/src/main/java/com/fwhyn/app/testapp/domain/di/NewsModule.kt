package com.fwhyn.app.testapp.domain.di

import com.fwhyn.app.testapp.data.repository.NewsRepository
import com.fwhyn.app.testapp.domain.usecase.GetNewsUseCase
import com.fwhyn.app.testapp.domain.usecase.GetNewsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class NewsModule {

    @Provides
    fun provideGetNewsUseCase(newsRepository: NewsRepository): GetNewsUseCase {
        return GetNewsUseCaseImpl(newsRepository)
    }
}