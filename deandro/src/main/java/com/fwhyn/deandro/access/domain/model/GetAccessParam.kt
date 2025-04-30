package com.fwhyn.deandro.access.domain.model

import android.app.Activity
import android.content.Intent

sealed class GetAccessParam() {

    data class GoogleDrive(
        val activity: Activity,
        val onRetrieveResult: ((Intent) -> Unit)? = null
    ) : GetAccessParam()

    data object Local : GetAccessParam()

    data object MyServer : GetAccessParam()

    data object Nothing : GetAccessParam()
}