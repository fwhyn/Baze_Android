package com.fwhyn.deandro.data.repository.auth

import com.fwhyn.baze.data.helper.extension.continueIfActive
import com.fwhyn.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.baze.domain.helper.Rezult
import com.fwhyn.deandro.data.local.auth.TokenLocalDataSource
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import com.fwhyn.deandro.data.remote.auth.GoogleDriveAccess
import com.fwhyn.deandro.data.remote.auth.GoogleDriveAccess.OnFailedCallback
import com.fwhyn.deandro.data.remote.auth.GoogleDriveAccess.OnSuccessCallback
import com.fwhyn.deandro.data.remote.auth.GoogleSignIn
import com.fwhyn.deandro.data.remote.auth.GoogleSignInWaitingType
import com.fwhyn.deandro.data.remote.auth.TokenRemoteDataSource
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val tokenRemoteDataSource: TokenRemoteDataSource,
    private val googleSignIn: GoogleSignInWaitingType,
    private val googleDriveAccess: GoogleDriveAccess,
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
            is Rezult.Success<GoogleIdTokenCredential> -> getTokenByGoogleDriveAccess(param)
        }
    }

    suspend fun getTokenByGoogleDriveAccess(param: LoginParam.Google): UserToken? {

        return suspendCancellableCoroutine { continuation ->
            googleDriveAccess.getGDriveAccess(
                activity = param.activity,
                onSuccess = object : OnSuccessCallback {
                    override fun onSuccess(result: AuthorizationResult) {
                        continuation.continueIfActive(UserToken(name = "fake", code = "21fdhs"))
                    }
                },
                onFailed = object : OnFailedCallback {
                    override fun onFailed(errorType: GoogleDriveAccess.ErrorType) {
                        continuation.continueIfActive(null)
                    }

                }
            )
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