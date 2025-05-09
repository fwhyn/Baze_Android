package com.fwhyn.app.deandro.ui.feature.auth

import androidx.lifecycle.ViewModel
import com.fwhyn.app.deandro.data.model.auth.LoginParam

abstract class LoginVmInterface : ViewModel() {

    open fun onEmailValueChange(value: String) {}
    open fun onPasswordValueChange(value: String) {}
    open fun onCheckRememberMe() {}
    open fun onLogin(loginParam: LoginParam) {}
    open fun onCalledFromBackStack() {}
}