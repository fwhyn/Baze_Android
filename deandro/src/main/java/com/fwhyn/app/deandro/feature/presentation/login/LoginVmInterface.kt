package com.fwhyn.app.deandro.feature.presentation.login

import androidx.lifecycle.ViewModel
import com.fwhyn.app.deandro.feature.func.auth.domain.model.GetAuthTokenParam

abstract class LoginVmInterface : ViewModel() {

    open fun onEmailValueChange(value: String) {}
    open fun onPasswordValueChange(value: String) {}
    open fun onCheckRememberMe() {}
    open fun onLogin(getAuthTokenParam: GetAuthTokenParam) {}
    open fun onCalledFromBackStack() {}
}