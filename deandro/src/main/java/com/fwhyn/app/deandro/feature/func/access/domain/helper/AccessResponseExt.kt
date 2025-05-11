package com.fwhyn.app.deandro.feature.func.access.domain.helper

import com.fwhyn.app.deandro.feature.func.access.data.model.AccessResultRaw
import com.fwhyn.app.deandro.feature.func.access.domain.model.AccessResult

fun AccessResultRaw.toGetAccessResult(): AccessResult {
    return when (this) {
        is AccessResultRaw.GoogleDrive -> AccessResult.GoogleDrive(this.result)
        AccessResultRaw.None -> AccessResult.None
    }
}