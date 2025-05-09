package com.fwhyn.app.deandro.common.network.api

import retrofit2.Retrofit

class RetrofitApiClient<T>(
    private val retrofit: Retrofit,
    private val cls: Class<T>,
) {

    val client: T
        get() = retrofit.create(cls)
}