package com.fwhyn.app.deandro.feature.func.auth.domain.usecase

import com.fwhyn.app.deandro.feature.func.auth.domain.model.AuthTokenModel
import com.fwhyn.app.deandro.feature.func.auth.domain.model.GetAuthTokenParam
import com.fwhyn.lib.baze.domain.usecase.BaseUseCase

abstract class GetAuthTokenUseCase : BaseUseCase<GetAuthTokenParam, AuthTokenModel>()