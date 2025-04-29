package com.fwhyn.baze.data.helper.extension

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import androidx.navigation.NavOptionsBuilder
import com.fwhyn.baze.data.helper.Constant.TAG_BAZE_DEBUG
import java.io.File
import kotlin.math.pow
import kotlin.math.roundToInt

class Extension

fun <T> Class<T>.getDebugTag(): String {
    return this.simpleName + TAG_BAZE_DEBUG
}

fun <T> Class<T>.getTag(): String {
    return this.simpleName
}

// ----------------------------------------------------------------
fun NavOptionsBuilder.removeFromBackStack(route: String) {
    popUpTo(route) {
        inclusive = true
    }
}

// ----------------------------------------------------------------
// https://stackoverflow.com/questions/58102486/opencv-utils-mattobitmap-assertion-failed-androidbitmap-lockpixelsenv-bitmap
// https://sudarnimalan.blogspot.com/2011/09/android-convert-immutable-bitmap-into.html
fun Bitmap.copyBmp(isMutable: Boolean = true): Bitmap {
    return copy(Bitmap.Config.ARGB_8888, isMutable)
}

// ----------------------------------------------------------------
fun RectF.leftPlusRight(): Float {
    return left + right
}

fun RectF.topPlusBottom(): Float {
    return top + bottom
}

// ----------------------------------------------------------------
fun String.toUri(): Uri {
    return Uri.fromFile(File(this))
}

// ----------------------------------------------------------------
fun Float.round(decimals: Int = 0): Float {
    return this.toDouble().round(decimals).toFloat()
}

fun Double.round(decimals: Int = 0): Double {
    val multiplier = 10.0.pow(decimals)
    return (this * multiplier).roundToInt() / multiplier
}