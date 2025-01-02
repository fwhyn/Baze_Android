package com.fwhyn.deandro.ui.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.fwhyn.baze.data.helper.extension.showToast
import com.fwhyn.baze.ui.dialog.BazeDialog
import com.fwhyn.baze.ui.dialog.CircularProgressDialog
import com.fwhyn.baze.ui.main.ActivityState
import com.fwhyn.baze.ui.main.AppState
import com.fwhyn.deandro.ui.NavigationHost

@Composable
fun MainScreen(
    appState: AppState,
    activityState: ActivityState,
) {
    MainHomeView(
        appState = appState,
        activityState = activityState,
    )

    when (val state = activityState.state) {
        ActivityState.State.Idle -> {} // Do nothing
        is ActivityState.State.Loading -> CircularProgressDialog(state.progress)
        is ActivityState.State.OnNotification -> LocalContext.current.showToast(state.getMessageAndFinish(true))
        is ActivityState.State.OnShowDialog<*> -> BazeDialog(state.model)
        ActivityState.State.OnFinish -> {} // Do nothing
    }
}

@Composable
private fun MainHomeView(
    appState: AppState,
    activityState: ActivityState,
) {
    NavigationHost(
        appState = appState,
        activityState = activityState,
    )
}