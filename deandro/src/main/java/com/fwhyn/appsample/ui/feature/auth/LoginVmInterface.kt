package com.fwhyn.appsample.ui.feature.auth

import androidx.lifecycle.ViewModel

abstract class LoginVmInterface : ViewModel() {

    open fun onEmailValueChange(value: String) {}
    open fun onPasswordValueChange(value: String) {}
    open fun onCheckRememberMe() {}
    open fun onLogin() {}
    open fun onCalledFromBackStack() {}
}