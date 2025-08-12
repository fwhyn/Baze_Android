package com.fwhyn.lib.baze.compose.model

sealed class CommonEvent {
    class Notify<T>(val dat: T) : CommonEvent()
}