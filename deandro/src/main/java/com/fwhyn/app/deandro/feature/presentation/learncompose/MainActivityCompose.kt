package com.fwhyn.app.deandro.feature.presentation.learncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fwhyn.app.deandro.common.ui.config.MyTheme

class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MyTheme {
                Column {
                    Spacer(Modifier.height(100.dp))
                    RecompositionDemo()
                }
            }
        }
    }
}