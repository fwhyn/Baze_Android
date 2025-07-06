package com.fwhyn.app.deandro.feature.func.access.data.helper

import com.fwhyn.app.deandro.feature.func.access.data.model.AccessResultRaw
import com.fwhyn.app.deandro.feature.func.access.data.model.GetAccessRepoParam
import com.fwhyn.app.deandro.feature.func.access.data.remote.GoogleDriveAccess
import com.fwhyn.lib.baze.common.helper.extension.continueIfActive
import com.google.android.gms.auth.api.identity.AuthorizationResult
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun GoogleDriveAccess.get(param: GetAccessRepoParam.GoogleDrive): AccessResultRaw {
    return suspendCancellableCoroutine { continuation ->
        this.getGDriveAccess(
            activity = param.activity,
            onSuccess = object : GoogleDriveAccess.OnSuccessCallback {
                override fun onSuccess(result: AuthorizationResult) {
                    continuation.continueIfActive(AccessResultRaw.GoogleDrive(result))
                }
            },
            onFailed = object : GoogleDriveAccess.OnFailedCallback {
                override fun onFailed(errorType: GoogleDriveAccess.ErrorType) {
                    continuation.continueIfActive(AccessResultRaw.None)
                }
            }
        )

        param.onRetrieveResult = { intent ->
            this.extractResult(param.activity, intent)
        }
    }
}