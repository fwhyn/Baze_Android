package com.fwhyn.app.deandro.feature.func.auth.data.model

sealed class SetAuthTokenRepoParam() {

    data object Local : SetAuthTokenRepoParam()

    data object Unknown : SetAuthTokenRepoParam()
}