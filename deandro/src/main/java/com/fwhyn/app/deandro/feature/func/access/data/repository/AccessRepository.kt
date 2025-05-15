package com.fwhyn.app.deandro.feature.func.access.data.repository

import com.fwhyn.app.deandro.feature.func.access.data.model.AccessResultRaw
import com.fwhyn.app.deandro.feature.func.access.data.model.GetAccessRepoParam
import com.fwhyn.lib.baze.common.data.repository.GetRepositoryCoroutine

interface AccessRepository : GetRepositoryCoroutine<GetAccessRepoParam, AccessResultRaw>