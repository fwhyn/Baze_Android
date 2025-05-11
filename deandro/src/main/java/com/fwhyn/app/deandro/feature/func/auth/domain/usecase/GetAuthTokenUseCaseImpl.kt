package com.fwhyn.app.deandro.feature.func.auth.domain.usecase

import com.fwhyn.app.deandro.common.helper.Constant.TIMEOUT_MILLIS
import com.fwhyn.app.deandro.feature.func.auth.data.repository.AuthTokenRepository
import com.fwhyn.app.deandro.feature.func.auth.domain.helper.toAuthTokenModel
import com.fwhyn.app.deandro.feature.func.auth.domain.helper.toGetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.domain.model.AuthTokenModel
import com.fwhyn.app.deandro.feature.func.auth.domain.model.GetAuthTokenParam
import javax.inject.Inject

class GetAuthTokenUseCaseImpl @Inject constructor(
    private val authTokenRepository: AuthTokenRepository,
) : GetAuthTokenUseCase() {

    init {
        setTimeOutMillis(TIMEOUT_MILLIS)
    }

    override suspend fun onRunning(param: GetAuthTokenParam): AuthTokenModel {
        return authTokenRepository.get(param.toGetAuthTokenRepoParam()).toAuthTokenModel()
    }
}