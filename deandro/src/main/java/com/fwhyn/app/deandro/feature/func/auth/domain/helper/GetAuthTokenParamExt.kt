package com.fwhyn.app.deandro.feature.func.auth.domain.helper

import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.domain.model.GetAuthTokenParam

fun GetAuthTokenParam.toGetAuthTokenRepoParam(): GetAuthTokenRepoParam {
    return when (this) {
        is GetAuthTokenParam.Local -> GetAuthTokenRepoParam.Local
        is GetAuthTokenParam.MyServer -> GetAuthTokenRepoParam.MyServer(
            username = username,
            password = password,
        ).also { it.remember = this.remember }

        is GetAuthTokenParam.Google -> GetAuthTokenRepoParam.Google(
            activity = activity,
        )
    }
}