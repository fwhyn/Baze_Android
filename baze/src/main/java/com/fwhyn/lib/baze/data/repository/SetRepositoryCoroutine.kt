package com.fwhyn.lib.baze.data.repository

interface SetRepositoryCoroutine<PARAM, DATA> {

    /**
     * Sets the data for the given parameter.
     *
     * @param param The parameter for which to set the data.
     * @param data The data to set.
     */
    suspend fun set(param: PARAM, data: DATA)
}