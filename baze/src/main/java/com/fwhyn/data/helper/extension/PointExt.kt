package com.fwhyn.data.helper.extension

import android.graphics.PointF

fun PointF.notEqualz(value: PointF): Boolean {
    return !equalz(value)
}

fun PointF.equalz(value: PointF): Boolean {
    return x == value.x && y == value.y
}

fun PointF.copy(x: Float = this.x, y: Float = this.y): PointF {
    return PointF(x, y)
}