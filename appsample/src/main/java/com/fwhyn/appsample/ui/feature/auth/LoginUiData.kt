package com.fwhyn.appsample.ui.feature.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class LoginUiData {

    var email: String by mutableStateOf("")
    var pwd: String by mutableStateOf("")
    var id: String by mutableStateOf("")

    var remember: Boolean by mutableStateOf(false)
        private set

    fun updateRemember() {
        remember = !remember
    }

    val isNotEmpty: Boolean
        get() = email.isNotEmpty() && pwd.isNotEmpty() && id.isNotEmpty()

    val loginData: LoginParam
        get() = LoginParam(email, pwd, id).also {
            it.forceLogin = LoginParam.ForceLogin.YES
            it.remember = remember
        }

}