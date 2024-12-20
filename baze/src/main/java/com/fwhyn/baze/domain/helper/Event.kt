package com.fwhyn.baze.domain.helper

import android.util.Log
import com.fwhyn.baze.data.helper.extension.getTestTag

open class Event<T>(data: T) {

    private val debugTag = Event::class.java.getTestTag()

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