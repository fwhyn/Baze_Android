package com.fwhyn.app.deandro.feature.func.auth.domain.helper

import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw.Dto
import com.fwhyn.app.deandro.feature.func.auth.domain.model.AuthTokenModel

fun AuthTokenModel.toAuthTokenRaw(): AuthTokenRaw {
    return when (this) {
        is AuthTokenModel.Data -> Dto(
            name = this.name,
            code = this.code,
            type = this.type,
            userId = this.userId,
        )

        AuthTokenModel.None -> AuthTokenRaw.None
    }
}