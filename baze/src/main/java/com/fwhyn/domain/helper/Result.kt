package com.fwhyn.domain.helper

sealed class Result<out DATA, out ERROR> : SingleEvent<Boolean>(true) {
    data class Success<D>(val dat: D) : Result<D, Nothing>()
    data class Failure<E>(val err: E) : Result<Nothing, E>()
}