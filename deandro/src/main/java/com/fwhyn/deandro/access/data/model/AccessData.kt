package com.fwhyn.deandro.access.data.model

import com.google.android.gms.auth.api.identity.AuthorizationResult

sealed class AccessData {

    data class GoogleDrive(val result: AuthorizationResult) : AccessData()

    data object Nothing : AccessData()
}