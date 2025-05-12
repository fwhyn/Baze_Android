package com.fwhyn.app.testapp.data.di

import com.fwhyn.app.testapp.data.helper.NetworkMonitor
import com.fwhyn.app.testapp.data.local.NewsLocalDataSource
import com.fwhyn.app.testapp.data.remote.NewsApi
import com.fwhyn.app.testapp.data.remote.NewsApiImpl
import com.fwhyn.app.testapp.data.remote.NewsRemoteDataSource
import com.fwhyn.app.testapp.data.repository.NewsRepository
import com.fwhyn.app.testapp.data.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NewsModule {

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsLocalDataSource: NewsLocalDataSource,
        newsRemoteDataSource: NewsRemoteDataSource,
        networkMonitor: NetworkMonitor
    ): NewsRepository {
        return NewsRepositoryImpl(newsLocalDataSource, newsRemoteDataSource, networkMonitor)
    }

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return NewsApiImpl()
    }
}