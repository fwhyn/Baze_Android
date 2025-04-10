package com.fwhyn.deandro.domain.usecase.auth

import com.fwhyn.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.baze.domain.usecase.BaseUseCase
import com.fwhyn.deandro.data.helper.Constant.TIMEOUT_MILLIS
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
) : BaseUseCase<LoginParam, UserToken?>() {

    init {
        setTimeOutMillis(TIMEOUT_MILLIS)
    }

    override suspend fun onRunning(param: LoginParam): UserToken? = tokenRepository.get(param)
}