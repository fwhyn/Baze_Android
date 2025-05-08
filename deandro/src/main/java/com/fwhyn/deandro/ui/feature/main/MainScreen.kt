package com.fwhyn.deandro.ui.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.fwhyn.deandro.ui.NavigationHost
import com.fwhyn.lib.baze.data.helper.extension.showToast
import com.fwhyn.lib.baze.ui.dialog.BazeDialog
import com.fwhyn.lib.baze.ui.dialog.CircularProgressDialog
import com.fwhyn.lib.baze.ui.main.ActivityRetainedState
import com.fwhyn.lib.baze.ui.main.ActivityState

@Composable
fun MainScreen(
    activityState: ActivityState,
    activityRetainedState: ActivityRetainedState,
) {
    MainHomeView(
        activityState = activityState,
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
    activityState: ActivityState,
) {
    NavigationHost(
        activityState = activityState,
    )
}