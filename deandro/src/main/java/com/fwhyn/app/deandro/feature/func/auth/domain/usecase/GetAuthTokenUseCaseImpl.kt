package com.fwhyn.app.deandro.feature.func.auth.domain.usecase

import com.fwhyn.app.deandro.common.helper.Constant.TIMEOUT_MILLIS
import com.fwhyn.app.deandro.feature.func.auth.data.repository.AuthTokenRepository
import com.fwhyn.app.deandro.feature.func.auth.domain.helper.toAuthTokenModel
import com.fwhyn.app.deandro.feature.func.auth.domain.helper.toGetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.domain.model.AuthTokenModel
import com.fwhyn.app.deandro.feature.func.auth.domain.model.GetAuthTokenParam
import com.fwhyn.lib.baze.common.data.model.Exzeption
import com.fwhyn.lib.baze.common.data.model.Status
import javax.inject.Inject

class GetAuthTokenUseCaseImpl @Inject constructor(
    private val authTokenRepository: AuthTokenRepository,
) : GetAuthTokenUseCase() {

    init {
        setTimeOutMillis(TIMEOUT_MILLIS)
    }

    override suspend fun onRunning(param: GetAuthTokenParam): AuthTokenModel {
        val result = authTokenRepository.get(param.toGetAuthTokenRepoParam()).toAuthTokenModel()

        return when (result) {
            is AuthTokenModel.Data -> result
            AuthTokenModel.None -> throw Exzeption(status = Status.Unauthorized)
        }
    }
}