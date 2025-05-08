package com.fwhyn.deandro.access.domain.usecase

import com.fwhyn.deandro.access.domain.model.GetAccessParam
import com.fwhyn.deandro.access.domain.model.GetAccessResult
import com.fwhyn.lib.baze.domain.usecase.BaseUseCase

abstract class GetAccessUseCaseInterface : BaseUseCase<GetAccessParam, GetAccessResult>()