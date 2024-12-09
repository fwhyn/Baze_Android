package com.fwhyn.appsample.data.repository.auth

import com.fwhyn.appsample.data.local.auth.TokenLocalDataSource
import com.fwhyn.appsample.data.model.auth.LoginParam
import com.fwhyn.appsample.data.model.auth.UserToken
import com.fwhyn.appsample.data.remote.auth.TokenRemoteDataSource
import com.fwhyn.appsample.data.repository.BaseRepositoryCoroutine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryFake @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val tokenRemoteDataSource: TokenRemoteDataSource,
) : BaseRepositoryCoroutine<LoginParam?, UserToken?> {
    override suspend fun get(param: LoginParam?): UserToken? {
        var token = tokenLocalDataSource.token
        if (token == null && param?.isNotEmpty() == true) {
            token = UserToken(
                name = "fake",
                code = "21fdhs"
            )
            if (param.remember) tokenLocalDataSource.token = token
        }

        return token
    }

    override suspend fun set(param: LoginParam?, data: UserToken?) {
        tokenLocalDataSource.token = data
    }
}