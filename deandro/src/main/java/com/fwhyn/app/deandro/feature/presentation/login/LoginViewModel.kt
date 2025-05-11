package com.fwhyn.app.deandro.feature.presentation.login

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.fwhyn.app.deandro.feature.func.auth.domain.model.AuthTokenModel
import com.fwhyn.app.deandro.feature.func.auth.domain.model.GetAuthTokenParam
import com.fwhyn.app.deandro.feature.func.auth.domain.usecase.GetAuthTokenUseCase
import com.fwhyn.lib.baze.data.model.Status
import com.fwhyn.lib.baze.domain.helper.Rezult
import com.fwhyn.lib.baze.domain.usecase.BaseUseCase
import com.fwhyn.lib.baze.ui.helper.MessageHandler
import com.fwhyn.lib.baze.ui.main.ActivityRetainedState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginUiData: LoginUiData,
    val loginUiState: LoginUiState,
    private val activityRetainedState: ActivityRetainedState,
    private val messageHandler: MessageHandler<Status>,
    private val getTokenUseCase: GetAuthTokenUseCase,
) : LoginVmInterface() {

    companion object {
        const val MAX_LOGIN_TRY = 3
    }

    init {
        init()
    }

    private fun init() {
        getToken(GetAuthTokenParam.Local)
    }

    override fun onEmailValueChange(value: String) {
        loginUiData.email = value
    }

    override fun onPasswordValueChange(value: String) {
        loginUiData.pwd = value
    }

    override fun onCheckRememberMe() {
        loginUiData.updateRemember()
    }

    @SuppressLint("NewApi")
    override fun onLogin(getAuthTokenParam: GetAuthTokenParam) {
        loginUiState.tryCount = getTryCount(loginUiState.tryCount)

        getToken(getAuthTokenParam)
    }

    override fun onCalledFromBackStack() {
        if (loginUiState.loginResult is Rezult.Success) {
            loginUiState.state = LoginUiState.State.LoggedIn()
        }
    }

    private fun getToken(getAuthTokenParam: GetAuthTokenParam) {
        getTokenUseCase
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        if (loginUiState.tryCount > 0) {
                            activityRetainedState.showNotification(
                                messageHandler.getMessage(Status.Instance(-1, it.err.message ?: ""))
                            )
                        }
                    }

                    is Rezult.Success -> {
                        if (it.dat != AuthTokenModel.None) {
                            loginUiState.state = LoginUiState.State.LoggedIn()
                        }
                    }
                }

                loginUiState.loginResult = it
            }
            .setLifeCycleNotifier {
                when (it) {
                    BaseUseCase.LifeCycle.OnStart -> activityRetainedState.showLoading()
                    BaseUseCase.LifeCycle.OnFinish -> activityRetainedState.dismissLoading()
                }
            }
            .execute(getAuthTokenParam, viewModelScope)
    }

    private fun getTryCount(prevValue: Int): Int {
        return if (prevValue == MAX_LOGIN_TRY) {
            prevValue
        } else {
            prevValue + 1
        }
    }
}