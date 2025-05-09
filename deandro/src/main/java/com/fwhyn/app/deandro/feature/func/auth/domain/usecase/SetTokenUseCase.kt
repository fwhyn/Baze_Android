package com.fwhyn.app.deandro.feature.func.auth.domain.usecase

import com.fwhyn.app.deandro.feature.func.auth.data.model.LoginParam
import com.fwhyn.app.deandro.feature.func.auth.data.model.UserToken
import com.fwhyn.lib.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.lib.baze.domain.usecase.BaseUseCase
import javax.inject.Inject

class SetTokenUseCase @Inject constructor(
    private val tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
) : BaseUseCase<UserToken?, Unit>() {

    override suspend fun onRunning(param: UserToken?) = tokenRepository.set(null, param)
}