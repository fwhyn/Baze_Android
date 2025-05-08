package com.fwhyn.lib.baze.ui.main

import android.app.Activity
import android.content.Intent
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
    onActivityResult: ((activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null,
): ActivityState {
    return remember(
        navigation,
        window,
    ) {
        ActivityState(
            navigation,
            window,
            onActivityResult,
        )
    }
}

@Stable
class ActivityState(
    val navigation: NavHostController,
    val window: WindowSizeClass,
    var onActivityResult: ((activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null
)