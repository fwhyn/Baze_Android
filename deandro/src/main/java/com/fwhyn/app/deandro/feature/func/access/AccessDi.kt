package com.fwhyn.app.deandro.feature.func.access

import com.fwhyn.app.deandro.BuildConfig
import com.fwhyn.app.deandro.feature.func.access.data.remote.GoogleDriveAccess
import com.fwhyn.app.deandro.feature.func.access.data.repository.AccessRepository
import com.fwhyn.app.deandro.feature.func.access.data.repository.AccessRepositoryFake
import com.fwhyn.app.deandro.feature.func.access.data.repository.AccessRepositoryInterface
import com.fwhyn.app.deandro.feature.func.access.domain.usecase.GetAccessUseCase
import com.fwhyn.app.deandro.feature.func.access.domain.usecase.GetAccessUseCaseInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AccessDi {

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
            "Fake" -> AccessRepositoryFake()
            "Real" -> AccessRepository(googleDriveAccess)
            else -> throw IllegalArgumentException("Unknown flavor: ${BuildConfig.FLAVOR}")
        }
    }
}