package com.fwhyn.app.deandro.feature.func.auth.domain.usecase

import com.fwhyn.app.deandro.feature.func.auth.domain.model.SetAuthTokenParam
import com.fwhyn.lib.baze.common.helper.BaseRunner

abstract class SetAuthTokenUseCase : BaseRunner<SetAuthTokenParam, Unit>()