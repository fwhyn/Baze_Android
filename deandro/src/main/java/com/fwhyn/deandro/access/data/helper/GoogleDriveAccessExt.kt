package com.fwhyn.deandro.access.data.helper

import com.fwhyn.deandro.access.data.model.AccessData
import com.fwhyn.deandro.access.data.model.AccessParam
import com.fwhyn.deandro.access.data.remote.GoogleDriveAccess
import com.fwhyn.lib.baze.data.helper.extension.continueIfActive
import com.google.android.gms.auth.api.identity.AuthorizationResult
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun GoogleDriveAccess.get(param: AccessParam.GoogleDrive): AccessData {
    return suspendCancellableCoroutine { continuation ->
        this.getGDriveAccess(
            activity = param.activity,
            onSuccess = object : GoogleDriveAccess.OnSuccessCallback {
                override fun onSuccess(result: AuthorizationResult) {
                    continuation.continueIfActive(AccessData.GoogleDrive(result))
                }
            },
            onFailed = object : GoogleDriveAccess.OnFailedCallback {
                override fun onFailed(errorType: GoogleDriveAccess.ErrorType) {
                    continuation.continueIfActive(AccessData.Nothing)
                }
            }
        )

        param.onRetrieveResult = { intent ->
            this.extractResult(param.activity, intent)
        }
    }
}