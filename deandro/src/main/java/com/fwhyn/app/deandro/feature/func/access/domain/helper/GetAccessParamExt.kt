package com.fwhyn.app.deandro.feature.func.access.domain.helper

import com.fwhyn.app.deandro.feature.func.access.data.model.GetAccessRepoParam
import com.fwhyn.app.deandro.feature.func.access.domain.model.GetAccessParam

fun GetAccessParam.toAccessParam(): GetAccessRepoParam {
    return when (this) {
        is GetAccessParam.GoogleDrive -> GetAccessRepoParam.GoogleDrive(this.activity, this.onRetrieveResult)
        GetAccessParam.Local -> TODO()
        GetAccessParam.MyServer -> TODO()
        GetAccessParam.None -> TODO()
    }
}