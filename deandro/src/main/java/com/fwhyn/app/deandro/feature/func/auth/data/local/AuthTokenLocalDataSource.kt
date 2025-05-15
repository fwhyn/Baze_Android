package com.fwhyn.app.deandro.feature.func.auth.data.local

import android.content.SharedPreferences
import com.fwhyn.app.deandro.common.di.PreferenceDi
import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.lib.baze.common.data.helper.extension.get
import com.fwhyn.lib.baze.common.data.helper.extension.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenLocalDataSource @Inject constructor(
    @PreferenceDi.EncryptedPrefs private val encryptedPreferences: SharedPreferences,
) {
    companion object {
        const val TOKEN_KEY = "TOKEN_KEY"
    }

    var token: AuthTokenRaw.Dto? = null
        get() {
            return if (field == null) {
                field = encryptedPreferences.get<AuthTokenRaw.Dto>(TOKEN_KEY)
                field
            } else {
                field
            }
        }
        set(value) {
            field = value
            encryptedPreferences.put(TOKEN_KEY, field)
        }
}