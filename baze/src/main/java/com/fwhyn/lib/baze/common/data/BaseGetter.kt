package com.fwhyn.lib.baze.common.data

interface BaseGetter<PARAM, DATA> {

    /**
     * Retrieves the data for the given parameter.
     *
     * @param param The parameter for which to retrieve the data.
     * @return The data associated with the parameter.
     */
    fun get(param: PARAM): DATA
}