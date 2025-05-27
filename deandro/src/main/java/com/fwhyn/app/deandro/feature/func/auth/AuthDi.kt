package com.fwhyn.app.deandro.feature.func.auth

import com.fwhyn.app.deandro.BuildConfig
import com.fwhyn.app.deandro.common.network.api.RetrofitProvider
import com.fwhyn.app.deandro.feature.func.auth.data.local.AuthTokenLocalDataSource
import com.fwhyn.app.deandro.feature.func.auth.data.remote.AuthTokenByGoogleDataSource
import com.fwhyn.app.deandro.feature.func.auth.data.remote.AuthTokenByMyServerDataSource
import com.fwhyn.app.deandro.feature.func.auth.data.remote.LoginApi
import com.fwhyn.app.deandro.feature.func.auth.data.repository.AuthTokenRepository
import com.fwhyn.app.deandro.feature.func.auth.data.repository.AuthTokenRepositoryFake
import com.fwhyn.app.deandro.feature.func.auth.data.repository.AuthTokenRepositoryImpl
import com.fwhyn.app.deandro.feature.func.auth.domain.usecase.GetAuthTokenUseCase
import com.fwhyn.app.deandro.feature.func.auth.domain.usecase.GetAuthTokenUseCaseImpl
import com.fwhyn.app.deandro.feature.func.auth.domain.usecase.SetAuthTokenUseCase
import com.fwhyn.app.deandro.feature.func.auth.domain.usecase.SetAuthTokenUseCaseImpl
import com.fwhyn.lib.baze.retrofit.api.RetrofitApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AuthDi {

    @Provides
    fun provideGetTokenUseCase(
        authTokenRepository: AuthTokenRepository,
    ): GetAuthTokenUseCase {
        return GetAuthTokenUseCaseImpl(authTokenRepository)
    }

    @Provides
    fun provideSetTokenUseCase(
        authTokenRepository: AuthTokenRepository,
    ): SetAuthTokenUseCase {
        return SetAuthTokenUseCaseImpl(authTokenRepository)
    }

    @Provides
    fun provideTokenRepository(
        authTokenLocalDataSource: AuthTokenLocalDataSource,
        authTokenByMyServerDataSource: AuthTokenByMyServerDataSource,
        authTokenByGoogleDataSource: AuthTokenByGoogleDataSource,
    ): AuthTokenRepository {
        return when (BuildConfig.FLAVOR) {
            "Fake" -> AuthTokenRepositoryFake(authTokenLocalDataSource)
            "Real" -> AuthTokenRepositoryImpl(
                authTokenLocalDataSource,
                authTokenByMyServerDataSource,
                authTokenByGoogleDataSource
            )

            else -> throw IllegalArgumentException("Unknown flavor: ${BuildConfig.FLAVOR}")
        }
    }

    @Provides
    @Singleton
    fun provideLoginInterface(retrofitProvider: RetrofitProvider): LoginApi {
        return RetrofitApiService(retrofitProvider.get(null), LoginApi::class.java).create()
    }
}