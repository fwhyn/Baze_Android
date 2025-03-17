package com.fwhyn.deandro.data.repository.auth

import com.fwhyn.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.deandro.data.local.auth.TokenLocalDataSource
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryFake @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
) : BaseRepositoryCoroutine<LoginParam?, UserToken?> {
    override suspend fun get(param: LoginParam?): UserToken? {
        var token = tokenLocalDataSource.token

        if (token == null && param != null) {
            token = when (param) {
                is LoginParam.Google -> getTokenByGoogle(param)
                is LoginParam.MyServer -> getTokenFromRemote(param)
            }
        }

        return token
    }

    override suspend fun set(param: LoginParam?, data: UserToken?) {
        tokenLocalDataSource.token = data
    }

    suspend fun getTokenByGoogle(param: LoginParam.Google): UserToken? {
        return null
    }

    suspend fun getTokenFromRemote(param: LoginParam.MyServer): UserToken? {
        var token: UserToken? = null

        if (param.username == "admin" && param.password == "admin") {
            token = UserToken(
                name = "fake",
                code = "21fdhs"
            )
            if (param.remember) tokenLocalDataSource.token = token
        }

        return token
    }
}