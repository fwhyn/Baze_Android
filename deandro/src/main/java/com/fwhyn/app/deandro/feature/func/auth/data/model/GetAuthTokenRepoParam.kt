package com.fwhyn.app.deandro.feature.func.auth.data.model

import android.app.Activity
import com.google.gson.annotations.SerializedName

sealed class GetAuthTokenRepoParam() {

    data object Local : GetAuthTokenRepoParam()

    data class MyServer(
        @SerializedName("username") val username: String,
        @SerializedName("password") val password: String,
    ) : GetAuthTokenRepoParam() {
        @SerializedName("id_atm_sehat_kit")
        val id: String = "rspon1"
        var forceLogin: ForceLogin = ForceLogin.NO
        var remember: Boolean = false

        fun isNotEmpty(): Boolean {
            return username.isNotEmpty() && username.isNotEmpty()
        }
    }

    data class Google(
        val activity: Activity,
    ) : GetAuthTokenRepoParam()

    enum class ForceLogin(val data: Int) {
        YES(1),
        NO(0)
    }
}