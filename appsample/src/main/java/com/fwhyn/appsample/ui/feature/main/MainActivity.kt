package com.fwhyn.appsample.ui.feature.main

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fwhyn.appsample.ui.common.BaseActivity
import com.fwhyn.appsample.ui.config.BazeTheme
import com.fwhyn.atmsehat.ui.main.MainActivityViewModel
import com.fwhyn.atmsehat.ui.main.MainScreen
import com.fwhyn.ui.main.AppState.Companion.rememberAppState
import com.fwhyn.ui.main.MainUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val vm: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        animateSplashScreen()
        setContent {
            BazeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen.Create(
                        appState = rememberAppState(
                            windowSizeClass = calculateWindowSizeClass(this),
                            networkMonitor = vm.networkMonitor,
                        ),
                        mainUiState = vm.mainUiState,
                    )
                }
            }
        }
    }

    private fun animateSplashScreen(onAnimationEnd: (animator: Animator) -> Unit = {}) {
        val splashScreen = installSplashScreen()
        // Add a callback that's called when the splash screen is animating to the
        // app content.
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            // Create your custom animation.
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.view.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 200L

            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.doOnEnd {
                splashScreenView.remove()
                onAnimationEnd(it)
            }

            // Run your animation.
            slideUp.start()
        }

        splashScreen.setKeepOnScreenCondition {
            when (vm.mainUiState.state) {
                is MainUiState.State.Loading -> true
                else -> false
            }
        }
    }
}