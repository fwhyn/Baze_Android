package com.fwhyn.data.model

data class BazeException(
    val status: Status = Status.BadRequest,
    val throwable: Throwable? = null,
) : Exception(status.msg, throwable)