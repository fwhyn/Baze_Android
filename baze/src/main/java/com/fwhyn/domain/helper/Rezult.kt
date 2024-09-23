package com.fwhyn.domain.helper

sealed class Rezult<out DATA, out ERROR> : SingleEvent<Boolean>(true) {
    data class Success<D>(val dat: D) : Rezult<D, Nothing>()
    data class Failure<E>(val err: E) : Rezult<Nothing, E>()
}