package com.fwhyn.deandro.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.fwhyn.baze.ui.main.ActivityState
import com.fwhyn.deandro.ui.feature.auth.LOGIN_ROUTE
import com.fwhyn.deandro.ui.feature.auth.addLoginScreen
import com.fwhyn.deandro.ui.feature.home.addHomeScreen

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