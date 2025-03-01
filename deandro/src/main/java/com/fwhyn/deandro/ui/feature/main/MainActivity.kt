package com.fwhyn.deandro.ui.feature.main

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
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
import com.fwhyn.baze.ui.main.ActivityRetainedState
import com.fwhyn.baze.ui.main.rememberActivityState
import com.fwhyn.deandro.BuildConfig
import com.fwhyn.deandro.ui.common.BaseActivity
import com.fwhyn.deandro.ui.config.MyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val vm: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("This Baze App is", "${if (BuildConfig.DEBUG) "debug" else "release"} version")

        val activityRetainedState = vm.activityRetainedState
        animateSplashScreen(activityRetainedState)
        setContent {
            MyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        activityState = rememberActivityState(window = calculateWindowSizeClass(this)),
                        activityRetainedState = activityRetainedState,
                    )
                }
            }
        }
        finishWhenNeeded(activityRetainedState)
    }

    private fun animateSplashScreen(
        activityRetainedState: ActivityRetainedState,
        onAnimationEnd: (animator: Animator) -> Unit = {},
    ) {
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
            when (activityRetainedState.state) {
                is ActivityRetainedState.State.Loading -> true
                else -> false
            }
        }
    }

    private fun finishWhenNeeded(activityRetainedState: ActivityRetainedState) {
        if (activityRetainedState.state == ActivityRetainedState.State.OnFinish) {
            finish()
        }
    }
}