package com.fwhyn.data.helper

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.RectF
import android.net.Uri
import android.util.Size
import android.util.SizeF
import androidx.annotation.DrawableRes
import com.fwhyn.data.helper.Constant.TAG_BAZE_TEST
import com.google.gson.GsonBuilder
import java.io.File
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan

class Extension

fun <T> Class<T>.getTestTag(): String {
    return this.simpleName + TAG_BAZE_TEST
}

fun <T> Class<T>.getTag(): String {
    return this.simpleName
}

// ----------------------------------------------------------------
inline fun <reified T> SharedPreferences.get(key: String): T? {
    val value = this.getString(key, null)

    return GsonBuilder().create().fromJson(value, T::class.java)
}

fun <T> SharedPreferences.put(key: String, data: T) {
    val jsonString = GsonBuilder().create().toJson(data)

    this.edit().putString(key, jsonString).apply()
}

fun SharedPreferences.delete(key: String) {
    this.edit().remove(key).apply()
}

// ----------------------------------------------------------------
fun Context.getBitmap(@DrawableRes id: Int): Bitmap? {
    return try {
        val option = BitmapFactory.Options()
        option.inPreferredConfig = Bitmap.Config.ARGB_8888

        BitmapFactory.decodeResource(
            resources,
            id,
            option
        )
    } catch (e: Exception) {
        null
    }
}

// ----------------------------------------------------------------
// https://stackoverflow.com/questions/58102486/opencv-utils-mattobitmap-assertion-failed-androidbitmap-lockpixelsenv-bitmap
// https://sudarnimalan.blogspot.com/2011/09/android-convert-immutable-bitmap-into.html
fun Bitmap.copyBmp(isMutable: Boolean = true): Bitmap {
    return copy(Bitmap.Config.ARGB_8888, isMutable)
}

// ----------------------------------------------------------------
fun <T> Set<T>.copy(): Set<T> {
    return HashSet(this)
}

fun <T> List<T>.copy(): List<T> {
    return ArrayList(this)
}

fun <T> List<T>.set(index: Int, data: T): List<T> {
    val newList = ArrayList(this)
    newList[index] = data

    return newList
}

fun <T> List<T>.add(data: T): List<T> {
    val newList = ArrayList(this)
    newList.add(data)

    return newList
}

fun <T> List<T>.clear(): List<T> {
    return listOf()
}

// ----------------------------------------------------------------
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

// ----------------------------------------------------------------
fun RectF.leftPlusRight(): Float {
    return left + right
}

fun RectF.topPlusBottom(): Float {
    return top + bottom
}

// ----------------------------------------------------------------
fun PointF.notEqualz(value: PointF): Boolean {
    return !equalz(value)
}

fun PointF.equalz(value: PointF): Boolean {
    return x == value.x && y == value.y
}

fun PointF.copy(x: Float = this.x, y: Float = this.y): PointF {
    return PointF(x, y)
}

// ----------------------------------------------------------------
fun String.toUri(): Uri {
    return Uri.fromFile(File(this))
}

// ----------------------------------------------------------------
fun <T> Int.createList(onAdd: (index: Int) -> T): List<T> {
    val output = ArrayList<T>()

    for (i in 0 until this) {
        output.add(onAdd(i))
    }

    return output
}

// ----------------------------------------------------------------
fun Float.round(decimals: Int = 0): Float {
    return this.toDouble().round(decimals).toFloat()
}

fun Double.round(decimals: Int = 0): Double {
    val multiplier = 10.0.pow(decimals)
    return (this * multiplier).roundToInt() / multiplier
}

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