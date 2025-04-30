package com.fwhyn.deandro.data.repository.auth

import com.fwhyn.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.baze.domain.helper.Rezult
import com.fwhyn.deandro.data.local.auth.TokenLocalDataSource
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import com.fwhyn.deandro.data.remote.auth.GoogleSignIn
import com.fwhyn.deandro.data.remote.auth.TokenRemoteDataSource
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val tokenRemoteDataSource: TokenRemoteDataSource,
    private val googleSignIn: GoogleSignIn,
) : BaseRepositoryCoroutine<LoginParam, UserToken?> {
    override suspend fun get(param: LoginParam): UserToken? {

        return when (param) {
            is LoginParam.Google -> getTokenByGoogle(param)
            LoginParam.Local -> tokenLocalDataSource.token
            is LoginParam.MyServer -> getTokenFromRemote(param)
        }
    }

    override suspend fun set(param: LoginParam, data: UserToken?) {
        tokenLocalDataSource.token = data
    }

    suspend fun getTokenByGoogle(param: LoginParam.Google): UserToken? {
        val signInResult = googleSignIn.signInAndGetResult(
            activity = param.activity,
        )

        return when (signInResult) {
            is Rezult.Failure<GoogleSignIn.ErrorType> -> null
            is Rezult.Success<GoogleIdTokenCredential> -> UserToken(name = "fake", code = "21fdhs")
        }
    }

    suspend fun getTokenFromRemote(param: LoginParam.MyServer): UserToken? {
        var token: UserToken?

        token = tokenRemoteDataSource.getToken(param)?.also {
            if (param.remember) tokenLocalDataSource.token = it
        }

        return token
    }
}