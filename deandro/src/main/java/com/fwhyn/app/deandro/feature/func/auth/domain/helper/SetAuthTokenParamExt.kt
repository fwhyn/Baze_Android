package com.fwhyn.app.deandro.feature.func.auth.domain.helper

import com.fwhyn.app.deandro.feature.func.auth.data.model.SetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.domain.model.SetAuthTokenParam

fun SetAuthTokenParam.toSetAuthTokenRepoParam(): SetAuthTokenRepoParam {
    return when (this) {
        is SetAuthTokenParam.Local -> SetAuthTokenRepoParam.Local
        is SetAuthTokenParam.Unknown -> SetAuthTokenRepoParam.Unknown
    }
}