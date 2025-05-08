package com.fwhyn.deandro.access.data.repository

import com.fwhyn.deandro.access.data.model.AccessData
import com.fwhyn.deandro.access.data.model.AccessParam
import com.fwhyn.lib.baze.data.repository.BaseRepositoryCoroutine

interface AccessRepositoryInterface : BaseRepositoryCoroutine<AccessParam, AccessData>