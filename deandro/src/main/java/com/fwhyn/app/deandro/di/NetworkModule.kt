package com.fwhyn.app.deandro.di

import com.fwhyn.app.deandro.BuildConfig
import com.fwhyn.app.deandro.data.local.auth.TokenLocalDataSource
import com.fwhyn.app.deandro.data.remote.retrofit.RetrofitInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpBuilder: OkHttpClient.Builder): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpBuilder.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(
        logger: HttpLoggingInterceptor,
        headerInterceptor: RetrofitInterceptor,
    ): OkHttpClient.Builder {
        return OkHttpClient
            .Builder()
            .addNetworkInterceptor(headerInterceptor)
            .addInterceptor(logger)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideRetrofitInterceptor(tokenLocalDataSource: TokenLocalDataSource): RetrofitInterceptor {
        return RetrofitInterceptor(tokenLocalDataSource)
    }
}