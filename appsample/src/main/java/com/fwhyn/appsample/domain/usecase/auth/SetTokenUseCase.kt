package com.fwhyn.appsample.domain.usecase.auth

import com.fwhyn.appsample.data.model.auth.LoginParam
import com.fwhyn.appsample.data.model.auth.UserToken
import com.fwhyn.appsample.data.repository.BaseRepositoryCoroutine
import com.fwhyn.domain.usecase.BaseUseCaseRemote
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SetTokenUseCase @Inject constructor(
    private val tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
) : BaseUseCaseRemote<UserToken?, Unit>() {
    override fun executeOnBackground(param: UserToken?, scope: CoroutineScope) {
        runWithResult(scope) {
            tokenRepository.set(null, param)
        }
    }
}