package com.fwhyn.lib.baze.retrofit.api

import android.util.Log
import com.fwhyn.lib.baze.common.helper.extension.getDebugTag
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiRequestInterceptor(
    private val ongGetKey: () -> String,
) : Interceptor {

    private val debugTag = ApiRequestInterceptor::class.java.getDebugTag()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val token = ongGetKey()
        val bearer = "Bearer $token"

        Log.d(debugTag, bearer)

        val interceptedRequest: Request = originalRequest
            .newBuilder()
            .addHeader("Authorization", bearer)
            .build()

        val response = chain.proceed(interceptedRequest)

        return response
    }
}