package com.fwhyn.app.deandro.common.network.api

import android.util.Log
import com.fwhyn.lib.baze.common.data.helper.extension.getDebugTag
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiRequestInterceptor(
    private val ongGetToken: () -> String,
) : Interceptor {

    private val debugTag = ApiRequestInterceptor::class.java.getDebugTag()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val token = ongGetToken()
        val tokenCode = "Bearer $token"
        Log.d(debugTag, tokenCode)

        val interceptedRequest: Request = originalRequest
            .newBuilder()
            .header("Accept", "application/json")
            .header("Authorization", tokenCode)
            .method(originalRequest.method, originalRequest.body)
            .build()

        val response = chain.proceed(interceptedRequest)

        return if (response.code == 204 || response.code == 205) {
            response.newBuilder().code(200).build()
        } else {
            response
        }
    }
}