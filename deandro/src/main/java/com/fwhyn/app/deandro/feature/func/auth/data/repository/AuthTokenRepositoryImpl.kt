package com.fwhyn.app.deandro.feature.func.auth.data.repository

import com.fwhyn.app.deandro.feature.func.auth.data.helper.get
import com.fwhyn.app.deandro.feature.func.auth.data.helper.set
import com.fwhyn.app.deandro.feature.func.auth.data.local.AuthTokenLocalDataSource
import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.data.model.SetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.data.remote.AuthTokenByGoogleDataSource
import com.fwhyn.app.deandro.feature.func.auth.data.remote.AuthTokenByMyServerDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenRepositoryImpl @Inject constructor(
    private val authTokenLocalDataSource: AuthTokenLocalDataSource,
    private val authTokenByMyServerDataSource: AuthTokenByMyServerDataSource,
    private val authTokenByGoogleDataSource: AuthTokenByGoogleDataSource,
) : AuthTokenRepository {
    override suspend fun get(param: GetAuthTokenRepoParam): AuthTokenRaw {

        val response: AuthTokenRaw = when (param) {
            is GetAuthTokenRepoParam.Google -> authTokenByGoogleDataSource.get(param)
            is GetAuthTokenRepoParam.Local -> authTokenLocalDataSource.get(param)
            is GetAuthTokenRepoParam.MyServer -> authTokenByMyServerDataSource.get(param)
        }

        val isRemember = when (param) {
            is GetAuthTokenRepoParam.MyServer -> param.remember
            is GetAuthTokenRepoParam.Google -> false
            GetAuthTokenRepoParam.Local -> false
        }

        if (param !is GetAuthTokenRepoParam.Local && isRemember) {
            set(SetAuthTokenRepoParam.Local, response)
        }

        return response
    }

    override suspend fun set(param: SetAuthTokenRepoParam, data: AuthTokenRaw) {
        when (param) {
            is SetAuthTokenRepoParam.Local -> authTokenLocalDataSource.set(param, data)
            SetAuthTokenRepoParam.Unknown -> TODO()
        }
    }
}