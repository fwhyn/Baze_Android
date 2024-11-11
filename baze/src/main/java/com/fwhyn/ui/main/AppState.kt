package com.fwhyn.ui.main

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fwhyn.data.helper.network.AlwaysOnlineNetworkMonitor
import com.fwhyn.data.helper.network.NetworkMonitor
import kotlinx.coroutines.CoroutineScope

@Stable
class AppState(
    val scope: CoroutineScope,
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    val networkMonitor: NetworkMonitor,
) {
    companion object {
        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        @Composable
        fun rememberAppState(
            coroutineScope: CoroutineScope = rememberCoroutineScope(),
            navController: NavHostController = rememberNavController(),
            windowSizeClass: WindowSizeClass = WindowSizeClass.calculateFromSize(
                DpSize(
                    width = 324.dp,
                    height = 720.dp
                )
            ),
            networkMonitor: NetworkMonitor = AlwaysOnlineNetworkMonitor(),
        ): AppState {
            return remember(
                navController,
                windowSizeClass,
                networkMonitor,
            ) {
                AppState(
                    coroutineScope,
                    navController,
                    windowSizeClass,
                    networkMonitor,
                )
            }
        }
    }
}