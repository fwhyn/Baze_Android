package com.fwhyn.app.deandro.data.model.auth

import android.app.Activity
import com.google.gson.annotations.SerializedName

sealed class LoginParam() {

    data class Google(
        val activity: Activity,
    ) : LoginParam()

    data object Local : LoginParam()

    data class MyServer(
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String,
    ) : LoginParam() {
        @SerializedName("id_atm_sehat_kit")
        val id: String = "rspon1"
        var forceLogin: ForceLogin = ForceLogin.NO
        var remember: Boolean = false

        fun isNotEmpty(): Boolean {
            return username.isNotEmpty() && username.isNotEmpty()
        }
    }

    enum class ForceLogin(val data: Int) {
        YES(1),
        NO(0)
    }
}