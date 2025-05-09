package com.fwhyn.app.deandro.feature.func.access.domain.usecase

import com.fwhyn.app.deandro.common.helper.Constant
import com.fwhyn.app.deandro.feature.func.access.data.repository.AccessRepositoryInterface
import com.fwhyn.app.deandro.feature.func.access.domain.helper.toAccessParam
import com.fwhyn.app.deandro.feature.func.access.domain.helper.toGetAccessResult
import com.fwhyn.app.deandro.feature.func.access.domain.model.GetAccessParam
import com.fwhyn.app.deandro.feature.func.access.domain.model.GetAccessResult

class GetAccessUseCase(
    private val accessRepository: AccessRepositoryInterface,
) : GetAccessUseCaseInterface() {

    init {
        setTimeOutMillis(Constant.TIMEOUT_MILLIS)
    }

    override suspend fun onRunning(param: GetAccessParam): GetAccessResult {
        return accessRepository.get(param.toAccessParam()).toGetAccessResult()
    }
}