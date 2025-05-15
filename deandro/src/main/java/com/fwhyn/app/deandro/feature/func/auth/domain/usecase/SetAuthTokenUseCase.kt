package com.fwhyn.app.deandro.feature.func.auth.domain.usecase

import com.fwhyn.app.deandro.feature.func.auth.domain.model.SetAuthTokenParam
import com.fwhyn.lib.baze.common.domain.usecase.BaseUseCase

abstract class SetAuthTokenUseCase : BaseUseCase<SetAuthTokenParam, Unit>()