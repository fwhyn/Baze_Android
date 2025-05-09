package com.fwhyn.app.deandro.access.domain.usecase

import com.fwhyn.app.deandro.access.domain.model.GetAccessParam
import com.fwhyn.app.deandro.access.domain.model.GetAccessResult
import com.fwhyn.lib.baze.domain.usecase.BaseUseCase

abstract class GetAccessUseCaseInterface : BaseUseCase<GetAccessParam, GetAccessResult>()