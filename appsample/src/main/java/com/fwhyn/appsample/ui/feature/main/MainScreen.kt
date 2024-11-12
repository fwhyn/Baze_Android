package com.fwhyn.appsample.ui.feature.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.fwhyn.appsample.ui.NavigationHost
import com.fwhyn.atmsehat.ui.common.dialog.CircularProgressDialog
import com.fwhyn.atmsehat.ui.common.dialog.MyAlertDialog
import com.fwhyn.atmsehat.ui.helper.showToast
import com.fwhyn.ui.main.AppState
import com.fwhyn.ui.main.MainUiState

class MainScreen {
    companion object {
        @Composable
        fun Create(
            appState: AppState,
            mainUiState: MainUiState,
        ) {
            val context = LocalContext.current

            MainView(
                appState = appState,
                mainUiState = mainUiState,
            )

            when (val state = mainUiState.state) {
                MainUiState.State.Idle -> {} // Do nothing

                is MainUiState.State.Loading -> {
                    CircularProgressDialog.Create(state.progress)
                }

                is MainUiState.State.OnNotification -> {
                    state.invokeOnce {
                        context.showToast(state.message)
                    }
                }

                is MainUiState.State.AlertDialog -> {
                    state.run {
                        MyAlertDialog.Create(
                            message = message,
                            title = title,
                            onConfirmation = onConfirmation,
                            onCancellation = onCancellation,
                            onDismissRequest = onDismissRequest,
                            icon = icon,
                        )
                    }
                }

                MainUiState.State.OnFinish -> {} // Do nothing
            }
        }

        @Composable
        fun MainView(
            appState: AppState,
            mainUiState: MainUiState,
        ) {
            NavigationHost(
                appState = appState,
                mainUiState = mainUiState,
            )
        }

        @Composable
        fun Greeting(name: String, modifier: Modifier = Modifier) {
            Text(
                text = "Hello $name",
                modifier = modifier
            )
        }
    }
}