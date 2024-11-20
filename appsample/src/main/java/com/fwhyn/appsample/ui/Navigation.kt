package com.fwhyn.appsample.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.fwhyn.appsample.ui.feature.auth.LOGIN_ROUTE
import com.fwhyn.appsample.ui.feature.auth.addLoginScreen
import com.fwhyn.appsample.ui.feature.home.addHomeScreen
import com.fwhyn.ui.main.AppState
import com.fwhyn.ui.main.MainUiState

@Composable
fun NavigationHost(
    appState: AppState,
    mainUiState: MainUiState,
) {
    NavHost(
        navController = appState.navController,
        startDestination = LOGIN_ROUTE
    ) {
        addLoginScreen(
            appState = appState,
        )
        addHomeScreen(
            appState = appState,
        )

//        addPhotoEditScreen(
//            appState = appState,
//            mainUiState = mainUiState,
//        )
    }
}