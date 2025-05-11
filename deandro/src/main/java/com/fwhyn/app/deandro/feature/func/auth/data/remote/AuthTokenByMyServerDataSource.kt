package com.fwhyn.app.deandro.feature.func.auth.data.remote

import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.lib.baze.data.model.Exzeption
import com.fwhyn.lib.baze.data.model.Status
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenByMyServerDataSource @Inject constructor(
    private val loginApi: LoginApi,
) {
    suspend fun login(getAuthTokenRepoParam: GetAuthTokenRepoParam.MyServer): LoginApi.Response {
        return if (getAuthTokenRepoParam.isNotEmpty()) {
            val response: LoginApi.Response = loginApi.login(getAuthTokenRepoParam).also {
                if (it.status_code != Status.Success.code) {
                    throw Exzeption(it.status)
                }
            }

            response
        } else {
            throw Exzeption(
                status = Status.BadRequest,
                throwable = Throwable("Empty username or password"),
            )
        }
    }
}