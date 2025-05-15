package com.fwhyn.lib.baze.common.data.repository

interface BaseRepositoryCoroutine<PARAM, DATA> {
    suspend fun get(param: PARAM): DATA

    suspend fun set(param: PARAM, data: DATA)
}