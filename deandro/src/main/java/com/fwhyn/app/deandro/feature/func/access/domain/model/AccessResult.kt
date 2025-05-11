package com.fwhyn.app.deandro.feature.func.access.domain.model

import com.google.android.gms.auth.api.identity.AuthorizationResult

sealed class AccessResult() {

    data class GoogleDrive(val result: AuthorizationResult) : AccessResult()
    data object None : AccessResult()
}