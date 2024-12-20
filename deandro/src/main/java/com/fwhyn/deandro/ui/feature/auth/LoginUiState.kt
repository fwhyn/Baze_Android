package com.fwhyn.deandro.ui.feature.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fwhyn.baze.data.helper.extension.getTestTag
import com.fwhyn.baze.domain.helper.Rezult
import com.fwhyn.baze.domain.helper.SingleEvent
import com.fwhyn.deandro.data.model.auth.UserToken

class LoginUiState {

    var tryCount: Int = 0
    var loginResult: Rezult<UserToken?, Exception>? = null
    var state: State by mutableStateOf(State.NotLoggedIn())

    sealed class State() : SingleEvent<Boolean>(true) {

        // If we need a new instance each we call this state, then use class instead of object
        // Example: class Idle: State("Idle")
        // If we just need the same instance, declare it as an object declaration using the object keyword. This ensures
        // that it's always instantiated lazily when referenced
        // Example: objet Idle: State("Idle")

        private val debugTag = LoginUiState::class.java.getTestTag()

        constructor(message: String) : this() {
            Log.d(debugTag, "State: $message")
        }

        class LoggedIn : State("LoggedIn")
        class NotLoggedIn : State("LoggedIn")
    }
}