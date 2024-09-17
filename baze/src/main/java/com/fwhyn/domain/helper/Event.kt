package com.fwhyn.domain.helper

import android.util.Log
import com.fwhyn.data.helper.getTestTag

open class Event<T>(data: T) {

    private val debugTag = Event::class.java.getTestTag()

    var invoked = false
        private set

    val dataOrNull: T?
        get() = if (invoked) {
            Log.d(debugTag, "Event Data Null")
            null
        } else {
            Log.d(debugTag, "Event Data Exist")
            invoked = true
            data
        }

    var data: T
        get() {
            Log.d(debugTag, "Event Data Invoked")
            return field
        }
        private set

    init {
        requireNotNull(data) { "Null is not allowed." }
        this.data = data
    }
}