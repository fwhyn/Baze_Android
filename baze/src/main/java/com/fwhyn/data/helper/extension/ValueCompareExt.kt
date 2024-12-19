package com.fwhyn.data.helper.extension

import kotlin.math.absoluteValue

const val DELTA_F = 0.001f
const val DELTA = DELTA_F.toDouble()

fun Float.notEqualz(value: Float, delta: Float = DELTA_F): Boolean {
    return !value.equalz(delta)
}

fun Float.equalz(value: Float, delta: Float = DELTA_F): Boolean {
    return this.toDouble().equalz(value.toDouble(), delta.toDouble())
}

fun Double.notEqualz(value: Double, delta: Double = DELTA): Boolean {
    return !value.equalz(delta)
}

fun Double.equalz(value: Double, delta: Double = DELTA): Boolean {
    val diff = (this - value).absoluteValue
    return diff <= delta
}