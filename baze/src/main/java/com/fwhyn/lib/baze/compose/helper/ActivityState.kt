package com.fwhyn.lib.baze.compose.helper

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fwhyn.lib.baze.compose.model.ActivityResult
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberActivityState(
    window: WindowSizeClass = WindowSizeClass.calculateFromSize(
        DpSize(
            width = 324.dp,
            height = 720.dp
        )
    ),
    navigation: NavHostController = rememberNavController(),
    notification: SnackbarHostState = remember { SnackbarHostState() },
    activityResult: SharedFlow<ActivityResult>? = null,
): ActivityState {
    return remember(
        window,
        navigation,
        notification,
        activityResult
    ) {
        ActivityState(
            window,
            navigation,
            notification,
            activityResult,
        )
    }
}

@Stable
class ActivityState(
    val window: WindowSizeClass,
    val navigation: NavHostController,
    val notification: SnackbarHostState,
    var activityResult: SharedFlow<ActivityResult>?
)