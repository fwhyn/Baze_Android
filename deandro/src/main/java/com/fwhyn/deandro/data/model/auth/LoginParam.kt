package com.fwhyn.deandro.data.model.auth

import com.google.gson.annotations.SerializedName

data class LoginParam(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
) {
    @SerializedName("id_atm_sehat_kit")
    val id: String = "rspon1"
    var forceLogin: ForceLogin = ForceLogin.NO
    var remember: Boolean = false

    fun isNotEmpty(): Boolean {
        return username.isNotEmpty() && username.isNotEmpty()
    }

    enum class ForceLogin(val data: Int) {
        YES(1),
        NO(0)
    }
}