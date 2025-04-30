package com.fwhyn.deandro.access.di

import com.fwhyn.deandro.BuildConfig
import com.fwhyn.deandro.access.data.remote.GoogleDriveAccess
import com.fwhyn.deandro.access.data.repository.AccessRepository
import com.fwhyn.deandro.access.data.repository.AccessRepositoryInterface
import com.fwhyn.deandro.access.domain.usecase.GetAccessUseCase
import com.fwhyn.deandro.access.domain.usecase.GetAccessUseCaseInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AccessModule {

    @Provides
    fun provideGetAccessUseCase(
        accessRepository: AccessRepositoryInterface,
    ): GetAccessUseCaseInterface {
        return GetAccessUseCase(accessRepository)
    }

    @Provides
    fun provideAccessRepository(
        googleDriveAccess: GoogleDriveAccess
    ): AccessRepositoryInterface {
        return when (BuildConfig.FLAVOR) {
            "Fake" -> TODO()
            "Real" -> AccessRepository(googleDriveAccess)
            else -> throw IllegalArgumentException("Unknown flavor: ${BuildConfig.FLAVOR}")
        }
    }
}