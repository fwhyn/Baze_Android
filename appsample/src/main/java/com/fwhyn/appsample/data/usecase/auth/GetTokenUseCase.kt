package com.fwhyn.domain.usecase.auth

import com.fwhyn.data.model.auth.LoginParam
import com.fwhyn.data.model.auth.UserToken
import com.fwhyn.data.repository.BaseRepositoryCoroutine
import com.fwhyn.domain.helper.Constant.TIMEOUT
import com.fwhyn.domain.usecase.BaseUseCaseRemote
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
) : BaseUseCaseRemote<LoginParam, UserToken?>() {
    override fun executeOnBackground(param: LoginParam, coroutineScope: CoroutineScope) {
        setTimeOut(TIMEOUT)

        runWithResult(coroutineScope) {
            tokenRepository.get(param)
        }
    }
}