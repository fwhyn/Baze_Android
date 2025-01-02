package com.fwhyn.deandro.ui.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.fwhyn.baze.data.helper.extension.showToast
import com.fwhyn.baze.ui.dialog.BazeDialog
import com.fwhyn.baze.ui.dialog.CircularProgressDialog
import com.fwhyn.baze.ui.main.ActivityRetainedState
import com.fwhyn.baze.ui.main.AppState
import com.fwhyn.deandro.ui.NavigationHost

@Composable
fun MainScreen(
    appState: AppState,
    activityRetainedState: ActivityRetainedState,
) {
    MainHomeView(
        appState = appState,
        activityRetainedState = activityRetainedState,
    )

    when (val state = activityRetainedState.state) {
        ActivityRetainedState.State.Idle -> {} // Do nothing
        is ActivityRetainedState.State.Loading -> CircularProgressDialog(state.progress)
        is ActivityRetainedState.State.OnNotification -> LocalContext.current.showToast(state.getMessageAndFinish(true))
        is ActivityRetainedState.State.OnShowDialog<*> -> BazeDialog(state.model)
        ActivityRetainedState.State.OnFinish -> {} // Do nothing
    }
}

@Composable
private fun MainHomeView(
    appState: AppState,
    activityRetainedState: ActivityRetainedState,
) {
    NavigationHost(
        appState = appState,
        activityRetainedState = activityRetainedState,
    )
}