package com.fwhyn.app.deandro.feature.func.auth.domain.helper

import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.domain.model.AuthTokenModel

fun AuthTokenRaw.toAuthTokenModel(): AuthTokenModel {
    return when (this) {
        is AuthTokenRaw.Dto -> AuthTokenModel.Data(
            name = this.name,
            code = this.code,
            type = this.type,
            userId = this.userId
        )

        AuthTokenRaw.None -> AuthTokenModel.None
    }
}