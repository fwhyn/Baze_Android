package com.fwhyn.app.deandro.access.domain.helper

import com.fwhyn.app.deandro.access.data.model.AccessData
import com.fwhyn.app.deandro.access.domain.model.GetAccessResult

fun AccessData.toGetAccessResult(): GetAccessResult {
    return when (this) {
        is AccessData.GoogleDrive -> GetAccessResult.GoogleDrive(this.result)
        AccessData.Nothing -> TODO()
    }
}