package com.fwhyn.app.deandro.feature.func.auth.di

import com.fwhyn.app.deandro.BuildConfig
import com.fwhyn.app.deandro.common.network.api.RetrofitApiClient
import com.fwhyn.app.deandro.feature.func.auth.data.local.TokenLocalDataSource
import com.fwhyn.app.deandro.feature.func.auth.data.model.LoginParam
import com.fwhyn.app.deandro.feature.func.auth.data.model.UserToken
import com.fwhyn.app.deandro.feature.func.auth.data.remote.GoogleSignIn
import com.fwhyn.app.deandro.feature.func.auth.data.remote.LoginApi
import com.fwhyn.app.deandro.feature.func.auth.data.remote.TokenRemoteDataSource
import com.fwhyn.app.deandro.feature.func.auth.data.repository.TokenRepository
import com.fwhyn.app.deandro.feature.func.auth.data.repository.TokenRepositoryFake
import com.fwhyn.app.deandro.feature.func.auth.domain.usecase.GetTokenUseCase
import com.fwhyn.app.deandro.feature.func.auth.domain.usecase.SetTokenUseCase
import com.fwhyn.lib.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.lib.baze.domain.usecase.BaseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AuthModule {

    @Provides
    fun provideGetTokenUseCase(
        tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
    ): BaseUseCase<LoginParam, UserToken?> {
        return GetTokenUseCase(tokenRepository)
    }

    @Provides
    fun provideSetTokenUseCase(
        tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
    ): BaseUseCase<UserToken?, Unit> {
        return SetTokenUseCase(tokenRepository)
    }

    @Provides
    fun provideTokenRepository(
        tokenLocalDataSource: TokenLocalDataSource,
        tokenRemoteDataSource: TokenRemoteDataSource,
        googleSignIn: GoogleSignIn,
    ): BaseRepositoryCoroutine<LoginParam, UserToken?> {
        return when (BuildConfig.FLAVOR) {
            "Fake" -> TokenRepositoryFake(tokenLocalDataSource)
            "Real" -> TokenRepository(tokenLocalDataSource, tokenRemoteDataSource, googleSignIn)
            else -> throw IllegalArgumentException("Unknown flavor: ${BuildConfig.FLAVOR}")
        }
    }

    @Provides
    @Singleton
    fun provideLoginInterface(retrofit: Retrofit): LoginApi {
        return RetrofitApiClient(retrofit, LoginApi::class.java).client
    }
}