package com.fwhyn.deandro.ui.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.fwhyn.baze.data.helper.extension.showToast
import com.fwhyn.baze.ui.dialog.BazeDialog
import com.fwhyn.baze.ui.dialog.CircularProgressDialog
import com.fwhyn.baze.ui.main.AppState
import com.fwhyn.baze.ui.main.MainUiState
import com.fwhyn.deandro.ui.NavigationHost

@Composable
fun MainScreen(
    appState: AppState,
    mainUiState: MainUiState,
) {
    MainHomeView(
        appState = appState,
        mainUiState = mainUiState,
    )

    when (val state = mainUiState.state) {
        MainUiState.State.Idle -> {} // Do nothing
        is MainUiState.State.Loading -> CircularProgressDialog(state.progress)
        is MainUiState.State.OnNotification -> LocalContext.current.showToast(state.getMessageAndFinish(true))
        is MainUiState.State.OnShowDialog<*> -> BazeDialog(state.model)
        MainUiState.State.OnFinish -> {} // Do nothing
    }
}

@Composable
private fun MainHomeView(
    appState: AppState,
    mainUiState: MainUiState,
) {
    NavigationHost(
        appState = appState,
        mainUiState = mainUiState,
    )
}