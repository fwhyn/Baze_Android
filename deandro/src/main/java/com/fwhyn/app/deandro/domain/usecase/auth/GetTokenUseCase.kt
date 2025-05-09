package com.fwhyn.app.deandro.domain.usecase.auth

import com.fwhyn.app.deandro.data.helper.Constant.TIMEOUT_MILLIS
import com.fwhyn.app.deandro.data.model.auth.LoginParam
import com.fwhyn.app.deandro.data.model.auth.UserToken
import com.fwhyn.lib.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.lib.baze.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
) : BaseUseCase<LoginParam, UserToken?>() {

    init {
        setTimeOutMillis(TIMEOUT_MILLIS)
    }

    override suspend fun onRunning(param: LoginParam): UserToken? = tokenRepository.get(param)
}