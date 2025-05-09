package com.fwhyn.app.deandro.access.data.repository

import com.fwhyn.app.deandro.access.data.model.AccessData
import com.fwhyn.app.deandro.access.data.model.AccessParam
import com.fwhyn.lib.baze.data.repository.BaseRepositoryCoroutine

interface AccessRepositoryInterface : BaseRepositoryCoroutine<AccessParam, AccessData>