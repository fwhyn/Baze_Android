package com.fwhyn.appsample.data.model.auth

import com.google.gson.annotations.SerializedName

data class LoginParam(
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String,
    @SerializedName("id_atm_sehat_kit") var id: String,
) {
    var forceLogin: ForceLogin = ForceLogin.NO
    var remember: Boolean = false

    fun isNotEmpty(): Boolean {
        return username.isNotEmpty() && username.isNotEmpty() && id.isNotEmpty()
    }

    enum class ForceLogin(val data: Int) {
        YES(1),
        NO(0)
    }
}