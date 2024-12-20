package com.fwhyn.deandro.data.repository.auth

import com.fwhyn.data.repository.BaseRepositoryCoroutine
import com.fwhyn.deandro.data.local.auth.TokenLocalDataSource
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import com.fwhyn.deandro.data.remote.auth.TokenRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val tokenRemoteDataSource: TokenRemoteDataSource,
) : BaseRepositoryCoroutine<LoginParam?, UserToken?> {
    override suspend fun get(param: LoginParam?): UserToken? {
        var token = tokenLocalDataSource.token
        if (token == null && param != null) {
            token = tokenRemoteDataSource.getToken(param)?.also {
                if (param.remember) tokenLocalDataSource.token = it
            }
        }

        return token
    }

    override suspend fun set(param: LoginParam?, data: UserToken?) {
        tokenLocalDataSource.token = data
    }
}