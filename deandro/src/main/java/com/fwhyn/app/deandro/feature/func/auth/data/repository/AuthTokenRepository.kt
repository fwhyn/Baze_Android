package com.fwhyn.app.deandro.feature.func.auth.data.repository

import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.data.model.SetAuthTokenRepoParam
import com.fwhyn.lib.baze.common.helper.BaseGetter
import com.fwhyn.lib.baze.common.helper.BaseSetter

interface AuthTokenRepository :
    BaseGetter<GetAuthTokenRepoParam, AuthTokenRaw>,
    BaseSetter<SetAuthTokenRepoParam, AuthTokenRaw>