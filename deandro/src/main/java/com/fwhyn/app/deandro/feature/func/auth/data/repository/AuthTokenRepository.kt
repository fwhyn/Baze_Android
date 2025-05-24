package com.fwhyn.app.deandro.feature.func.auth.data.repository

import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.data.model.SetAuthTokenRepoParam
import com.fwhyn.lib.baze.common.data.BaseGetterCoroutine
import com.fwhyn.lib.baze.common.data.BaseSetterCoroutine

interface AuthTokenRepository :
    BaseGetterCoroutine<GetAuthTokenRepoParam, AuthTokenRaw>,
    BaseSetterCoroutine<SetAuthTokenRepoParam, AuthTokenRaw>