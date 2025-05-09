package com.fwhyn.app.deandro.data.remote.retrofit

import android.util.Log
import com.fwhyn.app.deandro.data.local.auth.TokenLocalDataSource
import com.fwhyn.lib.baze.data.helper.extension.getDebugTag
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitInterceptor @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
) : Interceptor {

    private val debugTag = RetrofitInterceptor::class.java.getDebugTag()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val token = tokenLocalDataSource.token
        val tokenCode = "Bearer " + token?.code
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