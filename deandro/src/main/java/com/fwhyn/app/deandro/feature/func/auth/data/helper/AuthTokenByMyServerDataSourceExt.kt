package com.fwhyn.app.deandro.feature.func.auth.data.helper

import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.data.remote.AuthTokenByMyServerDataSource

suspend fun AuthTokenByMyServerDataSource.get(param: GetAuthTokenRepoParam.MyServer): AuthTokenRaw {
    return this.login(param).dto
}
