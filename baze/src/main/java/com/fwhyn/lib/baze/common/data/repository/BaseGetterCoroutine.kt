package com.fwhyn.lib.baze.common.data.repository

interface BaseGetterCoroutine<PARAM, DATA> {

    /**
     * Retrieves data for the given parameter.
     *
     * @param param The parameter for which to retrieve data.
     * @return The retrieved data.
     */
    suspend fun get(param: PARAM): DATA
}