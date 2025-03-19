package com.fwhyn.deandro.ui.feature.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LoginUiData @Inject constructor() {

    var email: String by mutableStateOf("")
    var pwd: String by mutableStateOf("")

    var rememberMe: Boolean by mutableStateOf(false)
        private set

    fun updateRemember() {
        rememberMe = !rememberMe
    }

    val isNotEmpty: Boolean
        get() = email.isNotEmpty() && pwd.isNotEmpty()
}