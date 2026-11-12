package com.fwhyn.app.deandro.feature.presentation.learncompose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun RecompositionDemo() {
    println("RecompositionDemo called")

    var count by remember {
        mutableIntStateOf(0)
    }

    Column {
        Text(
            text = "Count: $count",
        )

        Button(
            onClick = {
                count++
            }
        ) {
            Text(text = "Increment")
        }
    }
}