package com.fwhyn.app.deandro.access.domain.usecase

import com.fwhyn.app.deandro.access.data.repository.AccessRepositoryInterface
import com.fwhyn.app.deandro.access.domain.helper.toAccessParam
import com.fwhyn.app.deandro.access.domain.helper.toGetAccessResult
import com.fwhyn.app.deandro.access.domain.model.GetAccessParam
import com.fwhyn.app.deandro.access.domain.model.GetAccessResult
import com.fwhyn.app.deandro.data.helper.Constant

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