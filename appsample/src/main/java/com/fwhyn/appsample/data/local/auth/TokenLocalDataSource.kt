package com.fwhyn.appsample.data.local.auth

import android.content.SharedPreferences
import com.fwhyn.appsample.data.model.auth.UserToken
import com.fwhyn.data.di.PreferenceModule
import com.fwhyn.data.helper.saveString
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenLocalDataSource @Inject constructor(
    @PreferenceModule.EncryptedPrefs private val encryptedPreferences: SharedPreferences,
) {
    companion object {
        const val TOKEN_KEY = "TOKEN_KEY"
    }

    var token: UserToken? = null
        get() {
            return if (field == null) {
                val tokenString = encryptedPreferences.getString(TOKEN_KEY, null)

                field = Gson().fromJson(tokenString, UserToken::class.java)
                field
            } else {
                field
            }
        }
        set(value) {
            field = value
            encryptedPreferences.saveString(TOKEN_KEY, if (field != null) Gson().toJson(field) else null)
        }
}