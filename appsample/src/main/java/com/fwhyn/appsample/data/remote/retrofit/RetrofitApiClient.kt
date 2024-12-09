package com.fwhyn.appsample.data.remote.retrofit

import retrofit2.Retrofit

class RetrofitApiClient<T>(
    private val retrofit: Retrofit,
    private val cls: Class<T>,
) {

    val client: T
        get() = retrofit.create(cls)
}