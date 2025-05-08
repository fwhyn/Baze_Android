package com.fwhyn.lib.baze.domain.helper

sealed class Rezult<out DATA, out ERROR> {
    data class Success<D>(val dat: D) : Rezult<D, Nothing>()
    data class Failure<E>(val err: E) : Rezult<Nothing, E>()
}