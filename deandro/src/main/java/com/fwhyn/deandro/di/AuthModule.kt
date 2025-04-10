package com.fwhyn.deandro.di

import com.fwhyn.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.baze.domain.usecase.BaseUseCase
import com.fwhyn.deandro.BuildConfig
import com.fwhyn.deandro.data.local.auth.TokenLocalDataSource
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import com.fwhyn.deandro.data.remote.auth.GoogleSignIn
import com.fwhyn.deandro.data.remote.auth.LoginApi
import com.fwhyn.deandro.data.remote.auth.TokenRemoteDataSource
import com.fwhyn.deandro.data.remote.retrofit.RetrofitApiClient
import com.fwhyn.deandro.data.repository.auth.TokenRepository
import com.fwhyn.deandro.data.repository.auth.TokenRepositoryFake
import com.fwhyn.deandro.domain.usecase.auth.GetTokenUseCase
import com.fwhyn.deandro.domain.usecase.auth.SetTokenUseCase
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