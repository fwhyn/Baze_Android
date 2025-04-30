package com.fwhyn.deandro.access.domain.usecase

import com.fwhyn.deandro.access.data.repository.AccessRepositoryInterface
import com.fwhyn.deandro.access.domain.helper.toAccessParam
import com.fwhyn.deandro.access.domain.helper.toGetAccessResult
import com.fwhyn.deandro.access.domain.model.GetAccessParam
import com.fwhyn.deandro.access.domain.model.GetAccessResult
import com.fwhyn.deandro.data.helper.Constant

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