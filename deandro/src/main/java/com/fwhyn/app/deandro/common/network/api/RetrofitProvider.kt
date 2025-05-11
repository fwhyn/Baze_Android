package com.fwhyn.app.deandro.common.network.api

import com.fwhyn.app.deandro.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitProvider @Inject constructor() {

    fun get(ongGetToken: (() -> String)?): Retrofit {

        val retrofitBuilder = Retrofit
            .Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())

        if (ongGetToken != null) {
            val okHttpClient = getOkHttpClient(ongGetToken)
            retrofitBuilder.client(okHttpClient)
        }

        return retrofitBuilder.build()
    }

    private fun getOkHttpClient(ongGetToken: () -> String): OkHttpClient {
        val apiRequestInterceptor = ApiRequestInterceptor(ongGetToken)
        val okHttpClient: OkHttpClient.Builder = OkHttpClient
            .Builder()
            .addNetworkInterceptor(apiRequestInterceptor)

        if (BuildConfig.DEBUG) {
            val logger: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClient.addInterceptor(logger)
        }

        return okHttpClient.build()
    }
}