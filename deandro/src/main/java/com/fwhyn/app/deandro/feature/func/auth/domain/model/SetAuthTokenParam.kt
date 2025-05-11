package com.fwhyn.app.deandro.feature.func.auth.domain.model

sealed class SetAuthTokenParam {
    data class Local(val authTokenModel: AuthTokenModel) : SetAuthTokenParam()
    data object Unknown : SetAuthTokenParam()
}