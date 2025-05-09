package com.fwhyn.app.deandro.feature.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.fwhyn.app.deandro.feature.presentation.auth.LOGIN_ROUTE
import com.fwhyn.app.deandro.feature.presentation.auth.addLoginScreen
import com.fwhyn.app.deandro.feature.presentation.home.addHomeScreen
import com.fwhyn.lib.baze.ui.main.ActivityState

@Composable
fun NavigationHost(
    activityState: ActivityState,
) {
    NavHost(
        navController = activityState.navigation,
        startDestination = LOGIN_ROUTE
    ) {
        addLoginScreen(
            activityState = activityState,
        )
        addHomeScreen(
            activityState = activityState,
        )

//        addPhotoEditScreen(
//            activityState = activityState,
//        )
    }
}