package com.fwhyn.deandro.ui.feature.auth

import androidx.lifecycle.viewModelScope
import com.fwhyn.baze.data.model.Status
import com.fwhyn.baze.domain.helper.Rezult
import com.fwhyn.baze.domain.usecase.BaseUseCase
import com.fwhyn.baze.domain.usecase.BaseUseCaseRemote
import com.fwhyn.baze.ui.helper.MessageHandler
import com.fwhyn.baze.ui.main.ActivityState
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginUiData: LoginUiData,
    val loginUiState: LoginUiState,
    private val activityState: ActivityState,
    private val messageHandler: MessageHandler<Status>,
    private val getTokenUseCase: BaseUseCaseRemote<LoginParam, UserToken?>,
) : LoginVmInterface() {

    companion object {
        const val MAX_LOGIN_TRY = 3

        const val LOGIN_SUCCESS = "LOGIN_SUCCESS"
        const val LOGIN_FAILED = "LOGIN_FAILED"
    }

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

    override fun onCheckRememberMe() {
        loginUiData.updateRemember()
    }

    override fun onLogin() {
        loginUiState.tryCount = getTryCount(loginUiState.tryCount)

        getToken()
    }

//    override fun onCalledFromBackStack() {
//        loginUiState.run {
//            if (loginResult is Rezult.Success) {
//                state = LoginUiState.State.OnFinish()
//            }
//        }
//    }

    private fun getToken() {
        getTokenUseCase
            .setResultNotifier {
                when (it) {
                    is Rezult.Failure -> {
                        if (loginUiState.tryCount > 0) {
                            activityState.showNotification(messageHandler.getMessage(it.err.status))
                        }
                    }

                    is Rezult.Success -> {
                        if (it.dat != null) {
                            loginUiState.state = LoginUiState.State.LoggedIn()
                        }
                    }
                }

                loginUiState.loginResult = it
            }
            .setLifeCycleNotifier {
                when (it) {
                    BaseUseCase.LifeCycle.OnStart -> activityState.showLoading()
                    BaseUseCase.LifeCycle.OnFinish -> activityState.dismissLoading()
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