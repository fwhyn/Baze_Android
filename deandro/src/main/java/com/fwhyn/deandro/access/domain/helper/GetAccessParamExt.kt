package com.fwhyn.deandro.access.domain.helper

import com.fwhyn.deandro.access.data.model.AccessParam
import com.fwhyn.deandro.access.domain.model.GetAccessParam

fun GetAccessParam.toAccessParam(): AccessParam {
    return when (this) {
        is GetAccessParam.GoogleDrive -> AccessParam.GoogleDrive(this.activity, this.onRetrieveResult)
        GetAccessParam.Local -> TODO()
        GetAccessParam.MyServer -> TODO()
        GetAccessParam.Nothing -> TODO()
    }
}