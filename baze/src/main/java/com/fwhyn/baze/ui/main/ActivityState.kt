package com.fwhyn.baze.ui.main

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberActivityState(
    navigation: NavHostController = rememberNavController(),
    window: WindowSizeClass = WindowSizeClass.calculateFromSize(
        DpSize(
            width = 324.dp,
            height = 720.dp
        )
    ),
): ActivityState {
    return remember(
        navigation,
        window,
    ) {
        ActivityState(
            navigation,
            window,
        )
    }
}

@Stable
class ActivityState(
    val navigation: NavHostController,
    val window: WindowSizeClass,
)