package com.fwhyn.deandro.ui.feature.auth

import android.content.Context
import androidx.lifecycle.ViewModel

abstract class LoginVmInterface : ViewModel() {

    open fun onEmailValueChange(value: String) {}
    open fun onPasswordValueChange(value: String) {}
    open fun onCheckRememberMe() {}
    open fun onLogin(context: Context) {}
    open fun onCalledFromBackStack() {}
}