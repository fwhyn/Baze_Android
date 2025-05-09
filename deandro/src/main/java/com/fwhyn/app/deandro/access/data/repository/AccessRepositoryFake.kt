package com.fwhyn.app.deandro.access.data.repository

import com.fwhyn.app.deandro.access.data.model.AccessData
import com.fwhyn.app.deandro.access.data.model.AccessParam
import com.google.android.gms.auth.api.identity.AuthorizationResult

class AccessRepositoryFake : AccessRepositoryInterface {
    override suspend fun get(param: AccessParam): AccessData {
        return when (param) {
            is AccessParam.GoogleDrive -> AccessData.GoogleDrive(
                AuthorizationResult(
                    null,
                    null,
                    null,
                    listOf(""),
                    null,
                    null
                )
            )

            AccessParam.Nothing -> TODO()
        }
    }

    override suspend fun set(
        param: AccessParam,
        data: AccessData
    ) {
        TODO("Not yet implemented")
    }
}