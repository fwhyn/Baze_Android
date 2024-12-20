package com.fwhyn.deandro.ui.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.fwhyn.data.helper.extension.showToast
import com.fwhyn.deandro.ui.NavigationHost
import com.fwhyn.ui.dialog.BazeDialog
import com.fwhyn.ui.dialog.CircularProgressDialog
import com.fwhyn.ui.main.AppState
import com.fwhyn.ui.main.MainUiState

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