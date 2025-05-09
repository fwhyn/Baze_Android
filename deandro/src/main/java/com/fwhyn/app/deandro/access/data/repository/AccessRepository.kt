package com.fwhyn.app.deandro.access.data.repository

import com.fwhyn.app.deandro.access.data.helper.get
import com.fwhyn.app.deandro.access.data.model.AccessData
import com.fwhyn.app.deandro.access.data.model.AccessParam
import com.fwhyn.app.deandro.access.data.remote.GoogleDriveAccess

class AccessRepository(
    private val googleDriveAccess: GoogleDriveAccess
) : AccessRepositoryInterface {
    override suspend fun get(param: AccessParam): AccessData {
        return when (param) {
            is AccessParam.GoogleDrive -> googleDriveAccess.get(param)
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