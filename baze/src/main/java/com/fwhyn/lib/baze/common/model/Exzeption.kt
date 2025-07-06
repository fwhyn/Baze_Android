package com.fwhyn.lib.baze.common.model

data class Exzeption(
    val status: Status = Status.UnknownError,
    val throwable: Throwable? = null,
) : Exception(status.msg, throwable)