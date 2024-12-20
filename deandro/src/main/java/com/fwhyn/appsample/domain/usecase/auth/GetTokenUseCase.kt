package com.fwhyn.appsample.domain.usecase.auth

import com.fwhyn.appsample.data.helper.Constant.TIMEOUT_MILLIS
import com.fwhyn.appsample.data.model.auth.LoginParam
import com.fwhyn.appsample.data.model.auth.UserToken
import com.fwhyn.appsample.data.repository.BaseRepositoryCoroutine
import com.fwhyn.domain.usecase.BaseUseCaseRemote
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
) : BaseUseCaseRemote<LoginParam, UserToken?>() {
    override fun executeOnBackground(param: LoginParam, scope: CoroutineScope) {
        setTimeOut(TIMEOUT_MILLIS)

        runWithResult(scope) {
            tokenRepository.get(param)
        }
    }
}