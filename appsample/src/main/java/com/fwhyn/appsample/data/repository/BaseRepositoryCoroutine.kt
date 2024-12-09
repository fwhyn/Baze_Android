package com.fwhyn.appsample.data.repository

interface BaseRepositoryCoroutine<PARAM, DATA> {
    suspend fun get(param: PARAM): DATA

    suspend fun set(param: PARAM, data: DATA)
}