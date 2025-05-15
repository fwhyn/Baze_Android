package com.fwhyn.lib.baze.domain.helper

open class SingleEvent<T>(data: T) : Event<T>(data) {

    fun invokeOnce(onInvoked: (data: T) -> Unit) {
        dataOrNull?.let { onInvoked(it) }
    }
}