package com.fwhyn.deandro.data.repository

interface BaseRepository<PARAM, DATA> {
    fun get(param: PARAM): DATA

    fun set(param: PARAM, data: DATA)
}