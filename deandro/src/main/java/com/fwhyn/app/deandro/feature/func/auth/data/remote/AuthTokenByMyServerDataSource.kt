package com.fwhyn.app.deandro.feature.func.auth.data.remote

import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.lib.baze.common.model.Exzeption
import com.fwhyn.lib.baze.common.model.Status
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenByMyServerDataSource @Inject constructor(
    private val loginApi: LoginApi,
) {
    suspend fun login(getAuthTokenRepoParam: GetAuthTokenRepoParam.MyServer): LoginApi.Response {
        return if (getAuthTokenRepoParam.isNotEmpty()) {
            try {
                val response: LoginApi.Response = loginApi.login(
                    getAuthTokenRepoParam,
                    getAuthTokenRepoParam.forceLogin.data
                ).also {
                    if (it.status_code != Status.Success.code) {
                        throw Exzeption(it.status)
                    }
                }

                response
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> throw Exzeption(
                        status = Status.Unauthorized,
                        throwable = Throwable("Invalid username or password"),
                    )

                    422 -> throw Exzeption(status = Status.BadRequest)
                    else -> throw Exzeption()
                }
            }
        } else {
            throw Exzeption(
                status = Status.BadRequest,
                throwable = Throwable("Empty username or password"),
            )
        }
    }
}