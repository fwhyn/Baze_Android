package com.fwhyn.domain.helper

sealed class BazeResult<out DATA, out ERROR> : SingleEvent<Boolean>(true) {
    data class Success<D>(val dat: D) : BazeResult<D, Nothing>()
    data class Failure<E>(val err: E) : BazeResult<Nothing, E>()
}