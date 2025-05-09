package com.fwhyn.app.deandro.feature.func.access.domain.helper

import com.fwhyn.app.deandro.feature.func.access.data.model.AccessData
import com.fwhyn.app.deandro.feature.func.access.domain.model.GetAccessResult

fun AccessData.toGetAccessResult(): GetAccessResult {
    return when (this) {
        is AccessData.GoogleDrive -> GetAccessResult.GoogleDrive(this.result)
        AccessData.Nothing -> TODO()
    }
}