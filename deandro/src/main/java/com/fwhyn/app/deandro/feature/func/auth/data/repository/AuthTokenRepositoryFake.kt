package com.fwhyn.app.deandro.feature.func.auth.data.repository

import com.fwhyn.app.deandro.feature.func.auth.data.local.AuthTokenLocalDataSource
import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.data.model.SetAuthTokenRepoParam
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenRepositoryFake @Inject constructor(
    private val authTokenLocalDataSource: AuthTokenLocalDataSource,
) : AuthTokenRepository {
    override suspend fun get(param: GetAuthTokenRepoParam): AuthTokenRaw {

        return when (param) {
            is GetAuthTokenRepoParam.Google -> getTokenByGoogle(param)
            GetAuthTokenRepoParam.Local -> authTokenLocalDataSource.token ?: AuthTokenRaw.None
            is GetAuthTokenRepoParam.MyServer -> getTokenFromRemote(param)
        }
    }

    override suspend fun set(param: SetAuthTokenRepoParam, data: AuthTokenRaw) {
        when (param) {
            is SetAuthTokenRepoParam.Local -> setLocal(data)
            SetAuthTokenRepoParam.Unknown -> TODO()
        }
    }

    fun setLocal(data: AuthTokenRaw) {
        when (data) {
            is AuthTokenRaw.Dto -> authTokenLocalDataSource.token = data
            AuthTokenRaw.None -> authTokenLocalDataSource.token = null
        }
    }

    suspend fun getTokenByGoogle(param: GetAuthTokenRepoParam.Google): AuthTokenRaw {
        return AuthTokenRaw.Dto(
            name = "fake",
            code = "21fdhs"
        )
    }

    suspend fun getTokenFromRemote(param: GetAuthTokenRepoParam.MyServer): AuthTokenRaw {
        var token: AuthTokenRaw = AuthTokenRaw.None

        if (param.username == "admin" && param.password == "admin") {
            token = AuthTokenRaw.Dto(
                name = "fake",
                code = "21fdhs"
            )
            if (param.remember) authTokenLocalDataSource.token = token
        }

        return token
    }
}