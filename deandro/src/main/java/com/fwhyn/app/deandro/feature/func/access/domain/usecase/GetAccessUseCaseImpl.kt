package com.fwhyn.app.deandro.feature.func.access.domain.usecase

import com.fwhyn.app.deandro.common.helper.Constant
import com.fwhyn.app.deandro.feature.func.access.data.repository.AccessRepository
import com.fwhyn.app.deandro.feature.func.access.domain.helper.toAccessParam
import com.fwhyn.app.deandro.feature.func.access.domain.helper.toGetAccessResult
import com.fwhyn.app.deandro.feature.func.access.domain.model.AccessResult
import com.fwhyn.app.deandro.feature.func.access.domain.model.GetAccessParam

class GetAccessUseCaseImpl(
    private val accessRepository: AccessRepository,
) : GetAccessUseCase() {

    init {
        setTimeOutMillis(Constant.TIMEOUT_MILLIS)
    }

    override suspend fun onRunning(
        param: GetAccessParam,
        result: suspend (AccessResult) -> Unit
    ) {
        val output: AccessResult = accessRepository.get(param.toAccessParam()).toGetAccessResult()
        result(output)
    }
}