package com.fwhyn.lib.baze.common.helper.extension

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

fun Context.showToast(@StringRes stringId: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(stringId), length).show()
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

fun Context.getAllActivities(): List<String> {
    val packageName = this.packageName
    val packageManager = this.packageManager
    val activities = mutableListOf<String>()

    val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
    packageInfo.activities?.forEach {
        activities.add(it.name)
    }

    return activities
}