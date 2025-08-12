package com.fwhyn.lib.baze.compose.model

sealed class CommonState {
    data object Idle : CommonState()
    class Dialog<T>(val tag: String, val dat: T) : CommonState()
}