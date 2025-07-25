package com.fwhyn.app.deandro.feature.func.auth.domain.usecase

import com.fwhyn.app.deandro.feature.func.auth.data.repository.AuthTokenRepository
import com.fwhyn.app.deandro.feature.func.auth.domain.helper.toAuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.domain.helper.toSetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.domain.model.SetAuthTokenParam
import javax.inject.Inject

class SetAuthTokenUseCaseImpl @Inject constructor(
    private val authTokenRepository: AuthTokenRepository,
) : SetAuthTokenUseCase() {

    override suspend fun onRunning(
        param: SetAuthTokenParam,
        result: suspend (Unit) -> Unit
    ) {
        when (param) {
            is SetAuthTokenParam.Local -> authTokenRepository.set(
                param.toSetAuthTokenRepoParam(),
                param.authTokenModel.toAuthTokenRaw()
            )

            is SetAuthTokenParam.Unknown -> TODO()
        }
    }
}