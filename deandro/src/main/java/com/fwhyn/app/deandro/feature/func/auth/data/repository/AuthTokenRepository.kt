package com.fwhyn.app.deandro.feature.func.auth.data.repository

import com.fwhyn.app.deandro.feature.func.auth.data.model.AuthTokenRaw
import com.fwhyn.app.deandro.feature.func.auth.data.model.GetAuthTokenRepoParam
import com.fwhyn.app.deandro.feature.func.auth.data.model.SetAuthTokenRepoParam
import com.fwhyn.lib.baze.data.repository.GetRepositoryCoroutine
import com.fwhyn.lib.baze.data.repository.SetRepositoryCoroutine

interface AuthTokenRepository :
    GetRepositoryCoroutine<GetAuthTokenRepoParam, AuthTokenRaw>,
    SetRepositoryCoroutine<SetAuthTokenRepoParam, AuthTokenRaw>