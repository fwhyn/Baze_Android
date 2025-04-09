package com.fwhyn.deandro.domain.usecase.auth

import com.fwhyn.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.baze.domain.usecase.BaseUseCaseRemote
import com.fwhyn.deandro.data.model.auth.LoginParam
import com.fwhyn.deandro.data.model.auth.UserToken
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SetTokenUseCase @Inject constructor(
    private val tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
) : BaseUseCaseRemote<UserToken?, Unit>() {
    override fun execute(param: UserToken?, scope: CoroutineScope) {
        runWithResult(scope) {
            tokenRepository.set(null, param)
        }
    }
}