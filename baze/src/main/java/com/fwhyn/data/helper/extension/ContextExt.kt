package com.fwhyn.data.helper.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.annotation.DrawableRes

fun Context.showToast(string: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, string, length).show()
}

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