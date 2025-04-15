package com.fwhyn.deandro.domain.usecase.auth

import com.fwhyn.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.baze.domain.usecase.BaseUseCase
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import javax.inject.Inject

class SetTokenUseCase @Inject constructor(
    private val tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
) : BaseUseCase<UserToken?, Unit>() {

    override suspend fun onRunning(param: UserToken?) = tokenRepository.set(null, param)
}