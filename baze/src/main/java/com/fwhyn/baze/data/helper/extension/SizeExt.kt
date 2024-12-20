package com.fwhyn.baze.data.helper.extension

import android.util.Size
import android.util.SizeF
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan

fun Size.notEqualz(value: Size): Boolean {
    return !equalz(value)
}

fun Size.equalz(value: Size): Boolean {
    return toSizeF().equalz(value.toSizeF())
}

fun Size.toSizeF(): SizeF {
    return SizeF(this.width.toFloat(), this.height.toFloat())
}

fun SizeF.notEqualz(value: SizeF): Boolean {
    return !equalz(value)
}

fun SizeF.equalz(value: SizeF): Boolean {
    return width == value.width && height == value.height
}

fun SizeF.toSize(): Size {
    return Size(this.width.roundToInt(), this.height.roundToInt())
}

fun SizeF.isLandscape(): Boolean {
    return !isPortrait()
}

fun SizeF.isPortrait(): Boolean {
    return width < height
}

/**
 * getOriginalSize formula:
 * sin = |sin(rotation)| ; cos = |cos(rotation)| ; tan = |tan(rotation)|; cot = |cot(rotation)|
 * w' = x + y
 * h' = a + b
 * w' = w*cos + h*sin
 * h' = w*sin + h*cos
 * x = w*cos <-> w = x/cos ; y = h*sin <-> h = y/sin
 * a = w*sin <-> w = a/sin ; b = h*cos <-> h = b/cos
 * x/cos = a/sin <-> a = x*sin/cos ; y/sin = b/cos <-> b = y*cos/sin
 * w' = x + y                  *cos/sin
 * h' = x*sin/cos + y*cos/sin  *1
 * w'*cos/sin = x*cos/sin + y*cos/sin
 * h'         = x*sin/cos + y*cos/sin
 * ---------------------------------- -
 * w'*cos/sin - h' = x(cos/sin - sin/cos )
 * x               = (w'*cos/sin - h')/(cos/sin - sin/cos)
 *
 * finally:
 * x = (w*cot - h')/(cot - tan)
 * y = w' - x
 * w = x/cos
 * h = y/sin
 *
 * if rotation = 45
 * x + y - a - b = 0 -> it should never reach 45, must be adjusted to slightly before or after 45
 * for example: 45 can be adjusted to 44.999
 */
fun SizeF.getOriginalSize(rotation: Float): SizeF {
    getPortraitOrLandscapeSizeOrNull(rotation)?.let {
        return it
    }

    val adjustedRot = rotation.adjustBeforeLimit45()
    val rad = Math.toRadians(adjustedRot.toDouble())
    val sin = abs(sin(rad))
    val cos = abs(cos(rad))
    val tan = abs(tan(rad))
    val cot = 1 / tan

    val x = (width * cot - height) / (cot - tan)
    val y = width - x

    val originalWidth = (x / cos).toFloat()
    val originalHeight = (y / sin).toFloat()

    return SizeF(originalWidth, originalHeight)
}

fun SizeF.getRotatedSize(rotation: Float): SizeF {
    getPortraitOrLandscapeSizeOrNull(rotation)?.let {
        return it
    }

    val adjustedRot = rotation.adjustBeforeLimit45()
    val radians = Math.toRadians(adjustedRot.toDouble())
    val newWidth: Float = (abs(width * cos(radians)) + abs(height * sin(radians))).toFloat()
    val newHeight: Float = (abs(width * sin(radians)) + abs(height * cos(radians))).toFloat()

    return SizeF(newWidth, newHeight)
}

fun SizeF.getPortraitOrLandscapeSizeOrNull(rotation: Float): SizeF? {
    if (rotation.isRotatedPortrait()) {
        return this
    }

    if (rotation.isRotatedLandScape()) {
        return swap()
    }

    return null
}

fun SizeF.swap(): SizeF {
    return SizeF(height, width)
}