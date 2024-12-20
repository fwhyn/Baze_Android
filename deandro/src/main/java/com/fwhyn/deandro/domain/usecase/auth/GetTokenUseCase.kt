package com.fwhyn.deandro.domain.usecase.auth

import com.fwhyn.deandro.data.helper.Constant.TIMEOUT_MILLIS
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import com.fwhyn.deandro.data.repository.BaseRepositoryCoroutine
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