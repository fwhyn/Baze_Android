package com.fwhyn.appsample.di

import com.fwhyn.appsample.data.local.auth.TokenLocalDataSource
import com.fwhyn.appsample.data.model.auth.LoginParam
import com.fwhyn.appsample.data.model.auth.UserToken
import com.fwhyn.appsample.data.remote.auth.LoginApi
import com.fwhyn.appsample.data.remote.auth.TokenRemoteDataSource
import com.fwhyn.data.remote.retrofit.RetrofitApiClient
import com.fwhyn.data.repository.BaseRepositoryCoroutine
import com.fwhyn.data.repository.auth.TokenRepositoryFake
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
    fun provideTokenRepository(
        tokenLocalDataSource: TokenLocalDataSource,
        tokenRemoteDataSource: TokenRemoteDataSource,
    ): BaseRepositoryCoroutine<LoginParam?, UserToken?> {
        return TokenRepositoryFake(tokenLocalDataSource, tokenRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideLoginInterface(retrofit: Retrofit): LoginApi {
        return RetrofitApiClient(retrofit, LoginApi::class.java).client
    }
}