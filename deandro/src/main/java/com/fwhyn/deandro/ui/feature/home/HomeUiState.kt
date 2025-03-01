package com.fwhyn.deandro.ui.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fwhyn.baze.domain.helper.SingleEvent

class HomeUiState {

    var state: State by mutableStateOf(State.Idle)

    sealed class State : SingleEvent<Boolean>(true) {
        class CallPhotoEdit(val key: String) : State()
        class LoggedOut : State()
        data object Idle : State()
    }
}