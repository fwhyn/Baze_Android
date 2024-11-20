package com.fwhyn.domain.usecase.auth

import com.fwhyn.data.model.auth.LoginParam
import com.fwhyn.data.model.auth.UserToken
import com.fwhyn.data.repository.BaseRepositoryCoroutine
import com.fwhyn.domain.usecase.BaseUseCaseRemote
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SetTokenUseCase @Inject constructor(
    private val tokenRepository: BaseRepositoryCoroutine<LoginParam?, UserToken?>,
) : BaseUseCaseRemote<UserToken?, Unit>() {
    override fun executeOnBackground(param: UserToken?, coroutineScope: CoroutineScope) {
        runWithResult(coroutineScope) {
            tokenRepository.set(null, param)
        }
    }
}