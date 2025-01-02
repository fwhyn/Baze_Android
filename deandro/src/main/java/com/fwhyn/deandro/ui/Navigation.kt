package com.fwhyn.deandro.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.fwhyn.baze.ui.main.ActivityState
import com.fwhyn.baze.ui.main.AppState
import com.fwhyn.deandro.ui.feature.auth.LOGIN_ROUTE
import com.fwhyn.deandro.ui.feature.auth.addLoginScreen
import com.fwhyn.deandro.ui.feature.home.addHomeScreen

@Composable
fun NavigationHost(
    appState: AppState,
    activityState: ActivityState,
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
//            activityState = activityState,
//        )
    }
}