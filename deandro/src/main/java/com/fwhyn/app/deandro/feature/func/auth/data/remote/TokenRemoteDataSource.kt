package com.fwhyn.app.deandro.feature.func.auth.data.remote

import com.fwhyn.app.deandro.feature.func.auth.data.model.LoginParam
import com.fwhyn.app.deandro.feature.func.auth.data.model.UserToken
import com.fwhyn.lib.baze.data.model.Exzeption
import com.fwhyn.lib.baze.data.model.Status
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRemoteDataSource @Inject constructor(
    private val loginApi: LoginApi,
) {
    suspend fun getToken(loginParam: LoginParam.MyServer): UserToken? {
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