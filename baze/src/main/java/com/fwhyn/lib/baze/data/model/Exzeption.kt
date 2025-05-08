package com.fwhyn.lib.baze.data.model

data class Exzeption(
    val status: Status = Status.BadRequest,
    val throwable: Throwable? = null,
) : Exception(status.msg, throwable)