package com.fwhyn.lib.baze.common.data.helper.extension

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson

inline fun <reified T> SharedPreferences.get(key: String): T? {
    val value = this.getString(key, null)

    return Gson().fromJson(value, T::class.java)
}

fun <T> SharedPreferences.put(key: String, data: T) {
    val jsonString = if (data != null) Gson().toJson(data) else null

    this.edit { putString(key, jsonString) }
}

fun SharedPreferences.delete(key: String) {
    this.edit { remove(key) }
}