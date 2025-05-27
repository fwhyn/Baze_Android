package com.fwhyn.lib.baze.retrofit.api

import retrofit2.Retrofit

class RetrofitApiService<T>(
    private val retrofit: Retrofit,
    private val cls: Class<T>,
) {

    fun create(): T = retrofit.create(cls)
}