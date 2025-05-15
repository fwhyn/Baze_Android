package com.fwhyn.lib.baze.common.data.model

data class Exzeption(
    val status: Status = Status.UnknownError,
    val throwable: Throwable? = null,
) : Exception(status.msg, throwable)