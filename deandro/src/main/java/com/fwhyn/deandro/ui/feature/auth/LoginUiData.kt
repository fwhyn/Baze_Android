package com.fwhyn.deandro.ui.feature.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fwhyn.deandro.data.model.auth.LoginParam
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LoginUiData @Inject constructor() {

    var email: String by mutableStateOf("")
    var pwd: String by mutableStateOf("")

    var remember: Boolean by mutableStateOf(false)
        private set

    fun updateRemember() {
        remember = !remember
    }

    val isNotEmpty: Boolean
        get() = email.isNotEmpty() && pwd.isNotEmpty()

    val loginData: LoginParam
        get() = LoginParam.MyServer(email, pwd).also {
            it.forceLogin = LoginParam.ForceLogin.YES
            it.remember = remember
        }

}