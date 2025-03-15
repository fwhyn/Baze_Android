package com.fwhyn.deandro.data.repository.auth

import com.fwhyn.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.deandro.data.local.auth.TokenLocalDataSource
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import com.fwhyn.deandro.data.remote.auth.GoogleSignIn
import com.fwhyn.deandro.data.remote.auth.TokenRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val tokenRemoteDataSource: TokenRemoteDataSource,
    private val googleSignIn: GoogleSignIn,
) : BaseRepositoryCoroutine<LoginParam?, UserToken?> {
    override suspend fun get(param: LoginParam?): UserToken? {
        var token = tokenLocalDataSource.token

        if (token == null) {
            if (param?.loginByGoogle == true) {
                token = getTokenByGoogle(param)
            } else {
                token = getTokenFromRemote(param)
            }
        }

        return token
    }

    override suspend fun set(param: LoginParam?, data: UserToken?) {
        tokenLocalDataSource.token = data
    }

    suspend fun getTokenByGoogle(param: LoginParam?): UserToken? {

    }

    suspend fun getTokenFromRemote(param: LoginParam?): UserToken? {
        var token: UserToken? = null

        if (param != null) {
            token = tokenRemoteDataSource.getToken(param)?.also {
                if (param.remember) tokenLocalDataSource.token = it
            }
        }

        return token
    }
}