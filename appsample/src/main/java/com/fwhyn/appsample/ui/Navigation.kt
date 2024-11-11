package com.fwhyn.appsample.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.fwhyn.ui.main.AppState
import com.fwhyn.ui.main.MainUiState

@Composable
fun NavigationHost(
    appState: AppState,
    mainUiState: MainUiState,
) {
    NavHost(
        navController = appState.navController,
        startDestination = LoginScreen.LOGIN_ROUTE
    ) {
        addLoginScreen(
            appState = appState,
            mainUiState = mainUiState,
        )
        addHomeScreen(
            appState = appState,
            mainUiState = mainUiState,
        )

        addPhotoEditScreen(
            appState = appState,
            mainUiState = mainUiState,
        )
    }
}