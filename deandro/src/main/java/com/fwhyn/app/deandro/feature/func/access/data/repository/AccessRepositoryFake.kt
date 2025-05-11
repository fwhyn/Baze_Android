package com.fwhyn.app.deandro.feature.func.access.data.repository

import com.fwhyn.app.deandro.feature.func.access.data.model.AccessResultRaw
import com.fwhyn.app.deandro.feature.func.access.data.model.GetAccessRepoParam
import com.google.android.gms.auth.api.identity.AuthorizationResult

class AccessRepositoryFake : AccessRepository {
    override suspend fun get(param: GetAccessRepoParam): AccessResultRaw {
        return when (param) {
            is GetAccessRepoParam.GoogleDrive -> AccessResultRaw.GoogleDrive(
                AuthorizationResult(
                    null,
                    null,
                    null,
                    listOf(""),
                    null,
                    null
                )
            )

            GetAccessRepoParam.None -> TODO()
        }
    }
}