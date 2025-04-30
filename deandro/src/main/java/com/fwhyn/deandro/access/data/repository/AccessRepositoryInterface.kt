package com.fwhyn.deandro.access.data.repository

import com.fwhyn.baze.data.repository.BaseRepositoryCoroutine
import com.fwhyn.deandro.access.data.model.AccessData
import com.fwhyn.deandro.access.data.model.AccessParam

interface AccessRepositoryInterface : BaseRepositoryCoroutine<AccessParam, AccessData>