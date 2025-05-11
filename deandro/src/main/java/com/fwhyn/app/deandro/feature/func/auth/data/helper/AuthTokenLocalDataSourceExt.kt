package com.fwhyn.app.deandro.feature.func.auth.data.helper

import com.fwhyn.app.deandro.feature.func.auth.data.local.AuthTokenLocalDataSource
import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.data.model.SetAuthTokenRepoParam

fun AuthTokenLocalDataSource.get(param: GetAuthTokenRepoParam.Local): AuthTokenRaw {

    return this.token ?: AuthTokenRaw.None
}

fun AuthTokenLocalDataSource.set(param: SetAuthTokenRepoParam.Local, data: AuthTokenRaw) {
    when (data) {
        is AuthTokenRaw.Dto -> this.token = data
        AuthTokenRaw.None -> this.token = null
    }
}