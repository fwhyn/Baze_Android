package com.fwhyn.app.deandro.feature.func.access.domain.model

import com.google.android.gms.auth.api.identity.AuthorizationResult

sealed class GetAccessResult() {

    data class GoogleDrive(val result: AuthorizationResult) : GetAccessResult()
    data object Nothing : GetAccessResult()
}