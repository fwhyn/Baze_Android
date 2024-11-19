package com.fwhyn.appsample.ui.feature.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fwhyn.appsample.data.model.auth.UserToken
import com.fwhyn.data.helper.getTag
import com.fwhyn.data.model.CustomException
import com.fwhyn.domain.helper.Results
import com.fwhyn.domain.helper.SingleEvent

class LoginUiState {

    var tryCount: Int = 0
    var loginResult: Results<UserToken?, CustomException> = Results.Loading(0)
    var state: State by mutableStateOf(State.Loading)

    sealed class State() : SingleEvent<Boolean>(true) {

        // If we need a new instance each we call this state, then use class instead of object
        // Example: class Idle: State("Idle")
        // If we just need the same instance, declare it as an object declaration using the object keyword. This ensures
        // that it's always instantiated lazily when referenced
        // Example: objet Idle: State("Idle")

        private val debugTag = LoginUiState::class.java.getTag()

        constructor(message: String) : this() {
            Log.d(debugTag, "State: $message")
        }

        class OnNotification(val message: String) : State("OnNotification")
        object Idle : State("Idle")
        object Loading : State("Loading")
        class LoggedIn : State("LoggedIn")
        class OnFinish : State("OnFinish")
    }
}