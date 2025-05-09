package com.fwhyn.app.deandro.domain.usecase.auth

import com.fwhyn.app.deandro.data.model.auth.LoginParam
import com.fwhyn.app.deandro.data.model.auth.UserToken
import com.fwhyn.lib.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.lib.baze.domain.usecase.BaseUseCase
import javax.inject.Inject

class SetTokenUseCase @Inject constructor(
    private val tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
) : BaseUseCase<UserToken?, Unit>() {

    override suspend fun onRunning(param: UserToken?) = tokenRepository.set(null, param)
}