package com.fwhyn.app.deandro.feature.presentation.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fwhyn.lib.baze.common.helper.SingleEvent
import com.fwhyn.lib.baze.common.helper.extension.getDebugTag
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LoginUiState @Inject constructor() {

    var tryCount: Int = 0
    var isLoggedIn: Boolean = false
    var state: State by mutableStateOf(State.NotLoggedIn())

    sealed class State() : SingleEvent<Boolean>(true) {

        // If we need a new instance each we call this state, then use class instead of object
        // Example: class Idle: State("Idle")
        // If we just need the same instance, declare it as an object declaration using the object keyword. This ensures
        // that it's always instantiated lazily when referenced
        // Example: objet Idle: State("Idle")

        private val debugTag = LoginUiState::class.java.getDebugTag()

        constructor(message: String) : this() {
            Log.d(debugTag, "State: $message")
        }

        class LoggedIn : State("LoggedIn")
        class NotLoggedIn : State("NotLoggedIn")
    }
}