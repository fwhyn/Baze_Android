package com.fwhyn.data.helper.extension

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder

inline fun <reified T> SharedPreferences.get(key: String): T? {
    val value = this.getString(key, null)

    return GsonBuilder().create().fromJson(value, T::class.java)
}

fun <T> SharedPreferences.put(key: String, data: T) {
    val jsonString = if (data != null) Gson().toJson(data) else null

    this.edit().putString(key, jsonString).apply()
}

fun SharedPreferences.delete(key: String) {
    this.edit().remove(key).apply()
}