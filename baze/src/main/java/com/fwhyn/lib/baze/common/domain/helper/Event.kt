package com.fwhyn.lib.baze.common.domain.helper

import android.util.Log
import com.fwhyn.lib.baze.common.data.helper.extension.getDebugTag

open class Event<T>(data: T) {

    private val debugTag = Event::class.java.getDebugTag()

    var invoked = false
        private set

    val dataOrNull: T?
        get() {
            val result = if (invoked) {
                null
            } else {
                invoked = true
                data
            }

            Log.d(debugTag, "Event Data: $result")

            return result
        }

    var data: T
        get() {
            Log.d(debugTag, "Event Data: $field")

            return field
        }
        private set

    init {
        requireNotNull(data) { "Null is not allowed." }
        this.data = data
    }
}