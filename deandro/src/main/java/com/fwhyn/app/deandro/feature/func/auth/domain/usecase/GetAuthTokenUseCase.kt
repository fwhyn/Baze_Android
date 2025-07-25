package com.fwhyn.app.deandro.feature.func.auth.domain.usecase

import com.fwhyn.app.deandro.feature.func.auth.domain.model.AuthTokenModel
import com.fwhyn.app.deandro.feature.func.auth.domain.model.GetAuthTokenParam
import com.fwhyn.lib.baze.common.helper.BaseRunner

abstract class GetAuthTokenUseCase : BaseRunner<GetAuthTokenParam, AuthTokenModel>()