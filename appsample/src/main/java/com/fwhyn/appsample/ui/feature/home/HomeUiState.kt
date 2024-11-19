package com.fwhyn.appsample.ui.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fwhyn.domain.helper.SingleEvent

class HomeUiState {

    var state: State by mutableStateOf(State.Idle)

    sealed class State : SingleEvent<Boolean>(true) {

        class OnNotification(val message: String) : State()
        object Idle : State()
        object Loading : State()
        class CallPhotoEdit(val key: String) : State()
        class LoggedOut : State()
        object OnFinish : State()
    }
}