package com.fwhyn.app.deandro.feature.func.access.domain.helper

import com.fwhyn.app.deandro.feature.func.access.data.model.AccessParam
import com.fwhyn.app.deandro.feature.func.access.domain.model.GetAccessParam

fun GetAccessParam.toAccessParam(): AccessParam {
    return when (this) {
        is GetAccessParam.GoogleDrive -> AccessParam.GoogleDrive(this.activity, this.onRetrieveResult)
        GetAccessParam.Local -> TODO()
        GetAccessParam.MyServer -> TODO()
        GetAccessParam.Nothing -> TODO()
    }
}