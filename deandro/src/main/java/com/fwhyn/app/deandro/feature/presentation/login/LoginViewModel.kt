package com.fwhyn.app.deandro.feature.presentation.login

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.fwhyn.app.deandro.feature.func.auth.domain.model.AuthTokenModel
import com.fwhyn.app.deandro.feature.func.auth.domain.model.GetAuthTokenParam
import com.fwhyn.app.deandro.feature.func.auth.domain.usecase.GetAuthTokenUseCase
import com.fwhyn.lib.baze.common.model.Exzeption
import com.fwhyn.lib.baze.common.model.Status
import com.fwhyn.lib.baze.compose.helper.ActivityRetainedState
import com.fwhyn.lib.baze.string.helper.StringIdManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginUiData: LoginUiData,
    val loginUiState: LoginUiState,
    private val activityRetainedState: ActivityRetainedState,
    private val stringIdManager: StringIdManager<Status>,
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
        if (loginUiState.isLoggedIn) {
            loginUiState.state = LoginUiState.State.LoggedIn()
        }
    }

    private fun getToken(getAuthTokenParam: GetAuthTokenParam) {

        getTokenUseCase.invoke(
            scope = viewModelScope,
            onStart = { activityRetainedState.showLoading() },
            onFetchParam = { getAuthTokenParam },
            onOmitResult = {
                it.onSuccess { output ->
                    if (output != AuthTokenModel.None) {
                        loginUiState.state = LoginUiState.State.LoggedIn()
                    }
                }.onFailure { error ->
                    if (loginUiState.tryCount > 0) {
                        val exception = error as? Exzeption
                        val status = exception?.status ?: Status.UnknownError
                        activityRetainedState.showNotification(stringIdManager.getId(status))
                    }
                }

                loginUiState.isLoggedIn = it.isSuccess
            },
            onFinish = { activityRetainedState.dismissLoading() }
        )
    }

    private fun getTryCount(prevValue: Int): Int {
        return if (prevValue == MAX_LOGIN_TRY) {
            prevValue
        } else {
            prevValue + 1
        }
    }
}