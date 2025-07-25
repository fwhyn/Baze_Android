package com.fwhyn.app.deandro.feature.func.auth.data.helper

import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.data.remote.AuthTokenByGoogleDataSource
import com.fwhyn.lib.baze.common.helper.Rezult
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

suspend fun AuthTokenByGoogleDataSource.get(param: GetAuthTokenRepoParam.Google): AuthTokenRaw {
    val signInResult = this.signInAndGetResult(
        activity = param.activity,
    )

    return when (signInResult) {
        is Rezult.Failure<AuthTokenByGoogleDataSource.ErrorType> -> AuthTokenRaw.None
        is Rezult.Success<GoogleIdTokenCredential> -> AuthTokenRaw.Dto(name = "fake", code = "21fdhs")
    }
}