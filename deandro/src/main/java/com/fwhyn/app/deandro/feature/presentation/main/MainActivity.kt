package com.fwhyn.app.deandro.feature.presentation.main

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fwhyn.app.deandro.BuildConfig
import com.fwhyn.app.deandro.common.ui.base.BaseActivity
import com.fwhyn.app.deandro.common.ui.config.MyTheme
import com.fwhyn.lib.baze.ui.main.ActivityRetainedState
import com.fwhyn.lib.baze.ui.main.rememberActivityState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val vm: MainViewModel by viewModels()
    private var onActivityResult: ((activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? =
        null

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("This Baze App is", "${if (BuildConfig.DEBUG) "debug" else "release"} version")

        val activityRetainedState = vm.activityRetainedState
        animateSplashScreen(activityRetainedState)
        enableEdgeToEdge()
        setContent {
            MyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        activityState = rememberActivityState(
                            window = calculateWindowSizeClass(this),
                            onActivityResult = onActivityResult
                        ),
                        activityRetainedState = activityRetainedState,
                    )
                }
            }
        }
        finishWhenNeeded(activityRetainedState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, caller: ComponentCaller) {
        onActivityResult?.invoke(this, requestCode, resultCode, data)
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