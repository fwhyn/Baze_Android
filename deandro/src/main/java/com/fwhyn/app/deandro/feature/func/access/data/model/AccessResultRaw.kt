package com.fwhyn.app.deandro.feature.func.access.data.model

import com.google.android.gms.auth.api.identity.AuthorizationResult

sealed class AccessResultRaw {

    data class GoogleDrive(val result: AuthorizationResult) : AccessResultRaw()

    data object None : AccessResultRaw()
}