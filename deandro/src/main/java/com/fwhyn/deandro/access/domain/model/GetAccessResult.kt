package com.fwhyn.deandro.access.domain.model

import com.google.android.gms.auth.api.identity.AuthorizationResult

sealed class GetAccessResult() {

    data class GoogleDrive(val result: AuthorizationResult) : GetAccessResult()
    data object Nothing : GetAccessResult()
}