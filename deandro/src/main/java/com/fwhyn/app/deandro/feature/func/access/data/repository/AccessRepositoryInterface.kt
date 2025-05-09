package com.fwhyn.app.deandro.feature.func.access.data.repository

import com.fwhyn.app.deandro.feature.func.access.data.model.AccessData
import com.fwhyn.app.deandro.feature.func.access.data.model.AccessParam
import com.fwhyn.lib.baze.data.repository.BaseRepositoryCoroutine

interface AccessRepositoryInterface : BaseRepositoryCoroutine<AccessParam, AccessData>