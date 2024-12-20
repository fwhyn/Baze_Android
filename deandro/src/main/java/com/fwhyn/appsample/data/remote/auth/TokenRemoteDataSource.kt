package com.fwhyn.appsample.data.remote.auth

import com.fwhyn.appsample.data.model.auth.LoginParam
import com.fwhyn.appsample.data.model.auth.UserToken
import com.fwhyn.data.model.Exzeption
import com.fwhyn.data.model.Status
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRemoteDataSource @Inject constructor(
    private val loginApi: LoginApi,
) {
    suspend fun getToken(loginParam: LoginParam): UserToken? {
        return if (loginParam.isNotEmpty()) {
            val response = loginApi.login(loginParam).also {
                if (it.status_code != Status.Success.code) {
                    throw Exzeption(it.status)
                }
            }

            response.userToken
        } else {
            null
        }
    }
}