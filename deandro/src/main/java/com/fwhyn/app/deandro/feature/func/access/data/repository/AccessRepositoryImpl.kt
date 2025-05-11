package com.fwhyn.app.deandro.feature.func.access.data.repository

import com.fwhyn.app.deandro.feature.func.access.data.helper.get
import com.fwhyn.app.deandro.feature.func.access.data.model.AccessResultRaw
import com.fwhyn.app.deandro.feature.func.access.data.model.GetAccessRepoParam
import com.fwhyn.app.deandro.feature.func.access.data.remote.GoogleDriveAccess

class AccessRepositoryImpl(
    private val googleDriveAccess: GoogleDriveAccess
) : AccessRepository {
    override suspend fun get(param: GetAccessRepoParam): AccessResultRaw {
        return when (param) {
            is GetAccessRepoParam.GoogleDrive -> googleDriveAccess.get(param)
            GetAccessRepoParam.None -> TODO()
        }
    }
}