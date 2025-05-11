package com.fwhyn.app.deandro.feature.func.access.data.model

import android.app.Activity
import android.content.Intent

sealed class GetAccessRepoParam {

    data class GoogleDrive(
        val activity: Activity,
        var onRetrieveResult: ((Intent) -> Unit)? = null
    ) : GetAccessRepoParam()

    data object None : GetAccessRepoParam()
}