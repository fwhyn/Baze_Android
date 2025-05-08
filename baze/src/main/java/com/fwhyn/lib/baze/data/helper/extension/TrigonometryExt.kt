package com.fwhyn.lib.baze.data.helper.extension

import kotlin.math.cos
import kotlin.math.sin

fun Float.isRotatedLandScape(): Boolean {
    val rad = Math.toRadians(this.toDouble())

    return sin(rad) == 1.0
}

fun Float.isRotatedPortrait(): Boolean {
    val rad = Math.toRadians(this.toDouble())

    return cos(rad) == 1.0
}

fun Float.adjustBeforeLimit45(): Float {
    var adjustedRot = this

    if (adjustedRot.isRotated45()) {
        adjustedRot -= 0.01f
    }

    return adjustedRot
}

fun Float.isRotated45(): Boolean {
    return this % 45 == 0f
}