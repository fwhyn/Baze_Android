package com.fwhyn.lib.baze.data.repository

interface GetRepositoryCoroutine<PARAM, DATA> {

    /**
     * Retrieves data for the given parameter.
     *
     * @param param The parameter for which to retrieve data.
     * @return The retrieved data.
     */
    suspend fun get(param: PARAM): DATA
}