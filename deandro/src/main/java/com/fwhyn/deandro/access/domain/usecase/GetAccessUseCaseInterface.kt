package com.fwhyn.deandro.access.domain.usecase

import com.fwhyn.baze.domain.usecase.BaseUseCase
import com.fwhyn.deandro.access.domain.model.GetAccessParam
import com.fwhyn.deandro.access.domain.model.GetAccessResult

abstract class GetAccessUseCaseInterface : BaseUseCase<GetAccessParam, GetAccessResult>()