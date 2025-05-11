package com.fwhyn.app.deandro.feature.func.auth.domain.model

import android.app.Activity

sealed class GetAuthTokenParam {

    data object Local : GetAuthTokenParam()

    data class MyServer(
        val username: String,
        val password: String,
        val remember: Boolean,
    ) : GetAuthTokenParam()

    data class Google(
        val activity: Activity,
    ) : GetAuthTokenParam()
}