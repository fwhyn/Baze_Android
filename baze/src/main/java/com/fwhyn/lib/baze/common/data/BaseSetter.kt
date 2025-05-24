package com.fwhyn.lib.baze.common.data

interface BaseSetter<PARAM, DATA> {

    /**
     * Sets the data for the given parameter.
     *
     * @param param The parameter for which to set the data.
     * @param data The data to set.
     */
    fun set(param: PARAM, data: DATA)
}