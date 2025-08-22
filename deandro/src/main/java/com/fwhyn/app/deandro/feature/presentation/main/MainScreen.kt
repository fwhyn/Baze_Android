package com.fwhyn.app.deandro.feature.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.fwhyn.app.deandro.feature.presentation.NavigationHost
import com.fwhyn.lib.baze.common.helper.extension.showToast
import com.fwhyn.lib.baze.compose.dialog.BazeDialog
import com.fwhyn.lib.baze.compose.dialog.CircularProgressDialog
import com.fwhyn.lib.baze.compose.helper.ActivityRetainedState
import com.fwhyn.lib.baze.compose.helper.ActivityState

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
        is ActivityRetainedState.State.Loading -> CircularProgressDialog()
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