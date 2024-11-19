package com.fwhyn.appsample.ui.feature.auth

import androidx.lifecycle.viewModelScope
import com.fwhyn.appsample.data.model.auth.LoginParam
import com.fwhyn.appsample.data.model.auth.UserToken
import com.fwhyn.domain.helper.Results
import com.fwhyn.domain.usecase.BaseUseCaseRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getTokenUseCase: BaseUseCaseRemote<LoginParam, UserToken?>,
) : LoginVmInterface() {

    companion object {
        const val MAX_LOGIN_TRY = 3

        const val LOGIN_SUCCESS = "LOGIN_SUCCESS"
        const val LOGIN_FAILED = "LOGIN_FAILED"
    }

    val loginUiData = LoginUiData()
    val loginUiState = LoginUiState()

    init {
        init()
    }

    private fun init() {
        getToken()
    }

    override fun onEmailValueChange(value: String) {
        loginUiData.email = value
    }

    override fun onPasswordValueChange(value: String) {
        loginUiData.pwd = value
    }

    override fun onStationIdValueChange(value: String) {
        loginUiData.id = value
    }

    override fun onCheckRememberMe() {
        loginUiData.updateRemember()
    }

    override fun onLogin() {
        loginUiState.tryCount = getTryCount(loginUiState.tryCount)

        getToken()
    }

    override fun onCalledFromBackStack() {
        loginUiState.run {
            if (loginResult is Results.Success) {
                state = LoginUiState.State.OnFinish()
            }
        }
    }

    private fun getToken() {
        getTokenUseCase
            .setResultNotifier {
                loginUiState.run {
                    state = when (it) {
                        is Results.Failure -> {
                            val error = it.err
                            if (tryCount > 0) {
                                LoginUiState.State.OnNotification(error.status.code.toString())
                            } else {
                                LoginUiState.State.Idle
                            }
                        }

                        is Results.Loading -> LoginUiState.State.Loading
                        is Results.Success -> {
                            val data = it.dat
                            if (data != null) {
                                LoginUiState.State.LoggedIn()
                            } else {
                                LoginUiState.State.Idle
                            }
                        }
                    }

                    loginResult = it
                }
            }
            .executeOnBackground(loginUiData.loginData, viewModelScope)
    }

    private fun getTryCount(prevValue: Int): Int {
        return if (prevValue == MAX_LOGIN_TRY) {
            prevValue
        } else {
            prevValue + 1
        }
    }
}