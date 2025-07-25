package com.fwhyn.app.deandro.feature.func.access.domain.usecase

import com.fwhyn.app.deandro.feature.func.access.domain.model.AccessResult
import com.fwhyn.app.deandro.feature.func.access.domain.model.GetAccessParam
import com.fwhyn.lib.baze.common.helper.BaseRunner

abstract class GetAccessUseCase : BaseRunner<GetAccessParam, AccessResult>()